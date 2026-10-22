import dotenv from 'dotenv';
import path from 'path';
import { APP_URL as DEFAULT_APP_URL } from '../constants';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

/** Credentials are never committed: read them from .env locally or CI env vars. */
function required(name: string): string {
  const value = process.env[name];
  if (!value) {
    throw new Error(`Missing required env var ${name}. Copy .env.example to .env and fill it in.`);
  }
  return value;
}

const testEmail = required('TEST_EMAIL');
const testPassword = required('TEST_PASSWORD');

export const env = {
  appUrl: process.env.APP_URL ?? DEFAULT_APP_URL,
  testEmail,
  testPassword,
  /** Password given to accounts created by the signup specs. */
  signupPassword: process.env.SIGNUP_PASSWORD ?? testPassword,
  /** Placeholder mobile for signup and contact forms. */
  testPhone: process.env.TEST_PHONE ?? '0400000000',
  headless: process.env.HEADLESS !== 'false',
};
