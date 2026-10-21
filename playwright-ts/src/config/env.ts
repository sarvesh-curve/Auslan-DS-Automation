import dotenv from 'dotenv';
import path from 'path';
import {
  APP_URL as DEFAULT_APP_URL,
  TEST_EMAIL as DEFAULT_EMAIL,
  TEST_PASSWORD as DEFAULT_PASSWORD,
} from '../constants';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

export const env = {
  appUrl: process.env.APP_URL ?? DEFAULT_APP_URL,
  testEmail: process.env.TEST_EMAIL ?? DEFAULT_EMAIL,
  testPassword: process.env.TEST_PASSWORD ?? DEFAULT_PASSWORD,
  headless: process.env.HEADLESS !== 'false',
};
