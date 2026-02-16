-- Add award_id to badge_awards (run once if missing)
ALTER TABLE badge_awards ADD COLUMN IF NOT EXISTS award_id VARCHAR(50) UNIQUE;

-- Backfill existing rows
UPDATE badge_awards SET award_id = 'BA-' || user_id || '-' || badge_id WHERE award_id IS NULL;
