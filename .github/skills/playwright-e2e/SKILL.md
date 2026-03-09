---
name: playwright-e2e
description: >
  End-to-end testing and visual regression for Spring Boot REST APIs using Playwright.
  Covers Swagger UI verification, REST API smoke tests, CRUD workflows, and screenshot capture.
  Zero Java dependencies — runs entirely via npx/npm with TypeScript tests.
  USE FOR: e2e tests, browser tests, API smoke tests, Swagger UI tests, screenshot capture,
  visual regression, integration tests, Playwright setup.
---

# Playwright E2E Testing Skill — Spring Boot REST APIs

Run end-to-end browser and API tests against a running Spring Boot application using **Playwright**.
Captures screenshots, validates Swagger UI, and smoke-tests REST endpoints.

## Prerequisites

- Node.js 18+ (for `npx`)
- Running Spring Boot app (port 9966, context `/petclinic/`)

## Quick Start

```bash
# Install Playwright + Chromium (one-time)
npm install
npx playwright install chromium

# Start the app in another terminal
./mvnw spring-boot:run

# Run all tests
npm run playwright:test

# Run only screenshot tests
npm run playwright:screenshots

# Open interactive UI mode
npm run playwright:test:ui

# View HTML report
npm run playwright:report
```

## Project Structure

```
playwright.config.ts       # Playwright configuration (baseURL, browser, reporter)
e2e/
├── petclinic.spec.ts      # Main test file
├── screenshots/           # Captured screenshots (gitignored except .gitkeep)
├── test-results/          # Test artifacts (gitignored)
└── playwright-report/     # HTML report (gitignored)
```

## Test Categories

### 1. Swagger UI Tests (`@screenshot` tag)

Verifies the Swagger UI loads and displays API operations.
Captures full-page screenshots for visual verification.

```typescript
test("Swagger UI loads successfully @screenshot", async ({ page }) => {
  await page.goto("/swagger-ui/index.html");
  await page.waitForSelector(".swagger-ui", { timeout: 15_000 });
  const title = page.locator(".swagger-ui .info .title");
  await expect(title).toBeVisible();
  await page.screenshot({
    path: "e2e/screenshots/swagger-ui.png",
    fullPage: true,
  });
});
```

### 2. REST API Smoke Tests

Validates core API endpoints return expected status codes and data shapes.

```typescript
test("GET /api/owners returns 200", async ({ request }) => {
  const response = await request.get("/api/owners");
  expect(response.status()).toBe(200);
  const body = await response.json();
  expect(Array.isArray(body)).toBe(true);
});
```

### 3. CRUD Workflow Tests

End-to-end create → read → delete flow against the API.

```typescript
test("Create, read, and delete an owner", async ({ request }) => {
  const createResp = await request.post("/api/owners", {
    data: {
      firstName: "Playwright",
      lastName: "TestOwner",
      address: "123 Test St",
      city: "TestCity",
      telephone: "1234567890",
    },
  });
  expect(createResp.status()).toBe(201);
  const created = await createResp.json();
  // ... read and delete ...
});
```

## Writing New Tests

### Adding a Screenshot Test

1. Tag the test with `@screenshot` in the test name
2. Use `page.screenshot({ path: 'e2e/screenshots/your-name.png', fullPage: true })`
3. Run with `npm run playwright:screenshots`

### Adding an API Test

```typescript
test("GET /api/your-endpoint returns 200", async ({ request }) => {
  const response = await request.get("/api/your-endpoint");
  expect(response.status()).toBe(200);
});
```

### Configuration

- **Base URL**: `http://localhost:9966/petclinic` (set in `playwright.config.ts`)
- **Browser**: Chromium only (lightweight, CI-friendly)
- **Timeout**: 30 seconds per test
- **Screenshots**: Captured on failure automatically; manual via `page.screenshot()`
- **Trace**: Recorded on first retry for debugging

## Integration with CI

```bash
# In CI pipeline (headless by default):
npm ci
npx playwright install chromium --with-deps
./mvnw spring-boot:run &
sleep 15  # wait for app startup
npm run playwright:test
```

## Troubleshooting

| Problem                                        | Solution                                                            |
| ---------------------------------------------- | ------------------------------------------------------------------- |
| `browserType.launch: Executable doesn't exist` | Run `npx playwright install chromium`                               |
| `Connection refused on localhost:9966`         | Start the app first: `./mvnw spring-boot:run`                       |
| Tests timeout on Swagger UI                    | Increase timeout in config or check if springdoc is enabled         |
| Screenshots are blank                          | Ensure the page has loaded; add `waitForSelector` before screenshot |

## DO NOT

- Run Playwright tests without the Spring Boot app running
- Commit screenshots to git (they're in `.gitignore`)
- Use Playwright for unit testing — use JUnit for that
- Install all browsers — Chromium is sufficient
