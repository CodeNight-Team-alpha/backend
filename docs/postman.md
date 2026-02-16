# Postman ile Test Talimatları

Backend çalışırken (`mvn spring-boot:run`) base URL: **http://localhost:8080**

## Sorun giderme

- **Port 8080 already in use**: Önceki Spring Boot instance hâlâ çalışıyorsa port meşgul olur.  
  - 8080’i kullanan process’i bulup sonlandırın: `lsof -i :8080` → `kill <PID>`  
  - Veya farklı portla çalıştırın: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081` (Postman’da base URL’i 8081 yapın)
- **Schema-validation: points_ledger column [points]**: DB’de kolon `NUMERIC` olmalı, entity’de alan `BigDecimal` (precision 12, scale 2) ile eşleşmeli. Eğer hata alıyorsanız `PointsLedgerEntity.pointsDelta` tipini kontrol edin.

## 1. Engine’i çalıştır (tüm adımlar)

**POST** `/api/v1/admin/engine/run?asOfDate=2025-02-15`

- Sırasıyla: metrics → challenges → points → badges → leaderboard → notifications çalışır.
- `asOfDate` verilmezse: DB’deki `transactions` tablosundan `MAX(transaction_date)` kullanılır; yoksa bugünün tarihi.

**Örnek:**
```http
POST http://localhost:8080/api/v1/admin/engine/run?asOfDate=2025-02-15
```

**Beklenen cevap (200 OK):**
```json
{
  "asOfDate": "2025-02-15",
  "status": "OK"
}
```

---

## 2. Kullanıcı snapshot (tek kullanıcı özeti)

**GET** `/api/v1/users/{userId}/snapshot?asOfDate=2025-02-15`

- `userId`: path’te kullanıcı id.
- `asOfDate`: isteğe bağlı; verilmezse max transaction date veya bugün.

**Örnek:**
```http
GET http://localhost:8080/api/v1/users/U1/snapshot?asOfDate=2025-02-15
```

**Beklenen cevap (200 OK):**
```json
{
  "userId": "U1",
  "asOfDate": "2025-02-15",
  "metrics": {
    "spendToday": 100.50,
    "uniqueCategoriesToday": 2,
    "electronicsSpendToday": 50.00,
    "spend7d": 350.25
  },
  "challenge": {
    "awardId": "CA-...",
    "challengeId": "ch-1",
    "rewardPoints": 10
  },
  "points": { "totalPoints": 25 },
  "badges": [
    { "badgeId": "B1", "badgeName": "Starter", "threshold": 10 }
  ],
  "notifications": [
    { "id": 1, "sourceRef": "ref-1", "message": "...", "createdAt": "2025-02-15T12:00:00Z" }
  ]
}
```

---

## 3. Leaderboard

**GET** `/api/v1/leaderboard?asOfDate=2025-02-15`

- `asOfDate`: isteğe bağlı.

**Örnek:**
```http
GET http://localhost:8080/api/v1/leaderboard?asOfDate=2025-02-15
```

**Beklenen cevap (200 OK):**
```json
{
  "asOfDate": "2025-02-15",
  "top": [
    { "rank": 1, "userId": "U1", "displayName": "Alice", "totalPoints": 50 },
    { "rank": 2, "userId": "U2", "displayName": "Bob", "totalPoints": 30 }
  ]
}
```

---

## 4. Kullanıcı bildirimleri

**GET** `/api/v1/users/{userId}/notifications?asOfDate=2025-02-15`

- `userId`: path’te kullanıcı id.
- `asOfDate`: isteğe bağlı (şu an filtre uygulanmıyor; tüm bildirimler dönüyor).

**Örnek:**
```http
GET http://localhost:8080/api/v1/users/U1/notifications?asOfDate=2025-02-15
```

**Beklenen cevap (200 OK):** Bildirim listesi (array).

---

## Adım adım test akışı

1. Uygulamayı başlat: `mvn spring-boot:run`
2. **POST** `/api/v1/admin/engine/run?asOfDate=YYYY-MM-DD` ile engine’i çalıştır (DB’de o tarihte transaction olmalı).
3. **GET** `/api/v1/users/<userId>/snapshot?asOfDate=...` ile kullanıcı özetini kontrol et.
4. **GET** `/api/v1/leaderboard?asOfDate=...` ile sıralamayı kontrol et.
5. **GET** `/api/v1/users/<userId>/notifications` ile bildirimleri kontrol et.

Tek adım çalıştırmak için:
- **POST** `/api/v1/admin/engine/metrics?asOfDate=...`
- **POST** `/api/v1/admin/engine/challenges?asOfDate=...`
- **POST** `/api/v1/admin/engine/points?asOfDate=...`
- **POST** `/api/v1/admin/engine/badges?asOfDate=...`
- **POST** `/api/v1/admin/engine/leaderboard?asOfDate=...`
- **POST** `/api/v1/admin/engine/notifications?asOfDate=...`
