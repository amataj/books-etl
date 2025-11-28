## PostgreSQL & Temporal: Setup and Troubleshooting

This project runs **two separate PostgreSQL databases** in Docker:

- **App DB** – the main application database (`booksEtl`), service: `postgresql-books-etl`.
- **Temporal DB** – used only by Temporal, service: `postgresql-temporal`.

Both are started via `src/main/docker/services.yml`.

---

## Services Overview

- App DB (`src/main/docker/postgresql.yml`)

  - Service name: `postgresql-books-etl`
  - Image: `postgres:17.4`
  - Database: `booksEtl`
  - User: `booksEtl`
  - Password: `booksEtl`
  - Host port: `localhost:5432`
  - Volume: `postgres-data:/var/lib/postgresql/data`

- Temporal DB (`src/main/docker/temporal.yml`)
  - Service name: `postgresql-temporal`
  - Image: `postgres:17.4`
  - Database: `temporal`
  - User: `temporal`
  - Password: `temporal`
  - Host port: `localhost:15432`
  - Volume: `temporal-postgres-data:/var/lib/postgresql/data`

The volumes are declared in `src/main/docker/services.yml`.

---

## Starting the Stack

From the repo root:

- Start infra (DBs, Kafka, Temporal, Tesseract):

  - `cd src/main/docker`
  - `docker compose -f services.yml up -d`

- Optionally start the app container:
  - `docker compose -f app.yml up -d`

The app container is configured to talk to the app DB using the `booksEtl/booksEtl` credentials.

---

## Local Development (without app container)

When running the app via Maven (`./mvnw`), you use the host environment:

- DB URL (from `application-dev.yml` / `application-prod.yml`):
  - `jdbc:postgresql://localhost:5432/booksEtl`
  - User: `booksEtl`
  - Password: `${SPRING_DATASOURCE_PASSWORD:booksEtl}`

To avoid password mismatch errors when running locally, either:

- Start with explicit env vars:
  - `SPRING_DATASOURCE_PASSWORD=booksEtl SPRING_LIQUIBASE_PASSWORD=booksEtl ./mvnw`

or

- Configure your IDE run configuration with:
  - `SPRING_DATASOURCE_PASSWORD=booksEtl`
  - `SPRING_LIQUIBASE_PASSWORD=booksEtl`

---

## Common Errors and Fixes

### 1. “The server requested SCRAM-based authentication, but no password was provided.”

**Cause:**

- PostgreSQL is configured to require a password (SCRAM), but the app sends **no password**.

**Typical scenarios:**

- `spring.datasource.password` is empty in `application-*.yml`.
- `SPRING_DATASOURCE_PASSWORD` is not set when running locally.

**Fix:**

- Ensure `application-dev.yml` / `application-prod.yml` have:
  - `spring.datasource.password: ${SPRING_DATASOURCE_PASSWORD:booksEtl}`
- When running locally:
  - `SPRING_DATASOURCE_PASSWORD=booksEtl SPRING_LIQUIBASE_PASSWORD=booksEtl ./mvnw`

---

### 2. “FATAL: password authentication failed for user \"booksEtl\"”

**Cause:**

- The app is sending a password, but it **does not match** the password stored for the `booksEtl` role in Postgres.
- This often happens after changing `POSTGRES_PASSWORD` while reusing an existing volume: the image **does not** change the password of an already-initialized database.

**How to confirm from host:**

- `psql -h localhost -p 5432 -U booksEtl booksEtl`
  - If you get `booksEtl=#`, the password you entered is correct.
  - If you see `FATAL: password authentication failed`, the DB and app disagree.

**Fix (simple, wipes dev data):**

1. From `src/main/docker`:
   - `docker compose -f services.yml down -v`
2. Start fresh:
   - `docker compose -f services.yml up -d`
3. Verify:
   - `psql -h localhost -p 5432 -U booksEtl booksEtl` → enter `booksEtl`
4. Run the app with matching env vars (see “Local Development” above).

**Fix (keep data, more advanced):**

- Exec into the Postgres container and change the password:
  - `docker exec -it booksetl-postgresql-books-etl-1 psql -U postgres`
  - `ALTER USER "booksEtl" WITH PASSWORD 'booksEtl';`

---

### 3. “service ... refers to undefined volume temporal-postgres-data”

**Cause:**

- A service mounts a named volume that has not been declared under `volumes:` in the compose file.

**Fix:**

- Ensure `src/main/docker/services.yml` has:
  - `volumes:`  
    `  postgres-data:`  
    `  temporal-postgres-data:`

---

### 4. “network ... not found” when starting services

**Cause:**

- Docker Compose containers reference a network that was removed manually or by a previous `down`, leaving stale container metadata.

**Fix:**

From `src/main/docker`:

- `docker compose -f services.yml down --remove-orphans`
- `docker compose -f services.yml up -d`

This removes orphan containers and recreates the network.

---

## Volumes and Data Location

- App DB:

  - Named volume: `postgres-data`
  - Internal mount: `/var/lib/postgresql/data` in the `postgresql-books-etl` container.

- Temporal DB:
  - Named volume: `temporal-postgres-data`
  - Internal mount: `/var/lib/postgresql/data` in the `postgresql-temporal` container.

On macOS with Docker Desktop, **named volumes live inside Docker’s internal VM**, not directly in your filesystem. You can inspect them with:

- `docker volume inspect postgres-data`
- `docker volume inspect temporal-postgres-data`

If you need host-visible storage, replace the named volume with a bind mount, e.g.:

- `- ~/volumes/booksEtl/postgres:/var/lib/postgresql/data`

---

## Quick Checklist When DB/Temporal Fails

- [ ] Are `services.yml` and `postgresql.yml` started (`docker ps` shows Postgres containers)?
- [ ] Does `psql -h localhost -p 5432 -U booksEtl booksEtl` succeed with password `booksEtl`?
- [ ] If running via `./mvnw`, are `SPRING_DATASOURCE_PASSWORD` and `SPRING_LIQUIBASE_PASSWORD` set to `booksEtl` (or unset so the default applies)?
- [ ] Is Temporal using its own DB (`postgresql-temporal` on port `15432`) and not the app DB?
- [ ] If you recently changed `POSTGRES_PASSWORD`, did you either drop the volume (`down -v`) or update the `booksEtl` user password manually?

Following this sequence should resolve most PostgreSQL and Temporal connectivity or authentication issues in this project.
