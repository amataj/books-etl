---
title: 'AGENTS.md'
version: 1.0
scope: 'books-etl-clean-architecture'
language: 'Polyglot (Java, TS, Infra-as-Code)'
purpose: 'Ground rules for Codex/MCP agents collaborating inside the books-etl monorepo.'
---

---

# Agents Operating Manual

## 1. Mission

- Keep every change aligned with Clean Architecture and the rules documented in `LAYER-TO-CLLEAN-ARCHITECTURE-AGENTS.md`.
- Favor incremental, reversible steps; surface ambiguities before implementing risky assumptions.
- Protect existing behavior: when unsure, add automated checks instead of skipping validation.

## 2. Responsibilities

- **Software architecture**: enforce `adapter → usecase → domain ← infrastructure` boundaries and update wiring when classes move.
- **Backend & data engineering**: treat `booksetl` as the system of record; coordinate Java Spring, Node.js/TypeScript, and ETL artifacts.
- **Documentation**: refresh this file or the Layer guide whenever team-wide rules change; annotate non-obvious code paths with brief comments.

## 3. Operating Principles

1. Start every task by reading both this file and the Layer guide; if they conflict, pause and ask.
2. Prefer deterministic automation (`mvnw`, `npmw`, `scripts/*`) over manual steps.
3. Keep domain models JDK-only; Spring, database, or framework references belong to adapters or infrastructure.
4. When moving files, update imports, package declarations, and configuration scans in the same change.
5. Run targeted tests/lints whenever touching a component; document skipped checks in PR descriptions.

## 4. Collaboration Workflow

- **Plan**: Outline steps unless the task is trivial; update the plan after completing each step.
- **Implement**: Use `apply_patch` for focused edits; avoid bulk formatting unless required.
- **Validate**: Execute the smallest meaningful test suite (unit, ArchUnit, lint) to prove safety.
- **Report**: Summarize what changed, where, and why; mention any follow-up actions the next agent should tackle.

## 5. Tooling Map

- `mvnw -ntp --batch-mode` — default for backend builds/tests; keeps logs concise.
- `npmw`/`node_modules/.bin/*` — invoke JS tooling without relying on global installs.
- `rg`, `sed`, `jq` — preferred discovery utilities; faster and script-friendly.
- `scripts/` — contains project-specific helpers; read script headers before running.

## 6. Quality Gates

- No circular dependencies across layers; enforce via ArchUnit (`TechnicalStructureTest`).
- Domain objects use identity-based `equals`/`hashCode`; mutators validate invariants.
- REST adapters expose only use-case interfaces; never depend directly on repositories.
- Infrastructure adapters remain stateless and hold zero business logic.

## 7. Escalation & Communication

- When blocked by missing context or sandbox restrictions, document the issue and request guidance.
- If unexpected filesystem changes appear, stop working and ask the user before proceeding.
- Treat secrets and credentials as out-of-scope data; never add them to the repo or logs.

## 8. Domain Set Model

- These descriptors define the authoritative domain surface for books-etl; transform them into domain models/ports when applying Clean Architecture.
- `Book` — keyed by `documentId` (10-64 chars), tracks metadata (`title`, `author`, `lang`, `pages`), owns one-to-many links to `BookFile` (`file`) and `BookPageText` (`pageText`).
- `BookFile` — stores file-system metadata (`pathNorm`, `sha256`, `sizeBytes`, timestamps) and belongs to a `Book` via `documentId`; use TextBlob for large strings.
- `BookPageText` — per-page payload (`pageNo`, `text`) tied to a `Book` by `documentId`; enforce `pageNo >= 1`.
- `IngestRun` — execution record (`startedAt`, `finishedAt`, `status`, counter fields with `min=0`); no relationships but referenced by events.
- `IngestEvent` — message log (`runId`, `documentId`, `topic`, `payload`, `createdAt`) linked many-to-one to `IngestRun`.
- Keep these JSON files as the source of truth when regenerating entities or keeping migration metadata (`changelogDate`, `applications`, DTO/service styles) in sync.

## 9. References

- `LAYER-TO-CLLEAN-ARCHITECTURE-AGENTS.md` — migration playbook.
- `README.md` — repo overview and build instructions.
- `/scripts/*.md` or inline docs — tooling-specific instructions.

Stay disciplined, automate verifications, and leave breadcrumbs for the next agent.
