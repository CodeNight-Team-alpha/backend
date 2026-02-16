-- Add columns for triggered/selected/suppressed and reward_points (run once if missing)
ALTER TABLE challenge_awards
  ADD COLUMN IF NOT EXISTS triggered_challenges TEXT,
  ADD COLUMN IF NOT EXISTS selected_challenge VARCHAR(50),
  ADD COLUMN IF NOT EXISTS suppressed_challenges TEXT,
  ADD COLUMN IF NOT EXISTS reward_points INT;
-- Allow no selected challenge (all suppressed / none triggered)
ALTER TABLE challenge_awards ALTER COLUMN challenge_id DROP NOT NULL;
