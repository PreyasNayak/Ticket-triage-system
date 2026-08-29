# Ticket Triage System — Backend (Day 1–4)

Combined build + DevOps plan. Day 1–2: Spring Boot backend, MySQL, and Docker
wired together from the first commit. Day 3–4: Ticket CRUD API, a service
layer with unit tests, and a GitHub Actions pipeline that runs on every push.

## Run it

1. Copy `.env.example` to `.env` and fill in real values (never commit `.env` — it's already gitignored).
2. From the project root:
   ```
   docker compose up --build
   ```
3. Check that everything is talking to everything:
   ```
   curl http://localhost:8080/api/health
   curl http://localhost:8080/actuator/health
   ```
   Both should return `"status":"UP"`. If `actuator/health` shows the DB as down,
   check your `.env` values and that the `mysql` container passed its healthcheck
   (`docker compose ps`).

## What's here

- `backend/` — Spring Boot 3 app (Java 17, Maven). Actuator is enabled now so
  Prometheus can scrape it later (Day 12–13) without any rework.
- `docker-compose.yml` — backend + MySQL. Backend won't start until MySQL's
  healthcheck passes, so you won't see confusing connection-refused errors on
  first boot.
- `.env.example` — template for local secrets.

## Ticket API (Day 3–4)

| Method | Path              | Description                     |
|--------|-------------------|----------------------------------|
| POST   | `/api/tickets`      | Create a ticket                  |
| GET    | `/api/tickets`      | List all tickets                 |
| GET    | `/api/tickets/{id}` | Get one ticket                   |
| PUT    | `/api/tickets/{id}` | Update a ticket                  |
| DELETE | `/api/tickets/{id}` | Delete a ticket                  |

Example:
```
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{"title":"Login page returns 500","description":"Users see a server error on submit."}'
```

`priority` and `category` are on the entity but stay `null` until the
classification engine lands in Day 5–7.

## Tests

`TicketServiceTest` mocks the repository with Mockito, so it runs with no DB
and no Spring context — fast enough to run on every commit. Run it locally
with:
```
cd backend
mvn test
```

## CI (Day 3–4)

`.github/workflows/ci.yml` runs on every push/PR to `main`:
1. **test** — sets up JDK 17, runs `mvn test`
2. **build-image** — only runs if tests pass, builds the Docker image to
   confirm the Dockerfile still works

No registry push yet — that's Day 11, once we have somewhere to deploy to.

## Next up (Day 5–7)

- Add Ollama as a service in `docker-compose.yml`
- Build the classification engine (LLM + keyword fallback) that fills in
  `priority` and `category`
- Priority queue logic on top of classification results
