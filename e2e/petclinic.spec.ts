import { expect, test } from '@playwright/test';

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
