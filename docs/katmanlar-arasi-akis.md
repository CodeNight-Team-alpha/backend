# Katmanlar Arası Akış

**RiskPulse Backend — Basit Mimari Sunumu**

---

## 1. Katmanlar Neler?

| Katman | Paket | Sorumluluk |
|--------|--------|-------------|
| **API** | `api.controller`, `api.dto` | HTTP isteği/yanıt, thin controller |
| **Application** | `application.service`, `application.mapper` | Orkestrasyon, use case, DTO dönüşümü |
| **Domain** | `domain.model`, `domain.service` | İş kuralları, saf hesaplama (Spring/JPA yok) |
| **Port** | `port` | Arayüz (örn. `RiskStore`) |
| **Persistence** | `persistence.entity`, `persistence.repository` | Veritabanı erişimi, entity'ler |

---

## 2. Genel Akış Yönü

```
  İstek (HTTP)
       │
       ▼
  ┌─────────────┐
  │   API       │  Controller → DTO (Request/Response)
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐
  │ Application │  Service → orkestrasyon, mapper
  └──────┬──────┘
         │
    ┌────┴────┐
    ▼         ▼
  ┌──────┐  ┌──────────────┐
  │Domain│  │ Persistence  │  Domain: kurallar  |  Persistence: repo/entity
  └──────┘  └──────────────┘
```

**Kural:** Üst katman alt katmanı çağırır; domain dışa bağımlı olmaz.

---

## 3. Örnek 1: Basit Okuma — Risk Listesi

**İstek:** `GET /api/risks`

| Adım | Katman | Sınıf | Ne yapar? |
|------|--------|--------|------------|
| 1 | API | `RiskController` | `riskService.list()` çağırır |
| 2 | Application | `RiskService` | Port üzerinden `riskStore.findAll()` |
| 3 | Port | `RiskStore` | Arayüz (implementasyon persistence/adapter’da) |
| 4 | Persistence | `InMemoryRiskStore` veya DB adapter | Veriyi döner |
| 5 | Domain | `RiskItem` | Dönen model domain nesnesi |

**Sonuç:** Controller → Service → Port → Adapter → Domain model dönüşü.

---

## 4. Örnek 2: Kullanıcı Snapshot — Çok Katmanlı Okuma

**İstek:** `GET /api/v1/users/{userId}/snapshot?asOfDate=...`

| Adım | Katman | Ne yapar? |
|------|--------|-----------|
| 1 | **UserDataController** | `snapshotQueryService.getSnapshot(userId, asOfDate)` |
| 2 | **UserSnapshotQueryService** | Birden fazla repository’den veri çeker (UserState, ChallengeAward, PointsLedger, Badge, Notification) |
| 3 | **Persistence** | `UserStateRepository`, `ChallengeAwardRepository`, … |
| 4 | **UserSnapshotMapper** | Entity’leri `UserSnapshotResponse` DTO’ya çevirir |
| 5 | **Controller** | DTO’yu HTTP yanıtı olarak döner |

**Özet:** Controller tek bir application service çağırır; service repo’ları ve mapper’ı kullanır; API’ye sadece DTO gider.

---

## 5. Örnek 3: Motor Çalıştırma — Domain + Persistence

**İstek:** `POST /api/v1/admin/engine/run?asOfDate=...`

| Adım | Katman | Sınıf | Ne yapar? |
|------|--------|--------|------------|
| 1 | API | `AdminEngineController` | `engineOrchestrator.runAll(asOfDate)` |
| 2 | Application | `EngineOrchestrator` | Sırayla: metrics → challenges → points → badges → leaderboard → notifications |
| 3 | Application | `ChallengeAwardService` | Repo’lardan state ve challenge kurallarını alır |
| 4 | Application | Entity → Domain | `UserStateEntity` → `UserState` (domain model) |
| 5 | **Domain** | `ChallengeEngine.evaluate(state, rules)` | Tetiklenen/seçilen challenge’a karar verir (saf Java) |
| 6 | Application | `ChallengeAwardService` | Kararı `ChallengeAwardEntity` yapıp repository ile kaydeder |

**Önemli:** İş kuralı (hangi challenge tetiklendi) **domain** katmanında; veri okuma/yazma **application + persistence** katmanında.

---

## 6. Domain Katmanı Neden Ayrı?

- **ChallengeEngine**, **BadgeEngine**, **LeaderboardRanking** gibi sınıflar:
  - Sadece domain model’leri kullanır (`UserState`, `ChallengeRule`, `ChallengeDecision`).
  - Spring, JPA, HTTP, repository bilmez.
  - Test etmesi kolay; tek başına unit test yazılabilir.

```
Application Service (ChallengeAwardService)
        │
        │  Entity → UserState (mapping)
        ▼
   ChallengeEngine.evaluate(state, rules)  ← Domain
        │
        │  ChallengeDecision
        ▼
Application Service → Entity → Repository.save()
```

---

## 7. Özet: Katmanlar Arası Akış

1. **API:** Sadece isteği alır, DTO’ya çevirir, tek bir application service metodunu çağırır.
2. **Application:** Use case’i yürütür; gerekirse domain service’i çağırır, repository ve mapper kullanır.
3. **Domain:** İş kurallarını uygular; dış katmanlara bağımlılık yok.
4. **Port:** Uygulama ile altyapı arasında sözleşme (örn. `RiskStore`).
5. **Persistence:** Repository ve entity ile veriyi okur/yazar.

**Veri yönü:** HTTP → Controller → Application Service → (Domain Service) / (Repository + Mapper) → Response DTO → HTTP.

---

## 8. Kısa Referans

| Akış türü | Örnek endpoint | Akış |
|-----------|----------------|------|
| Basit okuma (port) | `GET /api/risks` | Controller → Service → Port → Adapter → Domain model |
| Sorgu (repos + mapper) | `GET /api/v1/users/{id}/snapshot` | Controller → QueryService → Repos + Mapper → DTO |
| Komut (domain + persistence) | `POST /api/v1/admin/engine/run` | Controller → Orchestrator → Services → Domain Engine + Repos |

*Sunum sonu.*
