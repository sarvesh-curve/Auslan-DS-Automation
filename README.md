# Auslan DS Automation

Playwright **TypeScript** automation for Auslan Digital Services (canary).

## Quick start

```bash
cd playwright-ts
cp .env.example .env   # optional
npm install
npx playwright install chromium
npm test
```

See [`playwright-ts/README.md`](playwright-ts/README.md) for coverage, config, and reports.

## CI

CircleCI runs the suite on the `canary` branch (and nightly). Config: [`.circleci/config.yml`](.circleci/config.yml).

## Project layout

```
playwright-ts/   # Playwright TypeScript suite
.circleci/       # CI config
```
