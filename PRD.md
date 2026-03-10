# PRD: Owners Excel Import/Export

## Overview

Add one-click XLSX export and multipart import for `Owner` records so administrators can bulk-edit owners in a spreadsheet and re-import changes with conflict resolution and validation.

## Tasks

### Task 1: Update OpenAPI spec (add/correct export & import paths)

- **Description**: Fix indentation and ensure `/api/owners/export` (GET) and `/api/owners/import` (POST multipart/form-data) are defined at the top-level of `/api/owners` in `openapi.yaml`.
- **Files**: `openapi.yaml`
- **Definition of Done**: Paths appear correctly in `openapi.yaml` and OpenAPI generation will include the two operations.
- **Tests**: None (manual OpenAPI validation / `./mvnw generate-sources`).

### Task 2: Regenerate OpenAPI sources

- **Description**: Run OpenAPI code generation to refresh generated API interfaces and DTOs.
- **Files**: generated-sources (no committed changes expected)
- **Definition of Done**: `OwnersApi` (or equivalent generated interface) includes `exportOwnersXlsx` and `importOwnersXlsx` operations.
- **Tests**: `./mvnw generate-sources` completes without error.

### Task 3: Add Apache POI dependency to `pom.xml`

- **Description**: Add `org.apache.poi:poi-ooxml` for XLSX read/write.
- **Files**: `pom.xml`
- **Definition of Done**: Dependency added; `./mvnw -DskipTests compile` succeeds.
- **Tests**: None specific.

### Task 4: Implement `OwnerExcelHelper` (read/write XLSX + heuristics)

- **Description**: New utility to serialize owners to XLSX and parse XLSX into a validation model. Implement auto-fix heuristics (trim, phone normalize, title-case names, skip empty rows, simple city extraction).
- **Files**: `src/main/java/org/springframework/samples/petclinic/util/OwnerExcelHelper.java`
- **Definition of Done**: Can produce an XLSX byte[] from a collection of `Owner` and parse an uploaded XLSX InputStream into parsed records + validation info.
- **Tests**: Unit tests covering parsing, heuristics, and validation.

### Task 5: Add Owner import models and validation/report types

- **Description**: Add classes representing parsed rows, field-level errors, duplicate detection results, and an import report that the controller/service can use to render conflict UI.
- **Files**: `src/main/java/org/springframework/samples/petclinic/service/owner/OwnerImportReport.java` and related classes
- **Definition of Done**: Import API returns structured report when conflicts/errors are found.
- **Tests**: Unit tests for the report construction.

### Task 6: Add service methods in `ClinicService` and `ClinicServiceImpl`

- **Description**: Add `byte[] exportOwnersToXlsx()` and `ImportReport importOwnersFromXlsx(InputStream)` (or similar) with transactional semantics. Keep facade usage consistent with project conventions.
- **Files**: `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`, `ClinicServiceImpl.java`
- **Definition of Done**: Methods present and callable from controller; import method is transactional.
- **Tests**: Unit tests for service import flow (mocking repositories) to verify transactional rollback on errors.

### Task 7: Add REST endpoints for export and import

- **Description**: Implement controller endpoints for `GET /api/owners/export` (returns XLSX) and `POST /api/owners/import` (multipart/form-data `file`) with `@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")`.
- **Files**: `src/main/java/org/springframework/samples/petclinic/rest/controller/OwnerRestController.java` (or generated API implementation file)
- **Definition of Done**: Endpoints compiled and accessible; export returns correct content-type; import returns 204 when successful or 400 with structured report when issues exist.
- **Tests**: MockMvc tests for Happy path and validation/conflict responses.

### Task 8: Ensure import is transactional and secure

- **Description**: Verify the import path runs within a single DB transaction and rolls back on unresolvable errors; enforce role-checks.
- **Files**: service/controller changes from Tasks 6–7
- **Definition of Done**: Tests assert no partial persistence on failure.
- **Tests**: Service-level transactional tests and MockMvc integration tests.

### Task 9: Write unit tests for parser/validation (`OwnerExcelHelperTests`)

- **Description**: Thorough unit coverage for parsing heuristics, validation rules (telephone digits-only rule, required fields), and skipping empty rows.
- **Files**: `src/test/java/.../OwnerExcelHelperTests.java`
- **Definition of Done**: Tests pass locally.

### Task 10: Write MockMvc tests for import endpoint

- **Description**: Use `MockMvc` to POST a multipart XLSX file and assert responses for success, validation errors, and duplicates.
- **Files**: `src/test/java/.../OwnerRestControllerImportTests.java`
- **Definition of Done**: Tests cover at least success, invalid telephone, missing required fields, and duplicate detection.

### Task 11: Add simple UI for export/import and conflict resolution page

- **Description**: Minimal static HTML page with export button and file-upload form; add a conflict resolution UI to present errors and allow overwrite/merge/skip decisions (server-assisted flow acceptable for v1).
- **Files**: `src/main/resources/static/owners-excel.html`, conflict resolution template
- **Definition of Done**: Playwright e2e can automate clicking export, uploading modified file, and resolving a simple conflict.
- **Tests**: Playwright e2e test described below.

### Task 12: Add Playwright e2e test for full export-import flow

- **Description**: End-to-end test that downloads the XLSX, modifies a cell, uploads it, and verifies the change persisted (and conflict UI shown when appropriate).
- **Files**: `e2e/owners-excel.spec.ts`
- **Definition of Done**: Playwright test passes in CI/local run.

## Dependencies

- OpenAPI generation step must run before implementing controller code that depends on generated interfaces.
- Adding Apache POI requires a `pom.xml` change.
