-- Cashback Battle Arena — Seed: challenges + badges (idempotent)

-- Challenges (4 rows: C-01..C-04)
INSERT INTO challenges (challenge_id, name, reward_points, priority, is_active)
VALUES
    ('C-01', 'Daily Spender', 10, 1, true),
    ('C-02', 'Category Explorer', 15, 2, true),
    ('C-03', 'Electronics Fan', 20, 3, true),
    ('C-04', 'Week Warrior', 25, 4, true)
ON CONFLICT (challenge_id) DO NOTHING;

-- Badges (3 rows: B-01..B-03 — Bronze 200, Silver 600, Gold 1000)
INSERT INTO badges (badge_id, badge_name, threshold_points)
VALUES
    ('B-01', 'Bronze', 200),
    ('B-02', 'Silver', 600),
    ('B-03', 'Gold', 1000)
ON CONFLICT (badge_id) DO NOTHING;
