-- Cashback Battle Arena — Seed: challenges + badges (idempotent)

-- Challenges (4 rows: C-01..C-04 — tablodaki kurallara göre)
INSERT INTO challenges (challenge_id, name, reward_points, priority, is_active)
VALUES
    ('C-01', 'Günlük Harcama', 60, 5, true),
    ('C-02', 'Kategori Avcısı', 120, 3, true),
    ('C-03', 'Elektronik Bonus', 180, 2, true),
    ('C-04', 'Haftalık Aktif', 220, 1, true)
ON CONFLICT (challenge_id) DO UPDATE SET
    name = EXCLUDED.name,
    reward_points = EXCLUDED.reward_points,
    priority = EXCLUDED.priority;

-- Badges (3 rows: B-01..B-03 — Bronze 200, Silver 600, Gold 1000)
INSERT INTO badges (badge_id, badge_name, threshold_points)
VALUES
    ('B-01', 'Bronze', 200),
    ('B-02', 'Silver', 600),
    ('B-03', 'Gold', 1000)
ON CONFLICT (badge_id) DO NOTHING;
