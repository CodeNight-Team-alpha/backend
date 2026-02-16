-- Mevcut bildirim mesajlarını ve challenge puanlarını tablodaki kurallara göre günceller.
-- (Eski "Challenge completed: +10 points" → Türkçe + doğru puan)

-- 1) notifications.message: challenge_awards ile eşleşen kayıtları Türkçe mesaja güncelle
UPDATE notifications n
SET message = (
  CASE ca.challenge_id
    WHEN 'C-01' THEN 'Günlük Harcama'
    WHEN 'C-02' THEN 'Kategori Avcısı'
    WHEN 'C-03' THEN 'Elektronik Bonus'
    WHEN 'C-04' THEN 'Haftalık Aktif'
    ELSE 'Görev'
  END
) || ' tamamlandı: +' || COALESCE(ca.reward_points, 0)::text || ' puan'
FROM challenge_awards ca
WHERE n.source_ref = ca.award_id
  AND n.user_id = ca.user_id
  AND ca.challenge_id IS NOT NULL;

-- 1b) 0 puanlı günler (hiç görev tamamlanmadı): mesajı Türkçe net ifadeye çek
UPDATE notifications n
SET message = 'Bu gün tamamlanan görev yok.'
FROM challenge_awards ca
WHERE n.source_ref = ca.award_id
  AND n.user_id = ca.user_id
  AND (ca.reward_points = 0 OR ca.reward_points IS NULL OR ca.challenge_id IS NULL);

-- 2) points_ledger: challenge kaynaklı satırların puanını challenge_awards.reward_points ile güncelle
UPDATE points_ledger pl
SET points = ca.reward_points
FROM challenge_awards ca
WHERE pl.source = 'challenge'
  AND pl.source_ref = ca.award_id
  AND pl.user_id = ca.user_id
  AND ca.reward_points IS NOT NULL;
