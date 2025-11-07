---
title: 'LAYER-TO-CLLEAN-ARCHITECTURE-AGENTS.md'
version: 2.0
scope: 'clean-architecture-one-module'
language: 'Java'
purpose: 'Convert layered JHipster backends into the Clean Architecture used by com.mycompany.myapp.blog.'
---

---

# Layer-to-Clean-Architecture Agents Guide

## 1. Goal

Convert a JHipster-generated layered Spring Boot backend into the Clean Architecture structure implemented in `src/main/java/com/mycompany/myapp/blog`, and keep the system aligned with that style after migration.

## 2. Agent Rules

- You act as a backend developer, software architect, and Spring/JHipster expert.
- Treat `AGENTS.md` and this document as authoritative; do not assume behaviour that neither document specifies.
- Work under `src/main/java/com/<company>/<app>/<context>/` unless instructed otherwise.
- Respect the dependency direction `adapter → usecase → domain ← infrastructure`; domain never depends outward.
- Keep domain code framework-free and rely only on Java language and JDK libraries.
- Do not rename public packages without confirmation; place new files inside the layer to which they belong.
- Update `@ComponentScan`, `@EntityScan`, and similar annotations whenever package locations change.
- Do not modify `config`, `security`, or `aop` packages beyond import adjustments needed to reflect moves.
- Run `./mvnw -ntp --batch-mode` after structural changes and execute targeted backend tests when contracts move.
- When pagination or shared primitives change, update the `shared` package and run `npm run backend:unit:test`.

## 3. Dependency Rules

- `adapter` depends only on `usecase.*` interfaces and types from `shared`.
- `usecase` depends only on `domain.*` and `shared`.
- `domain` imports only JDK classes; it must not depend on Spring, adapter, use case, or infrastructure code.
- `infrastructure` implements `domain.repository.*Repository` and `domain.repository.*DataAccessRepository` ports; it never imports adapter packages.
- Non-database infrastructure (messaging, cache) may depend on use-case ports when strictly necessary, but never on adapters.
- Configuration composes beans across layers but must not contain business logic.

## 4. Integration

This guide supports developers and automation (Codex prompts, MCP agents). Follow the steps in Section 5 sequentially and validate each layer boundary with the checks in Section 10.

## 5. High-Level Steps

1. **Introduce the Clean Architecture layout**

   - Under `src/main/java/com/<company>/<app>/<context>/`, create `adapter`, `usecase`, `domain`, `infrastructure`, and `shared` packages alongside existing `config`, `security`, and `aop`.
   - Leave the legacy layered packages in place until their responsibilities are migrated.

2. **Prepare the infrastructure layer**

   - Move JHipster JPA entities from `domain/` to `infrastructure/database/jpa/entity` and rename them to `*Entity` (e.g., `Blog` → `BlogEntity`).
   - Relocate Spring Data repositories into `infrastructure/database/jpa/repository` and rename them to `*JPARepository`.
   - Keep MapStruct mappers with the JPA layer, initially pointing to the legacy DTOs until the domain is carved out.

3. **Model the domain**

   - Move domain-centric DTOs from `service/dto` into `domain/core` and drop the `DTO` suffix.
   - Replace mutable setters with behaviour-driven methods that enforce invariants via `DomainValidationException`.
   - Create data-access repositories (reads/pagination) with format `domain/repository/<Domain>DataAccessRepository` with the read operations which are in the existing `domain/repository`.
   - The new data-access repositories will be used by use-cases to retrieve domain models.
   - The existing repository in `domain/repository` will contain the data-mutation operations (like save, update, partial update, and delete)
   - Move service interfaces into `domain/service` and keep them framework-free.
   - Update implementations (temporarily still in legacy packages) to use the new domain models before relocating them.

4. **Relocate domain services**

   - Move service interface implementations from `service/impl` into `domain/service/impl`.
   - Adapt them to consume domain models and depend on repository ports instead of Spring Data repositories directly.
   - No Spring annotations should remain in the domain layer once this step completes.

5. **Update mappers**

   - Place MapStruct mappers under `infrastructure/database/jpa/mapper`.
   - Adjust mapper signatures to translate between `*Entity` classes and domain models in `domain/core`.
   - Use manual mapping for relationships when MapStruct cannot resolve nested aggregates.

6. **Implement repository adapters**

   - Inside `infrastructure/database/jpa`, create `*JPARepositoryImpl` classes that implement both the data-mutation and data-access ports defined in the domain.
   - Each adapter composes the Spring Data repository and relevant mapper, remains stateless, and contains no business logic.

7. **Create use cases**

   - Introduce `usecase` interfaces (e.g., `BlogInteractor`) and `usecase/impl` classes.
   - Use-case interfaces should be used exclusively by the adapter layer.
   - Use-case implementations orchestrate domain services and repository ports, declare transactions, and keep orchestration concerns (pagination, cross-aggregate checks) out of the domain layer.
   - Use-case implementations should be kept in the `usecase/impl` package.
   - Use-case implementations should be stateless, contain the use-case orchestration logic, and contain no domain business logic.
   - Use-case implementations must use the data-access ports defined in the domain to read data.
   - Use-case implementations must use the domain services to mutate domain data.

8. **Adapt the web layer**

   - Move REST controllers and filters to `adapter/web/rest` and `adapter/web/filter`.
   - Replace direct dependencies on services or repositories with the new use-case interfaces.
   - Controllers should exchange domain models or adapter-specific DTOs defined locally.

9. **Wire everything together**

   - Create or update `BeanConfiguration` under `config` to assemble infrastructure adapters, domain services, and use-case interactors.
   - Expose only port interfaces to adapters and register MapStruct mappers or repository beans as needed.

10. **Clean up cross-layer references**

    - Remove lingering imports from legacy packages, delete obsolete DTOs or services, and ensure adapters reference only use-case ports.
    - Confirm that the dependency direction aligns with the rules in Section 3.

11. **Update and run tests**
    - Realign integration and unit tests with the new package structure.
    - Update `src/test/java/com/<company>/<app>/<context>/TechnicalStructureTest.java` to enforce the new boundaries.
    - Add a test in TechnicalStructureTest to enforce the
    - Run `./mvnw -ntp --batch-mode` to execute ArchUnit checks and ensure dependency isolation.

## 6. Detailed Rewrite Checklist

- [ ] Create domain POJOs in `domain/core` with identity-based `equals()` and `hashCode()`.
- [ ] Introduce repository ports for writes in `domain/repository/<Domain>Repository` and reads in `domain/repository/<Domain>DataAccessRepository`.
- [ ] Move services into `domain/service` and make their implementations depend solely on ports.
- [ ] Build use-case interfaces and implementations that orchestrate domain services and data-access ports define at `domain/repository`.
- [ ] Relocate JPA entities, repositories, mappers, and adapters into `infrastructure/database/jpa`.
- [ ] Ensure `*JPARepositoryImpl` implements both data-access and data-mutation ports defined in the `domain/repository`.
- [ ] Ensure `*JPARepositoryImpl` classes map entities to domain models using MapStruct or manual mapping.
- [ ] Refactor controllers and filters into `adapter` to depend exclusively on use-case interfaces.
- [ ] House pagination and shared primitives (e.g., `PageCriteria`, `PageResult`) in the `shared` package.
- [ ] Remove obsolete DTOs and service classes left from the layered structure.
- [ ] Update configuration annotations and bean definitions to point at new packages.
- [ ] Validate imports so dependencies flow `adapter → usecase → domain ← infrastructure`.
- [ ] Run application and test suites to confirm proper dependency injection and layer isolation.

## 7. Security & Operational Notes

- Never commit secrets, API keys, or credentials.
- Verify external API or network access complies with project permissions.
- Bound transactions to the use-case layer (or configuration) using `@Transactional`; avoid placing transactions in the domain.
- Ensure new beans are discoverable through constructor injection and explicit configuration.

## 8. References

- `AGENTS.md` (Clean Architecture reference for this project)
- Clean Architecture (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
- JHipster Documentation v8+
- OpenAI Agents / MCP best practices

## 9. Legacy-to-Clean Mapping

- `domain/` (legacy JPA entities) → `infrastructure/database/jpa/entity` (persistence) + `domain/core` (domain models).
- `service/dto` → `domain/core` for domain models or `adapter/web/rest/dto` for adapter-specific payloads.
- `service/mapper` → `infrastructure/database/jpa/mapper`.
- `repository` → `infrastructure/database/jpa/repository` (Spring Data) + `domain/repository/<Domain>Repository` & `domain/repository/<Domain>DataAccessRepository` (ports).
- `service/impl` → `domain/service/impl` (domain logic) and `usecase/impl` (orchestration).
- `web/rest` → `adapter/web/rest`; filters belong in `adapter/web/filter`.

## 10. Verification Steps During Migration

- After each layer move, compile and run affected unit tests to catch wiring issues early.
- Keep ArchUnit checks updated so dependency violations surface immediately.
- When replacing DTOs with domain models, adjust controller contracts and add regression tests.
- Confirm transactional annotations reside only where orchestration occurs (use cases/configuration).
- Perform final validation with `./mvnw -ntp --batch-mode` and, when pagination contracts change, run `npm run backend:unit:test`.
