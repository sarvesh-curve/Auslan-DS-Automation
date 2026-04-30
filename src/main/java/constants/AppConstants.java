package constants;

public class AppConstants {
    // Application URLs
    public static final String APP_URL = "http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout";
    
    // Test Data
    public static final String TEST_EMAIL = "sarvesh@curvetomorrow.com.au";
    public static final String TEST_PASSWORD = "Curve@2025";
    public static final String CLIENT_NAME = "Sarvesh newndis";
    public static final String ADDRESS = "2 Victory Road, Clarinda VIC, Australia";
    public static final String ADDRESS_SUGGESTION = "Clarinda VIC";
    public static final String PHONE_NUMBER = "0488883690";
    
    // Success Messages
    public static final String BOOKING_CREATED_MSG = "The Booking has been created.";
    public static final String ACCOUNT_CREATED_MSG = "Congratulations. Your account has been created.";
    public static final String ORG_REGISTERED_MSG = "Thank you for registering with Deaf Connect. We value your business. We will verify your details and be in contact shortly.";
    
    // Booking Status
    public static final String STATUS_REQUESTED = "Requested";
    public static final String STATUS_CANCELLED_NO_CHARGE = "Cancelled No Charge";
    
    // Wait Times
    public static final int SHORT_WAIT = 1000;
    public static final int MEDIUM_WAIT = 2000;
    public static final int LONG_WAIT = 5000;
    
    // Max Attempts
    public static final int MAX_NEXT_ATTEMPTS = 6;
    public static final int MAX_APPOINTMENT_PAGE_ATTEMPTS = 8;
}
