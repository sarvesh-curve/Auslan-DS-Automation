# Auslan DS — Playwright TypeScript

Automation suite for Auslan Digital Services (canary): 20 tests across 13 spec files.

## Coverage

| Area | Spec | Tests |
|------|------|-------|
| Auth | `login.spec.ts` | Login with valid credentials |
| Bookings | `ndis-booking`, `over65-booking`, `org-representative-booking`, `recurring-booking` | Create bookings by client type, recurring with linked bookings |
| Booking actions | `duplicate-ndis-booking`, `cancel-booking`, `update-appointment-date`, `assign-interpreter` | Duplicate, cancel (No Charge), change date, assign interpreter |
| Signup | `signup-ndis-client`, `signup-over65-client`, `signup-organisation` | Register each account type |
| Interpreter invite | `dcd-224-invited-interpreter-recommended.spec.ts` | 8 tests, see below |

### Interpreter invite: invited interpreters with Recommended ON

| Test | Checks |
|------|--------|
| 1 | Invited interpreter stays listed with Recommended ON |
| 2 | Several invited interpreters all stay listed |
| 3 | Still listed after navigating away and back |
| 4 | Still listed with Recommended OFF |
| 5 | Still listed after toggling OFF then ON |
| 6 | Search returns the invited interpreter, not unrelated ones |
| 7 | Bulk invite keeps every selected interpreter listed |
| 8 | Rapid toggling (8 clicks) leaves no missing or duplicate rows |

Each group creates a fresh booking, so it does not depend on leftover data.

Tests create real bookings, invitations and signups on the target environment.

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
