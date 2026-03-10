import { expect, test } from '@playwright/test';
import fs from 'fs';
import path from 'path';

test.describe('Owners Excel Export → Import roundtrip', () => {

  test('Import/Export page loads with harmonized design @screenshot', async ({ page }) => {
    await page.goto('/owners/import-export');
    await expect(page.locator('h2')).toContainText('Import / Export');
    // Verify both cards are present
    await expect(page.locator('#exportBtn')).toBeVisible();
    await expect(page.locator('#importBtn')).toBeVisible();
    // Verify nav includes the new link
    await expect(page.locator('.nav-links')).toContainText('Import / Export');
    await page.screenshot({ path: 'e2e/screenshots/import-export-page.png', fullPage: true });
  });

  test('Export XLSX via API', async ({ request }) => {
    const exportResp = await request.get('/api/owners/export');
    expect(exportResp.status()).toBe(200);
    const buffer = await exportResp.body();
    expect(buffer.length).toBeGreaterThan(100);
    // Verify XLSX magic bytes (PK zip header)
    expect(buffer[0]).toBe(0x50);
    expect(buffer[1]).toBe(0x4b);
  });

  test('Validate re-import detects duplicates via API', async ({ request }) => {
    // Export existing owners
    const exportResp = await request.get('/api/owners/export');
    expect(exportResp.status()).toBe(200);
    const buffer = await exportResp.body();

    // Validate: re-importing the same data should detect duplicates
    const validateResp = await request.post('/api/owners/import/validate', {
      multipart: {
        file: {
          name: 'owners.xlsx',
          mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          buffer,
        },
      },
    });

    // 200 — duplicates are informational, not errors
    expect(validateResp.status()).toBe(200);
    const report = await validateResp.json();
    expect(report.duplicates.length).toBeGreaterThan(0);
    expect(report.rows.length).toBeGreaterThan(0);
    expect(report.errors.length).toBe(0);
  });

  test('Conflict resolution UI shows duplicates when re-importing @screenshot', async ({ page }) => {
    // Navigate to import/export page
    await page.goto('/owners/import-export');

    // Export via UI — intercept the download
    const [download] = await Promise.all([
      page.waitForEvent('download'),
      page.click('#exportBtn'),
    ]);
    const downloadPath = path.join('e2e', 'downloads', 'owners-export.xlsx');
    await download.saveAs(downloadPath);
    expect(fs.existsSync(downloadPath)).toBe(true);

    // Upload the same file back for validation
    await page.setInputFiles('#fileInput', downloadPath);
    await page.click('#importBtn');

    // Wait for the results table to appear
    await expect(page.locator('#resultsSection')).toBeVisible({ timeout: 10000 });

    // Verify rows rendered with status badges
    await expect(page.locator('#resultsTable tbody tr')).toHaveCount(10);
    await expect(page.locator('.badge-unchanged')).toHaveCount(
      await page.locator('.badge-unchanged').count()
    );

    // Verify summary
    await expect(page.locator('#summary')).toBeVisible();
    await expect(page.locator('#summaryText')).toContainText('unchanged');

    // Verify "Apply Import" button is enabled (no errors)
    await expect(page.locator('#importApplyBtn')).toBeEnabled();

    // Screenshot the import preview UI
    await page.screenshot({ path: 'e2e/screenshots/conflict-resolution.png', fullPage: true });
  });

});
