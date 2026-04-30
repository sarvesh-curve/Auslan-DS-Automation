package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

    /**
     * Generate timestamp in ddMMHHmmss format
     */
    public static String generateTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    /**
     * Generate date of birth for over 65 years old
     */
    public static String generateDOBForOver65() {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.minusYears(65);   // youngest allowed
        LocalDate minDate = today.minusYears(100);  // oldest allowed

        long randomDay = ThreadLocalRandom.current()
                .nextLong(minDate.toEpochDay(), maxDate.toEpochDay());

        LocalDate randomDOB = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return randomDOB.format(formatter);
    }

    /**
     * Generate unique email
     */
    public static String generateEmail() {
        return "sarvesh" + generateTimestamp() + "@curvetomorrow.com.au";
    }

    /**
     * Generate unique first name
     */
    public static String generateFirstName(String prefix) {
        return prefix + generateTimestamp();
    }

    /**
     * Generate random phone number
     */
    public static String generatePhoneNumber() {
        return "0488883690";
    }

    /**
     * Generate future date by adding specified months
     * @param monthsToAdd Number of months to add to current date
     * @return Date string in dd/MM/yyyy format
     */
    public static String generateFutureDate(int monthsToAdd) {
        LocalDate futureDate = LocalDate.now().plusMonths(monthsToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return futureDate.format(formatter);
    }

    /**
     * Generate date 2 months in the future
     * @return Date string in dd/MM/yyyy format (e.g., "28/06/2026")
     */
    public static String generateTwoMonthsFutureDate() {
        return generateFutureDate(2);
    }

    /**
     * Generate future date by adding specified months and days
     * @param monthsToAdd Number of months to add to current date
     * @param daysToAdd Number of days to add after adding months
     * @return Date string in dd/MM/yyyy format
     */
    public static String generateFutureDate(int monthsToAdd, int daysToAdd) {
        LocalDate futureDate = LocalDate.now().plusMonths(monthsToAdd).plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return futureDate.format(formatter);
    }

    /**
     * Generate date 2 months and 15 days in the future
     * @return Date string in dd/MM/yyyy format (e.g., "15/07/2026")
     */
    public static String generateTwoMonthsFifteenDaysFutureDate() {
        return generateFutureDate(2, 15);
    }
}
