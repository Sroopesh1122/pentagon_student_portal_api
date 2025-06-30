package com.pentagon.app.utils;


import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

@Component
public class HtmlTemplates {

	
	public String getHtmlContent(String name, String email,String password)
	{
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>"
				+ name + "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>"
				+ "<ul>" + "<li><strong>Email:</strong> " + email + "</li>"
				+ "<li><strong>Temporary Password:</strong> " +password  + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}
	
	public String getLoginEmailHtmlContent(String name, String email,String password)
	{
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>"
				+ name + "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>"
				+ "<ul>" + "<li><strong>Email:</strong> " + email + "</li>"
				+ "<li><strong>Temporary Password:</strong> " +password  + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}
	
	
	public  String getAccountCreatedEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"en\">\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\" />\n" +
               "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
               "  <title>Pentagon Space - Account Created</title>\n" +
               "</head>\n" +
               "<body style=\"margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;\">\n" +
               "  <div style=\"max-width:410px; margin:40px auto; background:#fff; border-radius:12px; " +
               "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px 28px 24px 28px;\">\n" +
               "    <div style=\"font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;\">Pentagon Space</div>\n" +
               "    <div style=\"text-align:center; color:#222; font-size:1.09rem; font-weight:500; margin-bottom:2rem;\">Account Successfully Created</div>\n" +
               "    <p style=\"font-size:1.08rem; color:#263238; font-weight:600; margin-bottom:0.6rem; margin-top:0;\">Hello " + name + ",</p>\n" +
               "    <p style=\"margin-bottom:1.3rem; color:#525f7f; font-size:0.8rem; line-height:1.7; margin-top:0;\">\n" +
               "      We are pleased to inform you that your account has been successfully created.<br>\n" +
               "      To ensure the security of your account, please use the link below to set your password. Once updated, you can log in and start using our portal.\n" +
               "    </p>\n" +
               "    <div style=\"text-align:center; margin: 24px 0;\">\n" +
               "      <a href=\"" + link + "\" style=\"" +
               "display:inline-block; background:#ec003f; color:#fff; border-radius:7px; text-decoration:none; " +
               "font-weight:600; font-size:1.07rem; padding:11px 34px; box-shadow:0 2px 8px rgba(31,180,157,0.08);\">" +
               "Set Your Password</a>\n" +
               "    </div>\n" +
               "    <div style=\"width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;\">\n" +
               "      If you have any questions, feel free to contact our support team.<br>\n" +
               "      <strong style=\"color:#ec003f;\">Pentagon Space Team</strong>\n" +
               "    </div>\n" +
               "  </div>\n" +
               "</body>\n" +
               "</html>";
    }
	
	
	public String getForgotPasswordEmail(String name, String link) {
	    return "<!DOCTYPE html>\n" +
	           "<html lang=\"en\">\n" +
	           "<head>\n" +
	           "  <meta charset=\"UTF-8\" />\n" +
	           "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
	           "  <title>Pentagon Space - Password Reset Request</title>\n" +
	           "</head>\n" +
	           "<body style=\"margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;\">\n" +
	           "  <div style=\"max-width:410px; margin:40px auto; background:#fff; border-radius:12px; " +
	           "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px 28px 24px 28px;\">\n" +
	           "    <div style=\"font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;\">Pentagon Space</div>\n" +
	           "    <div style=\"text-align:center; color:#222; font-size:1.09rem; font-weight:500; margin-bottom:2rem;\">Password Reset Requested</div>\n" +
	           "    <p style=\"font-size:1.08rem; color:#263238; font-weight:600; margin-bottom:0.6rem; margin-top:0;\">Hello " + name + ",</p>\n" +
	           "    <p style=\"margin-bottom:1.3rem; color:#525f7f; font-size:0.8rem; line-height:1.7; margin-top:0;\">\n" +
	           "      We received a request to reset your password for your Pentagon Space account.<br>\n" +
	           "      If you initiated this request, you can reset your password using the link below.<br>\n" +
	           "      <strong>This link is valid for 15 minutes.</strong>\n" +
	           "    </p>\n" +
	           "    <div style=\"text-align:center; margin: 24px 0;\">\n" +
	           "      <a href=\"" + link + "\" style=\"" +
	           "display:inline-block; background:#ec003f; color:#fff; border-radius:7px; text-decoration:none; " +
	           "font-weight:600; font-size:1.07rem; padding:11px 34px; box-shadow:0 2px 8px rgba(31,180,157,0.08);\">" +
	           "Reset Your Password</a>\n" +
	           "    </div>\n" +
	           "    <p style=\"margin-top:0; font-size:0.75rem; color:#888; text-align:center;\">\n" +
	           "      If you did not request this, you can safely ignore this email.\n" +
	           "    </p>\n" +
	           "    <div style=\"width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;\">\n" +
	           "      For assistance, please contact our support team.<br>\n" +
	           "      <strong style=\"color:#ec003f;\">Pentagon Space Team</strong>\n" +
	           "    </div>\n" +
	           "  </div>\n" +
	           "</body>\n" +
	           "</html>";
	}

	public String getStudentJdAppliedEmail(
    String candidateName,
    String role,
    String companyName,
    String salary,
    String applicationId,
    String applicationLink
) {
    String emailContent = ""
        + "<!DOCTYPE html>"
        + "<html lang=\"en\">"
        + "<head>"
        + "  <meta charset=\"UTF-8\" />"
        + "  <title>Application Confirmation</title>"
        + "</head>"
        + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">"
        + "  <div style=\"max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden;\">"
        + "    <div style=\"background-color: #ec003f; color: #ffffff; padding: 20px; text-align: center;\">"
        + "      <h1 style=\"margin: 0; font-size: 24px;\">Your JD Application Has Been Submitted</h1>"
        + "    </div>"
        + "    <div style=\"padding: 20px;\">"
        + "      <p>Dear <strong>" + candidateName + "</strong>,</p>"
        + "      <p>"
        + "        Thank you for applying for the Job Description (JD) for the role of "
        + "        <strong>" + role + "</strong> at <strong>" + companyName + "</strong> through <strong>Pentagon Space</strong>."
        + "      </p>"
        + "      <p>Below are the details of your application:</p>"
        + "      <ul>"
        + "        <li><strong>Role:</strong> " + role + "</li>"
        + "        <li><strong>Company:</strong> " + companyName + "</li>"
        + "        <li><strong>Salary Package:</strong> " + salary + "</li>"
        + "        <li><strong>Application ID:</strong> " + applicationId + "</li>"
        + "      </ul>"
        + "      <p>You can view your application status at any time using the link below:</p>"
        + "      <p style=\"text-align: center;\">"
        + "        <a href=\"" + applicationLink + "\" "
        + "          style=\""
        + "            background-color: #ec003f;"
        + "            color: #ffffff;"
        + "            padding: 12px 24px;"
        + "            text-decoration: none;"
        + "            border-radius: 4px;"
        + "            display: inline-block;"
        + "          \""
        + "        >View Application</a>"
        + "      </p>"
        + "      <p>If you have any questions, please reach out to your placement coordinator at Pentagon Space.</p>"
        + "      <p style=\"margin-top: 30px;\">"
        + "        Best regards, <br />"
        + "        <strong>Pentagon Space Placement Team</strong>"
        + "      </p>"
        + "    </div>"
        + "    <div style=\""
        + "      background-color: #f3f4f6;"
        + "      color: #6b7280;"
        + "      padding: 10px;"
        + "      text-align: center;"
        + "      font-size: 12px;"
        + "    \">"
        + "      &copy; " + java.time.LocalDateTime.now().getYear() + " Pentagon Space. All rights reserved."
        + "    </div>"
        + "  </div>"
        + "</body>"
        + "</html>";

    return emailContent;
}


}
