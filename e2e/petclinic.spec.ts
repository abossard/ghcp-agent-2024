import { expect, test } from '@playwright/test';

// === Web UI Tests ===

test.describe('PetClinic Web UI - Home', () => {

  test('Welcome page loads @screenshot', async ({ page }) => {
    await page.goto('/');
    await expect(page.locator('h1')).toContainText('Welcome');
    await expect(page.locator('.nav-brand')).toBeVisible();
    await page.screenshot({ path: 'e2e/screenshots/welcome.png', fullPage: true });
  });

  test('Navigation links are present @screenshot', async ({ page }) => {
    await page.goto('/');
    await expect(page.locator('.nav-links a')).toHaveCount(5);
    await expect(page.locator('.nav-links')).toContainText('Find Owners');
    await expect(page.locator('.nav-links')).toContainText('Veterinarians');
    await page.screenshot({ path: 'e2e/screenshots/navigation.png', fullPage: true });
  });
});

test.describe('PetClinic Web UI - Owners', () => {

  test('Find Owners page loads @screenshot', async ({ page }) => {
    await page.goto('/owners/find');
    await expect(page.locator('h2')).toContainText('Find Owners');
    await expect(page.locator('#lastName')).toBeVisible();
    await page.screenshot({ path: 'e2e/screenshots/find-owners.png', fullPage: true });
  });

  test('Search all owners shows list @screenshot', async ({ page }) => {
    await page.goto('/owners/find');
    await page.click('button[type="submit"]');
    await expect(page.locator('.table')).toBeVisible();
    const rows = page.locator('.table tbody tr');
    await expect(rows.first()).toBeVisible();
    await page.screenshot({ path: 'e2e/screenshots/owners-list.png', fullPage: true });
  });

  test('Owner details page shows pets @screenshot', async ({ page }) => {
    // Owner 1 (George Franklin) is in seed data
    await page.goto('/owners/1');
    await expect(page.locator('h2')).toContainText('Owner Information');
    await expect(page.locator('.table-detail')).toContainText('George');
    await page.screenshot({ path: 'e2e/screenshots/owner-details.png', fullPage: true });
  });

  test('New Owner form renders @screenshot', async ({ page }) => {
    await page.goto('/owners/new');
    await expect(page.locator('h2')).toContainText('New Owner');
    await expect(page.locator('#firstName')).toBeVisible();
    await expect(page.locator('#lastName')).toBeVisible();
    await expect(page.locator('#telephone')).toBeVisible();
    await page.screenshot({ path: 'e2e/screenshots/new-owner-form.png', fullPage: true });
  });

  test('Create owner via web form', async ({ page }) => {
    await page.goto('/owners/new');
    await page.fill('#firstName', 'E2E');
    await page.fill('#lastName', 'TestOwner');
    await page.fill('#address', '456 Test Ave');
    await page.fill('#city', 'TestVille');
    await page.fill('#telephone', '9876543210');
    await page.click('button[type="submit"]');
    // Should redirect to owner details
    await expect(page.locator('h2')).toContainText('Owner Information');
    await expect(page.locator('.table-detail')).toContainText('E2E');
  });
});

test.describe('PetClinic Web UI - Vets', () => {

  test('Vet list page loads @screenshot', async ({ page }) => {
    await page.goto('/vets');
    await expect(page.locator('h2')).toContainText('Veterinarians');
    await expect(page.locator('.table')).toBeVisible();
    const rows = page.locator('.table tbody tr');
    await expect(rows.first()).toBeVisible();
    await page.screenshot({ path: 'e2e/screenshots/vets-list.png', fullPage: true });
  });
});

// === Swagger UI Tests ===

test.describe('PetClinic Swagger UI', () => {

  test('Swagger UI loads successfully @screenshot', async ({ page }) => {
    await page.goto('/swagger-ui/index.html');
    await page.waitForSelector('.swagger-ui', { timeout: 15_000 });

    // Verify title area is visible
    const title = page.locator('.swagger-ui .info .title');
    await expect(title).toBeVisible();

    await page.screenshot({ path: 'e2e/screenshots/swagger-ui.png', fullPage: true });
  });

  test('Swagger UI lists API endpoints @screenshot', async ({ page }) => {
    await page.goto('/swagger-ui/index.html');
    await page.waitForSelector('.swagger-ui .opblock', { timeout: 15_000 });

    // At least one operation block should be present
    const operations = page.locator('.swagger-ui .opblock');
    await expect(operations.first()).toBeVisible();

    await page.screenshot({ path: 'e2e/screenshots/swagger-endpoints.png', fullPage: true });
  });
});

test.describe('PetClinic API - REST Smoke Tests', () => {

  test('GET /api/owners returns 200', async ({ request }) => {
    const response = await request.get('/api/owners');
    expect(response.status()).toBe(200);
    const body = await response.json();
    expect(Array.isArray(body)).toBe(true);
  });

  test('GET /api/vets returns 200', async ({ request }) => {
    const response = await request.get('/api/vets');
    expect(response.status()).toBe(200);
    const body = await response.json();
    expect(Array.isArray(body)).toBe(true);
  });

  test('GET /api/pettypes returns 200', async ({ request }) => {
    const response = await request.get('/api/pettypes');
    expect(response.status()).toBe(200);
    const body = await response.json();
    expect(Array.isArray(body)).toBe(true);
  });

  test('GET /api/specialties returns 200', async ({ request }) => {
    const response = await request.get('/api/specialties');
    expect(response.status()).toBe(200);
  });

  test('OpenAPI spec is accessible @screenshot', async ({ page, request }) => {
    // Verify JSON spec
    const jsonResp = await request.get('/v3/api-docs');
    expect(jsonResp.status()).toBe(200);
    const spec = await jsonResp.json();
    expect(spec.openapi).toBeDefined();

    // Screenshot of the raw JSON rendered in browser
    await page.goto('/v3/api-docs');
    await page.screenshot({ path: 'e2e/screenshots/openapi-json.png', fullPage: true });
  });
});

test.describe('PetClinic API - CRUD Workflow', () => {

  test('Create, read, and delete an owner', async ({ request }) => {
    // Create
    const createResp = await request.post('/api/owners', {
      data: {
        firstName: 'Playwright',
        lastName: 'TestOwner',
        address: '123 Test St',
        city: 'TestCity',
        telephone: '1234567890',
      },
    });
    expect(createResp.status()).toBe(201);
    const created = await createResp.json();
    expect(created.id).toBeDefined();

    // Read
    const getResp = await request.get(`/api/owners/${created.id}`);
    expect(getResp.status()).toBe(200);
    const fetched = await getResp.json();
    expect(fetched.firstName).toBe('Playwright');

    // Delete
    const deleteResp = await request.delete(`/api/owners/${created.id}`);
    expect(deleteResp.status()).toBe(204);
  });
});
