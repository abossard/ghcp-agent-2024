import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  outputDir: './e2e/test-results',
  timeout: 30_000,
  retries: 0,
  use: {
    baseURL: 'http://localhost:9966/petclinic',
    screenshot: 'only-on-failure',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { browserName: 'chromium' },
    },
  ],
  reporter: [['html', { outputFolder: 'e2e/playwright-report' }]],
});
