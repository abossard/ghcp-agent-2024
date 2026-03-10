import { expect, test } from '@playwright/test';

test.describe('Owners Excel Export → Import roundtrip', () => {

  test('Export XLSX then re-import it without errors', async ({ request }) => {
    // Export: download XLSX bytes from API
    const exportResp = await request.get('/api/owners/export');
    expect(exportResp.status()).toBe(200);
    const buffer = await exportResp.body();

    // Import: send the same XLSX back as multipart/form-data
    const importResp = await request.post('/api/owners/import', {
      multipart: [
        {
          name: 'file',
          buffer,
          fileName: 'owners.xlsx',
          contentType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        },
      ],
    });

    // Success = 204 No Content (matches controller behavior)
    expect(importResp.status()).toBe(204);
  });

});
