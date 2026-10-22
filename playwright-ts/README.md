# Auslan DS — Playwright TypeScript

Automation suite for Auslan Digital Services (canary): 20 tests across 13 spec files.

## Coverage

| Area | Spec | Tests |
|------|------|-------|
| Auth | `login.spec.ts` | Login with valid credentials |
| Bookings | `ndis-booking`, `over65-booking`, `org-representative-booking`, `recurring-booking` | Create bookings by client type, recurring with linked bookings |
| Booking actions | `duplicate-ndis-booking`, `cancel-booking`, `update-appointment-date`, `assign-interpreter` | Duplicate, cancel (No Charge), change date, assign interpreter |
| Signup | `signup-ndis-client`, `signup-over65-client`, `signup-organisation` | Register each account type |
| Interpreter invite (DCD-224) | `dcd-224-invited-interpreter-recommended.spec.ts` | 8 tests, see below |

### DCD-224: invited interpreters with Recommended ON

Covers TC01–06, TC09 and TC13 from `DCD-224-Testcases.xlsx`:

| Test | Checks |
|------|--------|
| TC01 | Invited interpreter stays listed with Recommended ON |
| TC02 | Several invited interpreters all stay listed |
| TC03 | Still listed after navigating away and back |
| TC04 | Still listed with Recommended OFF |
| TC05 | Still listed after toggling OFF then ON |
| TC06 | Search returns the invited interpreter, not unrelated ones |
| TC09 | Bulk invite keeps every selected interpreter listed |
| TC13 | Rapid toggling (8 clicks) leaves no missing or duplicate rows |

Each group creates a fresh booking, so it does not depend on leftover data. TC01–06 and TC13 run in order on one booking; TC09 uses its own.

Not automated (by design): ranking cases for DCD-239/240/241 (they need specific interpreter data), and the DCD-238 Scoring Weights cases (only available to specific accounts, not the test user).

## Test data (canary)

| Item | Value |
|------|-------|
| Login | Admin test user from `TEST_EMAIL` / `TEST_PASSWORD` (never committed) |
| Client | `Sarvesh newndis` (NDIS) |
| Interpreters used by DCD-224 | `Ali Interpreter`, `Nikhil Interpreter`, `Interpreter Chaudhari` |
| Interpreter used by assign test | `Sarvesh Interpreter` |

Tests create real bookings, invitations and signups on canary. If the interpreter names above change, the DCD-224 and assign tests will fail.

## Setup

```bash
cd playwright-ts
cp .env.example .env   # then fill in TEST_EMAIL and TEST_PASSWORD
npm install
npx playwright install chromium
```

## Run

```bash
npm test                                   # whole suite, headless
npm run test:headed                        # headed browser
npx playwright test tests/login.spec.ts    # one spec
npm run report                             # open last HTML report
```

## Config

| Variable | Required | Default |
|----------|----------|---------|
| `TEST_EMAIL` / `TEST_PASSWORD` | Yes | None. The suite fails fast if missing |
| `APP_URL` | No | Canary logout URL |
| `SIGNUP_PASSWORD` | No | `TEST_PASSWORD` (password for accounts the signup specs create) |
| `TEST_PHONE` | No | `0400000000` (placeholder mobile for signup/contact forms) |
| `HEADLESS` | No | `true` (set `false` for headed) |

Generated signup emails use the `TEST_EMAIL` domain with a timestamp suffix.

The suite runs with one worker, a 180s test timeout, and one retry locally (two on CI). Failures keep a screenshot, video and trace in `test-results/`.

## Project layout

```
tests/                     # *.spec.ts files
src/
  config/env.ts            # env + defaults
  constants.ts             # URLs, test user, messages, waits
  pages/                   # page objects (login, dashboard, booking, booking-details,
                           #   interpreter-invite, signup)
  utils/
    test-data.ts           # generated dates, emails, names
    booking-flow.ts        # create a booking, open the newest one
```

## Notes

- Open bookings from the dashboard (Global DSQ service). Opening a booking URL directly resets the state filter to NSW and the Recommended list comes back empty.
- Inviting an interpreter is staged by **Invite** and only sent when the booking is **Save**d.
- `recurring-booking.spec.ts` has been seen to fail once and pass on retry (linked-bookings list timing).

## CI

CircleCI uses `mcr.microsoft.com/playwright:v1.64.1-noble` (keep it in step with `@playwright/test` in `package-lock.json`), runs `npm ci` + `npx playwright test` on the `canary` branch and nightly. See [`.circleci/config.yml`](../.circleci/config.yml).

Set `TEST_EMAIL` and `TEST_PASSWORD` as CircleCI project environment variables. Failure traces record typed values, so keep CircleCI artifacts private.
