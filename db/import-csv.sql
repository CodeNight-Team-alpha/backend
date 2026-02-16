-- Import CSVs into users, merchants, transactions via staging (column mapping).
-- Run from backend project root: psql postgresql://arena:arena@localhost:5432/arena -f db/import-csv.sql

-- Staging: match CSV structure
CREATE TEMP TABLE users_stage (user_id text, name text, segment text);
CREATE TEMP TABLE merchants_stage (merchant_id text, merchant_name text, category text);
CREATE TEMP TABLE transactions_stage (transaction_id text, user_id text, date text, merchant_id text, category text, amount_try text);

-- COPY from CSV (path relative to client; run from backend root)
\copy users_stage(user_id, name, segment) FROM 'src/main/resources/csv/users.csv' WITH (FORMAT csv, HEADER true)
\copy merchants_stage(merchant_id, merchant_name, category) FROM 'src/main/resources/csv/merchants.csv' WITH (FORMAT csv, HEADER true)
\copy transactions_stage(transaction_id, user_id, date, merchant_id, category, amount_try) FROM 'src/main/resources/csv/transactions.csv' WITH (FORMAT csv, HEADER true)

-- Map into real tables
INSERT INTO users (user_id, display_name)
SELECT user_id, name FROM users_stage
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO merchants (merchant_id, name, category)
SELECT merchant_id, merchant_name, category FROM merchants_stage
ON CONFLICT (merchant_id) DO NOTHING;

INSERT INTO transactions (transaction_id, user_id, merchant_id, amount, transaction_date)
SELECT transaction_id, user_id, merchant_id, amount_try::numeric(12,2), date::date FROM transactions_stage
ON CONFLICT (transaction_id) DO NOTHING;

-- Verification
SELECT 'users' AS tbl, COUNT(*) AS cnt FROM users
UNION ALL SELECT 'merchants', COUNT(*) FROM merchants
UNION ALL SELECT 'transactions', COUNT(*) FROM transactions;
