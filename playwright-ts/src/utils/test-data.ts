import { env } from '../config/env';

function pad(n: number): string {
  return String(n).padStart(2, '0');
}

/** Timestamp in ddMMHHmmss format (matches Java TestDataGenerator). */
export function generateTimestamp(): string {
  const now = new Date();
  return (
    pad(now.getDate()) +
    pad(now.getMonth() + 1) +
    pad(now.getHours()) +
    pad(now.getMinutes()) +
    pad(now.getSeconds())
  );
}

/** Unique email on the test user's domain, e.g. user0110093012@example.com. */
export function generateEmail(): string {
  const [local, domain] = env.testEmail.split('@');
  return `${local}${generateTimestamp()}@${domain}`;
}

export function generateFirstName(prefix: string): string {
  return `${prefix}${generateTimestamp()}`;
}

/** TEST_PHONE prefix (first 4 digits) plus 6 random digits, so signups don't collide. */
export function generatePhoneNumber(): string {
  const suffix = String(Math.floor(Math.random() * 1_000_000)).padStart(6, '0');
  return env.testPhone.slice(0, 4) + suffix;
}

function formatDate(date: Date): string {
  return `${pad(date.getDate())}/${pad(date.getMonth() + 1)}/${date.getFullYear()}`;
}

export function generateFutureDate(monthsToAdd: number, daysToAdd = 0): string {
  const date = new Date();
  date.setMonth(date.getMonth() + monthsToAdd);
  date.setDate(date.getDate() + daysToAdd);
  return formatDate(date);
}

export function generateTwoMonthsFutureDate(): string {
  return generateFutureDate(2);
}

export function generateTwoMonthsFifteenDaysFutureDate(): string {
  return generateFutureDate(2, 15);
}

/** Random DOB for someone aged 65–100 (dd/MM/yyyy). */
export function generateDOBForOver65(): string {
  const today = new Date();
  const max = new Date(today);
  max.setFullYear(max.getFullYear() - 65);
  const min = new Date(today);
  min.setFullYear(min.getFullYear() - 100);
  const t =
    min.getTime() + Math.random() * (max.getTime() - min.getTime());
  return formatDate(new Date(t));
}
