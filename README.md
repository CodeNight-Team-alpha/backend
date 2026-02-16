# RiskPulse Backend

Minimal Spring Boot backend for hackathon. Skeleton only.

## Requirements

- **Java 17**

## Run

```bash
./mvnw spring-boot:run
# or: mvn spring-boot:run   (note: hyphen in spring-boot)
```

API: `GET http://localhost:8080/api/risks` (sample data from in-memory store).

## Structure

- `api` — REST controller (thin, delegates to service)
- `application` — service layer
- `domain` — domain model (RiskEngine to be added later)
- `port` — store interface (outbound contract)
- `adapter` — in-memory implementation with sample data
