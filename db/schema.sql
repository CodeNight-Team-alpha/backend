-- Cashback Battle Arena — DDL (idempotent: run once or use IF NOT EXISTS where supported)
-- PostgreSQL 16

-- 1) users
CREATE TABLE IF NOT EXISTS users (
    user_id     VARCHAR(50) PRIMARY KEY,
    display_name VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 2) merchants
CREATE TABLE IF NOT EXISTS merchants (
    merchant_id VARCHAR(50) PRIMARY KEY,
    name        VARCHAR(255),
    category    VARCHAR(100),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 3) transactions
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   VARCHAR(50) PRIMARY KEY,
    user_id          VARCHAR(50) NOT NULL REFERENCES users(user_id),
    merchant_id      VARCHAR(50) NOT NULL REFERENCES merchants(merchant_id),
    amount           NUMERIC(12,2) NOT NULL,
    transaction_date DATE NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON transactions (user_id, transaction_date);
CREATE INDEX IF NOT EXISTS idx_transactions_merchant ON transactions (merchant_id);

-- 4) user_state
CREATE TABLE IF NOT EXISTS user_state (
    user_id                 VARCHAR(50) NOT NULL REFERENCES users(user_id),
    as_of_date              DATE NOT NULL,
    spend_today             NUMERIC(12,2) NOT NULL DEFAULT 0,
    unique_categories_today INT NOT NULL DEFAULT 0,
    electronics_spend_today NUMERIC(12,2) NOT NULL DEFAULT 0,
    spend_7d                NUMERIC(12,2) NOT NULL DEFAULT 0,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, as_of_date)
);

-- 5) challenges (config)
CREATE TABLE IF NOT EXISTS challenges (
    challenge_id  VARCHAR(50) PRIMARY KEY,
    name          VARCHAR(255),
    reward_points INT NOT NULL,
    priority      INT NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 6) challenge_awards (one selected award per user per day)
CREATE TABLE IF NOT EXISTS challenge_awards (
    award_id    VARCHAR(50) PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL REFERENCES users(user_id),
    challenge_id VARCHAR(50) NOT NULL REFERENCES challenges(challenge_id),
    as_of_date  DATE NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, as_of_date)
);
CREATE INDEX IF NOT EXISTS idx_challenge_awards_user_date ON challenge_awards (user_id, as_of_date);

-- 7) points_ledger (append-only; idempotency via source + source_ref)
CREATE TABLE IF NOT EXISTS points_ledger (
    id         BIGSERIAL PRIMARY KEY,
    user_id    VARCHAR(50) NOT NULL REFERENCES users(user_id),
    points     NUMERIC(12,2) NOT NULL,
    source     VARCHAR(50) NOT NULL,
    source_ref VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (source, source_ref)
);
CREATE INDEX IF NOT EXISTS idx_points_ledger_user ON points_ledger (user_id);

-- 8) leaderboard (snapshot per as_of_date)
CREATE TABLE IF NOT EXISTS leaderboard (
    as_of_date   DATE NOT NULL,
    user_id      VARCHAR(50) NOT NULL REFERENCES users(user_id),
    rank         INT NOT NULL,
    total_points NUMERIC(12,2) NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (as_of_date, user_id)
);
CREATE INDEX IF NOT EXISTS idx_leaderboard_date_rank ON leaderboard (as_of_date, rank);

-- 9) badges (config)
CREATE TABLE IF NOT EXISTS badges (
    badge_id         VARCHAR(50) PRIMARY KEY,
    badge_name       VARCHAR(100) NOT NULL,
    threshold_points INT NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 10) badge_awards (one per user per badge)
CREATE TABLE IF NOT EXISTS badge_awards (
    user_id    VARCHAR(50) NOT NULL REFERENCES users(user_id),
    badge_id   VARCHAR(50) NOT NULL REFERENCES badges(badge_id),
    awarded_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, badge_id),
    UNIQUE (user_id, badge_id)
);

-- 11) notifications (idempotent per user + source_ref)
CREATE TABLE IF NOT EXISTS notifications (
    id          BIGSERIAL PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL REFERENCES users(user_id),
    source_ref  VARCHAR(50) NOT NULL,
    message     TEXT,
    completed_at DATE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, source_ref)
);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications (user_id);
