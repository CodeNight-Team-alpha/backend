# Requirements Analysis — Reward / Challenge System

## 0) Amaç (Problem Statement)

Sistem, kullanıcıların harcama (transactions) verilerinden kullanıcı metrikleri üretir, bu metriklere göre challenge kurallarını çalıştırır, tek ödül kuralı ile puan verir, puan hareketlerini ledger'a yazar, leaderboard üretir, rozet atar ve bildirim + dashboard ile kullanıcıya gösterir. (Case 5)

---

## 1) Aktörler

| Aktör | Açıklama |
|-------|----------|
| **Kullanıcı** | Dashboard üzerinden kendi durumunu görür (puan, rozet, challenge, sıralama). |
| **Sistem (Reward Engine)** | Veriyi işler, karar verir, çıktıları üretir. |

---

## 2) Functional Requirements (FR)

### Veri Okuma / Hazırlama

| ID | Açıklama |
|----|----------|
| **FR-1** | Sistem users, merchants, transactions verilerini okuyabilmeli. |
| **FR-2** | as_of_date için işlemleri filtreleyebilmeli (bugün + son 7 gün). |

### Metrik Üretimi (user_state)

| ID | Açıklama |
|----|----------|
| **FR-3** | Her kullanıcı için user_state üretmeli: spend_today, unique_categories_today, electronics_spend_today, spend_7d. |
| **FR-4** | Üretilen metrikler as_of_date ile birlikte saklanabilmeli. |

### Challenge Motoru

| ID | Açıklama |
|----|----------|
| **FR-5** | Aktif challenge'ları user_state üzerinden evaluate edebilmeli (4 kural). |
| **FR-6** | Tetiklenen challenge'lar triggered_challenges olarak kaydedilmeli. |
| **FR-7** | Aynı kullanıcı için birden fazla challenge tetiklenirse: priority değeri en küçük olan seçilmeli; diğerleri suppressed_challenges olarak kaydedilmeli. |
| **FR-8** | Seçilen challenge selected_challenge olarak kaydedilmeli ve tek ödül uygulanmalı. |

### Puanlama (Ledger)

| ID | Açıklama |
|----|----------|
| **FR-9** | Seçilen challenge'ın reward_points değeri kadar puan, points_ledger'a append-only şekilde yazılmalı. |
| **FR-10** | Ledger satırı source='challenge' ve source_ref=award_id ile izlenebilir olmalı. |
| **FR-11** | Kullanıcının total_points değeri yalnızca ledger toplamından hesaplanmalı. |

### Leaderboard

| ID | Açıklama |
|----|----------|
| **FR-12** | Leaderboard üretmeli: sıralama total_points DESC, eşitlik user_id ASC, rank atanmalı. |

### Rozet Sistemi

| ID | Açıklama |
|----|----------|
| **FR-13** | total_points eşiklerine göre rozet atamalı: ≥200 Bronze, ≥600 Silver, ≥1000 Gold. |
| **FR-14** | Rozet atamaları badge_awards olarak kaydedilmeli. |

### Bildirimler

| ID | Açıklama |
|----|----------|
| **FR-15** | Challenge ödülü verildiğinde kullanıcıya bildirim üretmeli ve kaydetmeli. |

### Dashboard

| ID | Açıklama |
|----|----------|
| **FR-16** | Dashboard en az şunları gösterebilmeli: kullanıcı metrikleri, tetiklenen/seçilen challenge, güncel toplam puan, leaderboard, rozetler, bildirimler. |

---

## 3) Non-Functional Requirements (NFR)

| ID | Açıklama |
|----|----------|
| **NFR-1** | **Determinism:** Aynı input + aynı as_of_date → aynı output. |
| **NFR-2** | **Idempotency:** Aynı as_of_date için tekrar çalıştırma, aynı ödülü iki kez yazmamalı (ledger duplication önlenmeli). |
| **NFR-3** | **Auditability:** Puanın kaynağı source/source_ref ile izlenebilir olmalı. |
| **NFR-4** | **Performance:** Küçük dataset'te saniyeler içinde üretim (bootcamp case standardı). |
| **NFR-5** | **Observability:** Her run'da log: kaç user işlendi, kaç award çıktı, kaç ledger satırı yazıldı. |

---

## 4) Varsayımlar / Karar Noktaları (Assumptions)

| ID | Açıklama |
|----|----------|
| **A-1** | as_of_date = sistemin çalıştığı gün (default). |
| **A-2** | “Son 7 gün” penceresi: as_of_date dahil geriye 7 gün (implementation net seçilmeli). |
| **A-3** | Challenge tanımları config'tir; kullanıcı seçimi yoktur. |
| **A-4** | Leaderboard snapshot üretilebilir (her run'da yeniden hesaplanır). |

---

## 5) Kabul Kriterleri (Acceptance Criteria) — minimal

| ID | Açıklama |
|----|----------|
| **AC-1** | Her user için user_state doğru hesaplanır. |
| **AC-2** | Challenge tetikleme + priority seçimi doğru çalışır. |
| **AC-3** | Aynı gün tekrar koşmada ledger duplication oluşmaz. |
| **AC-4** | Leaderboard sort + tie-break doğru. |
| **AC-5** | Badge thresholds doğru atanır. |
| **AC-6** | Dashboard çıktısı eksiksiz alanları döner. |

---

## 6) Yapı Eşlemesi (Mevcut Skeleton → FR)

Mevcut **api → application → domain → port → adapter** yapısına göre önerilen dağılım:

| Katman | Sorumluluk (FR ile) |
|--------|----------------------|
| **domain** | UserState, Challenge (kural tanımı), LedgerEntry, Badge, Notification; Challenge evaluator (FR-5–8). |
| **port** | TransactionStore, UserStore, MerchantStore; PointsLedgerWriter, BadgeAwardStore, NotificationStore; ChallengeConfigReader. |
| **adapter** | CSV/InMemory: users, merchants, transactions; Ledger append; badge_awards, notifications persistence. |
| **application** | RewardEngine orquestrasyonu: metrik üretimi (FR-3–4), challenge evaluate (FR-5–8), ledger yazma (FR-9–11), leaderboard (FR-12), badge (FR-13–14), notification (FR-15). Idempotency (NFR-2) burada (as_of_date key). |
| **api** | Dashboard endpoint (FR-16): tek response ile metrics, selected_challenge, total_points, leaderboard, badges, notifications. İsteğe bağlı: run engine trigger (batch). |

Bu doküman, implementasyon sırasında referans olarak kullanılabilir.
