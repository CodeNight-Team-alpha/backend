-- Challenge tamamlanma tarihi: bildirimde gösterilecek gün (as_of_date)
ALTER TABLE notifications
  ADD COLUMN IF NOT EXISTS completed_at DATE;

-- Mevcut satırlar için completed_at'i source_ref'ten türet (AW-{userId}-{yyyy-MM-dd})
UPDATE notifications n
SET completed_at = (regexp_match(n.source_ref, '(\d{4}-\d{2}-\d{2})$'))[1]::date
WHERE n.completed_at IS NULL AND n.source_ref ~ '\d{4}-\d{2}-\d{2}$';
