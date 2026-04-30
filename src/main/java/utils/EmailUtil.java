package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtil {
    
    /**
     * Send email with ExtentReport attachment
     * @param reportPath Path to the ExtentReport HTML file
     * @param testSummary Summary of test execution
     */
    public static void sendExtentReport(String reportPath, String testSummary) {
        // Email configuration from config.properties
        String toEmail = ConfigReader.getProperty("email.to", "sarvesh@curvetomorrow.com.au");
        String fromEmail = ConfigReader.getProperty("email.from", "noreply@auslan-automation.com");
        String smtpHost = ConfigReader.getProperty("smtp.host", "smtp.gmail.com");
        String smtpPort = ConfigReader.getProperty("smtp.port", "587");
        String smtpUsername = ConfigReader.getProperty("smtp.username", "");
        String smtpPassword = ConfigReader.getProperty("smtp.password", "");
        
        // Check if email is configured
        if (smtpUsername.isEmpty() || smtpPassword.isEmpty()) {
            System.out.println("\n⚠️  Email not sent: SMTP credentials not configured in config.properties");
            System.out.println("To enable email reports, add the following to config.properties:");
            System.out.println("smtp.username=your-email@gmail.com");
            System.out.println("smtp.password=your-app-password");
            return;
        }
        
        try {
            // Setup mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // Create authenticator
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            };
            
            // Create session
            Session session = Session.getInstance(props, auth);
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("✅ Auslan DS - Test Execution Report - " + new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new java.util.Date()));
            
            // Create multipart message
            Multipart multipart = new MimeMultipart();
            
            // Email body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String emailBody = buildEmailBody(testSummary);
            messageBodyPart.setContent(emailBody, "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);
            
            // Attach ExtentReport
            File reportFile = new File(reportPath);
            if (reportFile.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(reportFile);
                attachmentPart.setFileName(reportFile.getName());
                multipart.addBodyPart(attachmentPart);
            }
            
            // Set content
            message.setContent(multipart);
            
            // Send email
            Transport.send(message);
            
            System.out.println("\n📧 Email sent successfully to: " + toEmail);
            System.out.println("📎 Attached report: " + reportFile.getName());
            
        } catch (Exception e) {
            System.err.println("\n❌ Failed to send email: " + e.getMessage());
            System.err.println("Please check your SMTP configuration in config.properties");
        }
    }
    
    /**
     * Build HTML email body
     */
    private static String buildEmailBody(String testSummary) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".header h1 { margin: 0; font-size: 24px; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".summary { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                ".summary h2 { color: #667eea; margin-top: 0; }" +
                ".info-row { display: flex; justify-content: space-between; margin: 10px 0; padding: 10px; background: #f5f5f5; border-radius: 5px; }" +
                ".label { font-weight: bold; color: #555; }" +
                ".value { color: #333; }" +
                ".footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }" +
                ".attachment { background: #e3f2fd; padding: 15px; border-radius: 5px; margin: 20px 0; text-align: center; }" +
                ".attachment-icon { font-size: 48px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎯 Auslan DS - Test Automation Report</h1>" +
                "<p>Playwright Test Execution Results</p>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='summary'>" +
                "<h2>📊 Test Summary</h2>" +
                "<pre style='font-family: Arial; white-space: pre-wrap;'>" + testSummary + "</pre>" +
                "</div>" +
                "<div class='attachment'>" +
                "<div class='attachment-icon'>📎</div>" +
                "<p><strong>Detailed ExtentReport is attached</strong></p>" +
                "<p>Open the attached HTML file to view the complete test execution report with screenshots and detailed logs.</p>" +
                "</div>" +
                "<div class='info-row'>" +
                "<span class='label'>Environment:</span>" +
                "<span class='value'>Canary</span>" +
                "</div>" +
                "<div class='info-row'>" +
                "<span class='label'>Browser:</span>" +
                "<span class='value'>" + ConfigReader.getProperty("browser", "chromium") + "</span>" +
                "</div>" +
                "<div class='info-row'>" +
                "<span class='label'>Execution Time:</span>" +
                "<span class='value'>" + new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new java.util.Date()) + "</span>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email from Auslan DS Test Automation Framework</p>" +
                "<p>Please do not reply to this email</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
