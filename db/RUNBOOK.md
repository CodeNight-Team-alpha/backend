# Cashback Battle Arena — DB Runbook

**All commands assume you are in the backend project directory** (where `docker-compose.yml` and `db/` live).

- **Start PostgreSQL:** Run `docker compose up -d`. Wait until healthy: `docker compose ps` shows `healthy` for `arena-postgres`.
- **Apply schema:** `psql postgresql://arena:arena@localhost:5432/arena -f db/schema.sql`
- **Apply seed:** `psql postgresql://arena:arena@localhost:5432/arena -f db/seed.sql`
- **Verify tables:** `psql postgresql://arena:arena@localhost:5432/arena -c "\dt"` — should list all 11 tables.
- **Verify seed:** `psql postgresql://arena:arena@localhost:5432/arena -c "SELECT challenge_id, reward_points, priority FROM challenges ORDER BY priority;"` and `psql postgresql://arena:arena@localhost:5432/arena -c "SELECT badge_id, badge_name, threshold_points FROM badges;"`
- **Without local psql:** Run schema inside container: `docker compose exec -T postgres psql -U arena -d arena < db/schema.sql` then same for `db/seed.sql`.
- **Stop DB:** `docker compose down`. Data persists in volume `arena-pgdata`. Use `docker compose down -v` to remove volume and reset data.
