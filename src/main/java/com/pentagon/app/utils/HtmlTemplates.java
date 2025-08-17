package com.pentagon.app.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class HtmlTemplates {

	public String getHtmlContent(String name, String email, String password) {
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>" + name
				+ "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>" + "<ul>"
				+ "<li><strong>Email:</strong> " + email + "</li>" + "<li><strong>Temporary Password:</strong> "
				+ password + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}

	public String getLoginEmailHtmlContent(String name, String email, String password) {
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>" + name
				+ "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>" + "<ul>"
				+ "<li><strong>Email:</strong> " + email + "</li>" + "<li><strong>Temporary Password:</strong> "
				+ password + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}

	public String getAnnouncementEmail(String title, String description, String announcedBy, String link) {
		StringBuilder emailBody = new StringBuilder();

		emailBody.append("<!DOCTYPE html>\n").append("<html lang=\"en\">\n").append("<head>\n")
				.append("  <meta charset=\"UTF-8\">\n")
				.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
				.append("  <title>New Announcement - Pentagon Space</title>\n").append("</head>\n")
				.append("<body style=\"margin: 0; padding: 20px; background-color: #f5f7fa; font-family: Arial, sans-serif;\">\n")
				.append("  <div style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; ")
				.append("box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); padding: 30px; border: 1px solid #e0e6ed;\">\n")

				.append("    <h2 style=\"color: #ec003f; text-align: center; margin-bottom: 20px;\">Pentagon Space</h2>\n")
				.append("    <h3 style=\"color: #222; font-size: 1.2rem; margin-bottom: 10px; text-align: center;\">📢 New Announcement</h3>\n")

				.append("    <p style=\"font-size: 0.95rem; color: #333333; line-height: 1.6;\"><strong>Title:</strong> "
						+ title + "</p>\n")
				.append("    <p style=\"font-size: 0.95rem; color: #555555; line-height: 1.6;\"><strong>Description:</strong><br>"
						+ description + "</p>\n");

		if (link != null && !link.trim().isEmpty()) {
			emailBody.append("    <p style=\"margin-top: 12px;\">\n")
					.append("      <a href=\"" + link
							+ "\" style=\"display: inline-block; background-color: #ec003f; color: white; ")
					.append("text-decoration: none; padding: 10px 20px; border-radius: 6px; font-size: 0.9rem;\">View More</a>\n")
					.append("    </p>\n");
		}

		emailBody.append(
				"    <p style=\"font-size: 0.85rem; color: #888888; margin-top: 30px; border-top: 1px solid #eee; padding-top: 16px; text-align: center;\">\n")
				.append("      Announced by <strong style=\"color: #ec003f;\">" + announcedBy + "</strong><br>\n")
				.append("      Pentagon Space Team\n").append("    </p>\n").append("  </div>\n").append("</body>\n")
				.append("</html>");

		return emailBody.toString();
	}

	public String getManagerChangeNotificationEmail(String executiveName, String executiveEmail, String managerName,
			String managerEmail) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\" />\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
				+ "  <title>Manager Change Notification - Pentagon Space</title>\n" + "</head>\n"
				+ "<body style=\"margin: 0; padding: 20px; background-color: #f5f7fa; font-family: Arial, sans-serif;\">\n"
				+ "  <div style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; "
				+ "box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); padding: 30px; border: 1px solid #e0e6ed;\">\n"
				+ "    <div style=\"text-align: center; margin-bottom: 24px;\">\n"
				+ "      <h2 style=\"color: #ec003f; margin: 0; font-size: 1.8rem;\">Pentagon Space</h2>\n"
				+ "      <p style=\"color: #333; font-size: 1.1rem; margin-top: 8px; font-weight: 600;\">Manager Change Notification</p>\n"
				+ "    </div>\n" + "    <p style=\"font-size: 1rem; color: #333333; margin-bottom: 16px;\">\n"
				+ "      Dear " + executiveName + " and " + managerName + ",\n" + "    </p>\n"
				+ "    <p style=\"font-size: 0.95rem; color: #555555; line-height: 1.6; margin-bottom: 20px;\">\n"
				+ "      We are writing to inform you of a recent change in reporting structure. Effective immediately, <strong>"
				+ executiveName + "</strong> will now be reporting directly to <strong>" + managerName + "</strong>.\n"
				+ "      <br><br>\n"
				+ "      We kindly request you to acknowledge and accommodate this change. Should you have any questions or need additional clarification, please do not hesitate to contact the Pentagon Space team.\n"
				+ "    </p>\n"
				+ "    <p style=\"font-size: 0.9rem; color: #777777; border-top: 1px solid #e6e6e6; padding-top: 20px; text-align: center;\">\n"
				+ "      Thank you for your cooperation.<br>\n"
				+ "      <strong style=\"color: #ec003f;\">Pentagon Space Team</strong>\n" + "    </p>\n" + "  </div>\n"
				+ "</body>\n" + "</html>";
	}

	public String getAccountBlockedEmail(String userName) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\">\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "  <title>Account Blocked - Pentagon Space</title>\n" + "</head>\n"
				+ "<body style=\"margin: 0; padding: 20px; background-color: #f5f7fa; font-family: Arial, sans-serif;\">\n"
				+ "  <div style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; "
				+ "box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); padding: 30px; border: 1px solid #e0e6ed;\">\n"
				+ "    <h2 style=\"color: #ec003f; text-align: center; margin-bottom: 20px;\">Pentagon Space</h2>\n"
				+ "    <p style=\"font-size: 1rem; color: #333;\">Dear " + userName + ",</p>\n"
				+ "    <p style=\"font-size: 0.95rem; color: #555; line-height: 1.6;\">\n"
				+ "      We would like to inform you that your Pentagon Space account has been <strong>temporarily blocked</strong>. \n"
				+ "      Access to the platform and its services is currently restricted.\n" + "    </p>\n"
				+ "    <p style=\"font-size: 0.95rem; color: #555;\">\n"
				+ "      If you believe this was a mistake or if you need further clarification, please contact our support team or your administrator.\n"
				+ "    </p>\n"
				+ "    <p style=\"font-size: 0.9rem; color: #777; border-top: 1px solid #eee; padding-top: 20px; text-align: center;\">\n"
				+ "      Thank you,<br><strong style=\"color: #ec003f;\">Pentagon Space Team</strong>\n" + "    </p>\n"
				+ "  </div>\n" + "</body>\n" + "</html>";
	}

	public String getAccountUnblockedEmail(String userName) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\">\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "  <title>Account Unblocked - Pentagon Space</title>\n" + "</head>\n"
				+ "<body style=\"margin: 0; padding: 20px; background-color: #f5f7fa; font-family: Arial, sans-serif;\">\n"
				+ "  <div style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; "
				+ "box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); padding: 30px; border: 1px solid #e0e6ed;\">\n"
				+ "    <h2 style=\"color: #ec003f; text-align: center; margin-bottom: 20px;\">Pentagon Space</h2>\n"
				+ "    <p style=\"font-size: 1rem; color: #333;\">Dear " + userName + ",</p>\n"
				+ "    <p style=\"font-size: 0.95rem; color: #555; line-height: 1.6;\">\n"
				+ "      We are pleased to inform you that your Pentagon Space account has been <strong>unblocked</strong> and is now active again.\n"
				+ "      You can log in and resume using the platform as usual.\n" + "    </p>\n"
				+ "    <p style=\"font-size: 0.95rem; color: #555;\">\n"
				+ "      If you face any issues accessing your account or need assistance, please don't hesitate to contact our support team.\n"
				+ "    </p>\n"
				+ "    <p style=\"font-size: 0.9rem; color: #777; border-top: 1px solid #eee; padding-top: 20px; text-align: center;\">\n"
				+ "      Welcome back!<br><strong style=\"color: #ec003f;\">Pentagon Space Team</strong>\n"
				+ "    </p>\n" + "  </div>\n" + "</body>\n" + "</html>";
	}

	public String getBulkManagerAssignmentEmail(String managerName, List<String> executiveNames) {
		StringBuilder emailHtml = new StringBuilder();

		emailHtml.append("<!DOCTYPE html>\n");
		emailHtml.append("<html lang=\"en\">\n");
		emailHtml.append("<head>\n");
		emailHtml.append("  <meta charset=\"UTF-8\">\n");
		emailHtml.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
		emailHtml.append("  <title>Manager Assignment Notification - Pentagon Space</title>\n");
		emailHtml.append("</head>\n");
		emailHtml.append(
				"<body style=\"margin: 0; padding: 20px; background-color: #f5f7fa; font-family: Arial, sans-serif;\">\n");
		emailHtml.append(
				"  <div style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; ");
		emailHtml.append("box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); padding: 30px; border: 1px solid #e0e6ed;\">\n");
		emailHtml.append("    <div style=\"text-align: center; margin-bottom: 24px;\">\n");
		emailHtml.append("      <h2 style=\"color: #ec003f; margin: 0; font-size: 1.8rem;\">Pentagon Space</h2>\n");
		emailHtml.append(
				"      <p style=\"color: #333; font-size: 1.1rem; margin-top: 8px; font-weight: 600;\">Manager Assignment Notification</p>\n");
		emailHtml.append("    </div>\n");
		emailHtml.append("    <p style=\"font-size: 1rem; color: #333333; margin-bottom: 16px;\">\n");
		emailHtml.append("      Dear ").append(managerName).append(",\n");
		emailHtml.append("    </p>\n");
		emailHtml.append(
				"    <p style=\"font-size: 0.95rem; color: #555555; line-height: 1.6; margin-bottom: 20px;\">\n");
		emailHtml.append("      You have been assigned as the reporting manager for the following executives:\n");
		emailHtml.append("    </p>\n");

		emailHtml.append(
				"    <ul style=\"padding-left: 20px; font-size: 0.95rem; color: #333333; margin-bottom: 24px;\">\n");
		for (String executive : executiveNames) {
			emailHtml.append("      <li style=\"margin-bottom: 6px;\">").append(executive).append("</li>\n");
		}
		emailHtml.append("    </ul>\n");

		emailHtml.append("    <p style=\"font-size: 0.95rem; color: #555555; line-height: 1.6;\">\n");
		emailHtml.append(
				"      Please ensure to connect with each executive and facilitate a smooth onboarding under your supervision. If you require any support during this transition, feel free to reach out to the Pentagon Space team.\n");
		emailHtml.append("    </p>\n");

		emailHtml.append(
				"    <p style=\"font-size: 0.9rem; color: #777777; border-top: 1px solid #e6e6e6; padding-top: 20px; text-align: center;\">\n");
		emailHtml.append("      Thank you.<br>\n");
		emailHtml.append("      <strong style=\"color: #ec003f;\">Pentagon Space Team</strong>\n");
		emailHtml.append("    </p>\n");

		emailHtml.append("  </div>\n");
		emailHtml.append("</body>\n");
		emailHtml.append("</html>");

		return emailHtml.toString();
	}

	public String getAccountCreatedEmail(String name, String link) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\" />\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n"
				+ "  <title>Pentagon Space - Account Created</title>\n" + "</head>\n"
				+ "<body style=\"margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;\">\n"
				+ "  <div style=\"max-width:410px; margin:40px auto; background:#fff; border-radius:12px; "
				+ "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px 28px 24px 28px;\">\n"
				+ "    <div style=\"font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;\">Pentagon Space</div>\n"
				+ "    <div style=\"text-align:center; color:#222; font-size:1.09rem; font-weight:500; margin-bottom:2rem;\">Account Successfully Created</div>\n"
				+ "    <p style=\"font-size:1.08rem; color:#263238; font-weight:600; margin-bottom:0.6rem; margin-top:0;\">Hello "
				+ name + ",</p>\n"
				+ "    <p style=\"margin-bottom:1.3rem; color:#525f7f; font-size:0.8rem; line-height:1.7; margin-top:0;\">\n"
				+ "      We are pleased to inform you that your account has been successfully created.<br>\n"
				+ "      To ensure the security of your account, please use the link below to set your password. Once updated, you can log in and start using our portal.\n"
				+ "    </p>\n" + "    <div style=\"text-align:center; margin: 24px 0;\">\n" + "      <a href=\"" + link
				+ "\" style=\""
				+ "display:inline-block; background:#ec003f; color:#fff; border-radius:7px; text-decoration:none; "
				+ "font-weight:600; font-size:1.07rem; padding:11px 34px; box-shadow:0 2px 8px rgba(31,180,157,0.08);\">"
				+ "Set Your Password</a>\n" + "    </div>\n"
				+ "    <div style=\"width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;\">\n"
				+ "      If you have any questions, feel free to contact our support team.<br>\n"
				+ "      <strong style=\"color:#ec003f;\">Pentagon Space Team</strong>\n" + "    </div>\n"
				+ "  </div>\n" + "</body>\n" + "</html>";
	}

	public String getForgotPasswordEmail(String name, String link) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\" />\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n"
				+ "  <title>Pentagon Space - Password Reset Request</title>\n" + "</head>\n"
				+ "<body style=\"margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;\">\n"
				+ "  <div style=\"max-width:410px; margin:40px auto; background:#fff; border-radius:12px; "
				+ "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px 28px 24px 28px;\">\n"
				+ "    <div style=\"font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;\">Pentagon Space</div>\n"
				+ "    <div style=\"text-align:center; color:#222; font-size:1.09rem; font-weight:500; margin-bottom:2rem;\">Password Reset Requested</div>\n"
				+ "    <p style=\"font-size:1.08rem; color:#263238; font-weight:600; margin-bottom:0.6rem; margin-top:0;\">Hello "
				+ name + ",</p>\n"
				+ "    <p style=\"margin-bottom:1.3rem; color:#525f7f; font-size:0.8rem; line-height:1.7; margin-top:0;\">\n"
				+ "      We received a request to reset your password for your Pentagon Space account.<br>\n"
				+ "      If you initiated this request, you can reset your password using the link below.<br>\n"
				+ "      <strong>This link is valid for 15 minutes.</strong>\n" + "    </p>\n"
				+ "    <div style=\"text-align:center; margin: 24px 0;\">\n" + "      <a href=\"" + link + "\" style=\""
				+ "display:inline-block; background:#ec003f; color:#fff; border-radius:7px; text-decoration:none; "
				+ "font-weight:600; font-size:1.07rem; padding:11px 34px; box-shadow:0 2px 8px rgba(31,180,157,0.08);\">"
				+ "Reset Your Password</a>\n" + "    </div>\n"
				+ "    <p style=\"margin-top:0; font-size:0.75rem; color:#888; text-align:center;\">\n"
				+ "      If you did not request this, you can safely ignore this email.\n" + "    </p>\n"
				+ "    <div style=\"width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;\">\n"
				+ "      For assistance, please contact our support team.<br>\n"
				+ "      <strong style=\"color:#ec003f;\">Pentagon Space Team</strong>\n" + "    </div>\n"
				+ "  </div>\n" + "</body>\n" + "</html>";
	}

	public String getPasswordResetSuccessEmail(String name) {
		return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\" />\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
				+ "  <title>Pentagon Space - Password Reset Successful</title>\n" + "</head>\n"
				+ "<body style=\"margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;\">\n"
				+ "  <div style=\"max-width:410px; margin:40px auto; background:#fff; border-radius:12px; "
				+ "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px 28px 24px 28px;\">\n"
				+ "    <div style=\"font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;\">Pentagon Space</div>\n"
				+ "    <div style=\"text-align:center; color:#222; font-size:1.09rem; font-weight:500; margin-bottom:2rem;\">Password Reset Successful</div>\n"
				+ "    <p style=\"font-size:1.08rem; color:#263238; font-weight:600; margin-bottom:0.6rem; margin-top:0;\">Hello "
				+ name + ",</p>\n"
				+ "    <p style=\"margin-bottom:1.3rem; color:#525f7f; font-size:0.8rem; line-height:1.7; margin-top:0;\">\n"
				+ "      This is a confirmation that your password has been successfully reset for your Pentagon Space account.<br>\n"
				+ "      If you did not perform this action, please contact our support team immediately.\n"
				+ "    </p>\n"
				+ "    <div style=\"width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;\">\n"
				+ "      Thank you for using Pentagon Space.<br>\n"
				+ "      <strong style=\"color:#ec003f;\">Pentagon Space Team</strong>\n" + "    </div>\n"
				+ "  </div>\n" + "</body>\n" + "</html>";
	}

	public String getStudentJdAppliedEmail(String candidateName, String role, String companyName, String salary,
			String applicationId, String applicationLink) {
		String emailContent = "" + "<!DOCTYPE html>" + "<html lang=\"en\">" + "<head>" + "  <meta charset=\"UTF-8\" />"
				+ "  <title>Application Confirmation</title>" + "</head>"
				+ "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">"
				+ "  <div style=\"max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden;\">"
				+ "    <div style=\"background-color: #ec003f; color: #ffffff; padding: 20px; text-align: center;\">"
				+ "      <h1 style=\"margin: 0; font-size: 24px;\">Your JD Application Has Been Submitted</h1>"
				+ "    </div>" + "    <div style=\"padding: 20px;\">" + "      <p>Dear <strong>" + candidateName
				+ "</strong>,</p>" + "      <p>"
				+ "        Thank you for applying for the Job Description (JD) for the role of " + "        <strong>"
				+ role + "</strong> at <strong>" + companyName + "</strong> through <strong>Pentagon Space</strong>."
				+ "      </p>" + "      <p>Below are the details of your application:</p>" + "      <ul>"
				+ "        <li><strong>Role:</strong> " + role + "</li>" + "        <li><strong>Company:</strong> "
				+ companyName + "</li>" + "        <li><strong>Salary Package:</strong> " + salary + "</li>"
				+ "        <li><strong>Application ID:</strong> " + applicationId + "</li>" + "      </ul>"
				+ "      <p>You can view your application status at any time using the link below:</p>"
				+ "      <p style=\"text-align: center;\">" + "        <a href=\"" + applicationLink + "\" "
				+ "          style=\"" + "            background-color: #ec003f;" + "            color: #ffffff;"
				+ "            padding: 12px 24px;" + "            text-decoration: none;"
				+ "            border-radius: 4px;" + "            display: inline-block;" + "          \""
				+ "        >View Application</a>" + "      </p>"
				+ "      <p>If you have any questions, please reach out to your placement coordinator at Pentagon Space.</p>"
				+ "      <p style=\"margin-top: 30px;\">" + "        Best regards, <br />"
				+ "        <strong>Pentagon Space Placement Team</strong>" + "      </p>" + "    </div>"
				+ "    <div style=\"" + "      background-color: #f3f4f6;" + "      color: #6b7280;"
				+ "      padding: 10px;" + "      text-align: center;" + "      font-size: 12px;" + "    \">"
				+ "      &copy; " + java.time.LocalDateTime.now().getYear() + " Pentagon Space. All rights reserved."
				+ "    </div>" + "  </div>" + "</body>" + "</html>";

		return emailContent;
	}

	public String getNewJdEmail(String role, String companyLogo, String companyName, String stackRequired,
			String salaryPackage, String applyLink) {
		return "<!DOCTYPE html>" + "<html lang=\"en\">" + "<head>" + "  <meta charset=\"UTF-8\">"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "  <title>New Job Opportunity | Pentagon Space</title>" + "  <style>" +
				"    body { font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; margin: 0; padding: 0; background-color: #f7fafc; line-height: 1.6; }"
				+ "    .container { max-width: 600px; margin: 20px auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); }"
				+ "    .header { background: linear-gradient(135deg, #4f46e5, #7c3aed); color: white; padding: 30px; text-align: center; }"
				+ "    .logo-container { display: flex; justify-content: center; margin: 20px 0; background: white; padding: 15px; border-radius: 8px; }"
				+ "    .company-logo { max-height: 60px; max-width: 180px; object-fit: contain; }"
				+ "    .content { padding: 25px; }"
				+ "    .jd-details { background: #f8fafc; border-radius: 8px; padding: 20px; margin: 20px 0; }"
				+ "    .detail-row { display: flex; margin-bottom: 12px; }"
				+ "    .detail-label { font-weight: 600; color: #4b5563; min-width: 120px; }"
				+ "    .detail-value { color: #1f2937; }" + "    .cta-button { " + "      display: inline-block; "
				+ "      background: #4f46e5; " + "      color: white; " + "      text-decoration: none; "
				+ "      padding: 12px 24px; " + "      border-radius: 6px; " + "      font-weight: 500; "
				+ "      transition: all 0.3s ease; " + "    }"
				+ "    .cta-button:hover { background: #3f37c5; transform: translateY(-2px); box-shadow: 0 4px 8px rgba(79, 70, 229, 0.3); }"
				+ "    .footer { text-align: center; padding: 15px; background: #f3f4f6; color: #6b7280; font-size: 12px; }"
				+ "    .tag { " + "      display: inline-block; " + "      background: #e0e7ff; "
				+ "      color: #4f46e5; " + "      padding: 4px 10px; " + "      border-radius: 20px; "
				+ "      font-size: 12px; " + "      margin-right: 8px; " + "      margin-bottom: 8px; "
				+ "      font-weight: 500; " + "    }" + "  </style>" + "</head>" + "<body>"
				+ "  <div class=\"container\">" + "    <div class=\"header\">" + "      <h2>New Career Opportunity</h2>"
				+ "      <p>Presented by Pentagon Space</p>" + "    </div>" + "    <div class=\"content\">"
				+ "      <div class=\"logo-container\">" + "        <img src=\"" + companyLogo + "\" alt=\""
				+ companyName + " Logo\" class=\"company-logo\">" + "      </div>"
				+ "      <h3 style=\"margin-top: 0; color: #1f2937;\">" + role + "</h3>" + "      <div>"
				+ "        <span class=\"tag\">" + stackRequired + "</span>" + "      </div>"
				+ "      <div class=\"jd-details\">" + "        <div class=\"detail-row\">"
				+ "          <span class=\"detail-label\">Company:</span>" + "          <span class=\"detail-value\">"
				+ companyName + "</span>" + "        </div>" + "        <div class=\"detail-row\">"
				+ "          <span class=\"detail-label\">Role:</span>" + "          <span class=\"detail-value\">"
				+ role + "</span>" + "        </div>" + "        <div class=\"detail-row\">"
				+ "          <span class=\"detail-label\">Package:</span>" + "          <span class=\"detail-value\">"
				+ salaryPackage + "</span>" + "        </div>" + "        <div class=\"detail-row\">"
				+ "          <span class=\"detail-label\">Tech Stack:</span>"
				+ "          <span class=\"detail-value\">" + stackRequired + "</span>" + "        </div>"
				+ "      </div>" + "      <div style=\"text-align: center; margin-top: 25px;\">" + "        <a href=\""
				+ applyLink + "\" class=\"cta-button\">Apply Now</a>" + "      </div>" + "    </div>"
				+ "    <div class=\"footer\">" + "      &copy; " + java.time.LocalDateTime.now().getYear()
				+ " Pentagon Space. All rights reserved." + "    </div>" + "  </div>" + "</body>" + "</html>";
	}

	public String generateJDApprovalEmail(String managerName, String executiveName, String executiveEmail,
			String executiveMobile, String role, String companyName, String companyLogoUrl, String approvalLink) {
		String html = "" + "<!DOCTYPE html>" + "<html lang=\"en\">" + "<head>" + "  <meta charset=\"UTF-8\" />"
				+ "  <title>JD Approval Request</title>" + "</head>"
				+ "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9fafb;\">"
				+ "  <div style=\"max-width: 600px; margin: 40px auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; background-color: #ffffff;\">"

				+ "    <div style=\"background-color: #f97316; color: #ffffff; padding: 20px; text-align: center;\">"
				+ "      <h1 style=\"margin: 0; font-size: 22px;\">New JD Posted – Approval Needed</h1>" + "    </div>"

				+ "    <div style=\"padding: 20px;\">" + "      <p>Dear " + managerName + ",</p>"

				+ "      <p>A new Job Description (JD) has been posted and requires your review and approval. Below are the details:</p>"

				+ "      <h3 style=\"margin-bottom: 5px;\">Job Details</h3>" + "      <ul>"
				+ "        <li><strong>Role:</strong> " + role + "</li>" + "        <li><strong>Company:</strong> "
				+ companyName + "</li>" + "      </ul>"

				+ "      <div style=\"margin: 20px 0; text-align: center;\">" + "        <img src=\"" + companyLogoUrl
				+ "\" alt=\"Company Logo\" style=\"max-width: 200px; height: auto;\"/>" + "      </div>"

				+ "      <h3 style=\"margin-bottom: 5px;\">Executive Details</h3>" + "      <ul>"
				+ "        <li><strong>Name:</strong> " + executiveName + "</li>"
				+ "        <li><strong>Email:</strong> " + executiveEmail + "</li>"
				+ "        <li><strong>Mobile:</strong> " + executiveMobile + "</li>" + "      </ul>"

				+ "      <p>Please review and approve this JD using the link below:</p>"

				+ "      <p style=\"text-align: center;\">" + "        <a href=\"" + approvalLink + "\" "
				+ "          style=\"" + "            background-color: #f97316;" + "            color: #ffffff;"
				+ "            padding: 12px 24px;" + "            text-decoration: none;"
				+ "            border-radius: 4px;" + "            display: inline-block;" + "          \""
				+ "        >Review & Approve JD</a>" + "      </p>"

				+ "      <p>If you have any questions, please contact the placement team at Pentagon Space.</p>"

				+ "      <p style=\"margin-top: 30px;\">" + "        Best regards,<br />"
				+ "        <strong>Pentagon Space Placement Team</strong>" + "      </p>" + "    </div>"

				+ "    <div style=\"background-color: #f3f4f6; color: #6b7280; padding: 10px; text-align: center; font-size: 12px;\">"
				+ "      &copy; 2024 Pentagon Space. All rights reserved." + "    </div>"

				+ "  </div>" + "</body>" + "</html>";

		return html;
	}

	public String generateJDApprovedEmail(String executiveName, String role, String companyName, String managerName,
			String companyLogoUrl, String jdLink) {
		return "<!DOCTYPE html>" + "<html lang=\"en\">" + "<head>" + "<meta charset=\"UTF-8\" />"
				+ "<title>JD Approved</title>" + "</head>"
				+ "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">"
				+ "<div style=\"max-width: 1000px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden;\">"
				+

				"<div style=\"background-color: #f97316; color: #ffffff; padding: 20px; text-align: center;\">"
				+ "<h1 style=\"margin: 0; font-size: 24px;\">Job Description Approved</h1>" + "</div>" +

				"<div style=\"padding: 20px;\">" + "<p>Dear <strong>" + executiveName + "</strong>,</p>" +

				"<p>We are pleased to inform you that the Job Description (JD) for the role of " + "<strong>" + role
				+ "</strong> at <strong>" + companyName
				+ "</strong> has been <strong>approved</strong> by the Manager at <strong>Pentagon Space</strong>.</p>"
				+

				"<p>Here are the approved JD details:</p>" +

				"<ul>" + "<li><strong>Role:</strong> " + role + "</li>" + "<li><strong>Company:</strong> " + companyName
				+ "</li>" + "<li><strong>Approved By:</strong> " + managerName + "</li>" + "</ul>" +

				"<p style=\"text-align: center; margin: 20px 0;\">" + "<img src=\"" + companyLogoUrl
				+ "\" alt=\"Company Logo\" style=\"max-width: 120px; height: auto;\" />" + "</p>" +

				"<p style=\"text-align: center;\">" + "<a href=\"" + jdLink + "\" style=\""
				+ "background-color: #f97316;" + "color: #ffffff;" + "padding: 12px 24px;" + "text-decoration: none;"
				+ "border-radius: 4px;" + "display: inline-block;" + "\">View JD</a>" + "</p>" +

				"<p>If you have any questions, please reach out to the Manager or Placement Team at Pentagon Space.</p>"
				+

				"<p style=\"margin-top: 30px;\">Best regards,<br /><strong>Pentagon Space Placement Team</strong></p>"
				+ "</div>" +

				"<div style=\"background-color: #f3f4f6; color: #6b7280; padding: 10px; text-align: center; font-size: 12px;\">"
				+ "&copy; 2024 Pentagon Space. All rights reserved." + "</div>" +

				"</div>" + "</body>" + "</html>";
	}

	public String getShortlistEmailHtmlContent(String companyName, String companyLogo, List<String> students, // List of
																												// "Name,Id,Email"
			String role, String nextRound, LocalDateTime scheduleDate) {
		String formattedDate = scheduleDate
				.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a"));

		// Build the student cards
		StringBuilder studentCards = new StringBuilder();
		for (String student : students) {
			String[] parts = student.split(",");
			String name = parts.length > 0 ? parts[0].trim() : "";
			String id = parts.length > 1 ? parts[1].trim() : "";
			String email = parts.length > 2 ? parts[2].trim() : "";

			studentCards.append(
					"<div style='border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin-bottom: 15px;'>"
							+ "<p style='margin: 0;'><strong>Name:</strong> " + name + "</p>"
							+ "<p style='margin: 0;'><strong>ID:</strong> " + id + "</p>"
							+ "<p style='margin: 0;'><strong>Email:</strong> " + email + "</p>" + "</div>");
		}

		String htmlContent = "<!DOCTYPE html>" + "<html>"
				+ "<body style='font-family: Arial, sans-serif; background-color: #f9fafb; padding: 40px;'>"
				+ "  <div style='max-width: 1000px; margin: auto; background-color: #ffffff; border: 1px solid #e5e7eb;'>"
				+ "    <div style='background-color: #ec003f; padding: 30px; text-align: center;'>" + "      <img src='"
				+ companyLogo + "' alt='" + companyName + " Logo' style='max-width: 150px; height: auto;' />"
				+ "      <h2 style='color: #ffffff; margin: 10px 0 0;'>" + companyName + "</h2>" + "    </div>"
				+ "    <div style='padding: 30px;'>" + "      <h3 style='color: #111827;'>Hi,</h3>"
				+ "      <p style='color: #374151; font-size: 16px;'>"
				+ "        Congratulations! The following students have been shortlisted for the <strong>" + nextRound
				+ "</strong> round for the role of " + "        <strong>" + role + "</strong> at <strong>" + companyName
				+ "</strong>." + "      </p>" + "      <p style='color: #374151; font-size: 16px;'>"
				+ "        <strong>Schedule Date:</strong> " + formattedDate + "      </p>"

				+ studentCards.toString()

				+ "      <p style='margin-top: 20px;'>If you have any questions, please reach out to your placement coordinator at Pentagon Space.</p>"
				+ "      <p style='margin-top: 30px;'>" + "        Best regards, <br />"
				+ "        <strong>Pentagon Space Placement Team</strong>" + "      </p>" + "    </div>"
				+ "    <div style='background-color: #f3f4f6; color: #6b7280; padding: 10px; text-align: center; font-size: 12px;'>"
				+ "      &copy; " + java.time.LocalDateTime.now().getYear() + " " + companyName
				+ ". All rights reserved." + "    </div>" + "  </div>" + "</body>" + "</html>";

		return htmlContent;
	}

	public String getRejectedEmailHtmlContent(String companyName, String companyLogo, List<String> students, // List of
																												// "Name,Id,Email"
			String role) {
		// Build the student cards
		StringBuilder studentCards = new StringBuilder();
		for (String student : students) {
			String[] parts = student.split(",");
			String name = parts.length > 0 ? parts[0].trim() : "";
			String id = parts.length > 1 ? parts[1].trim() : "";
			String email = parts.length > 2 ? parts[2].trim() : "";

			studentCards.append(
					"<div style='border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin-bottom: 15px;'>"
							+ "<p style='margin: 0;'><strong>Name:</strong> " + name + "</p>"
							+ "<p style='margin: 0;'><strong>ID:</strong> " + id + "</p>"
							+ "<p style='margin: 0;'><strong>Email:</strong> " + email + "</p>" + "</div>");
		}

		String htmlContent = "<!DOCTYPE html>" + "<html>"
				+ "<body style='font-family: Arial, sans-serif; background-color: #f9fafb; padding: 40px;'>"
				+ "  <div style='max-width: 600px; margin: auto; background-color: #ffffff; border: 1px solid #e5e7eb;'>"
				+ "    <div style='background-color: #ec003f; padding: 30px; text-align: center;'>" + "      <img src='"
				+ companyLogo + "' alt='" + companyName + " Logo' style='max-width: 150px; height: auto;' />"
				+ "      <h2 style='color: #ffffff; margin: 10px 0 0;'>" + companyName + "</h2>" + "    </div>"
				+ "    <div style='padding: 30px;'>" + "      <h3 style='color: #111827;'>Hi,</h3>"
				+ "      <p style='color: #374151; font-size: 16px;'>"
				+ "        We regret to inform you that the following students were not shortlisted for the role of "
				+ "        <strong>" + role + "</strong> at <strong>" + companyName + "</strong>." + "      </p>"

				+ studentCards.toString()

				+ "      <p style='margin-top: 20px;'>If you have any questions, please reach out to your placement coordinator at "
				+ companyName + ".</p>" + "      <p style='margin-top: 30px;'>" + "        Best regards, <br />"
				+ "        <strong>Pentagon Space Placement Team</strong>" + "      </p>" + "    </div>"
				+ "    <div style='background-color: #f3f4f6; color: #6b7280; padding: 10px; text-align: center; font-size: 12px;'>"
				+ "      &copy; " + java.time.LocalDateTime.now().getYear() + " " + companyName
				+ ". All rights reserved." + "    </div>" + "  </div>" + "</body>" + "</html>";

		return htmlContent;
	}

	public String getTrainerTimeTableEmail(List<String> timeTableDetails) {
		String rows = "";
		for (String details : timeTableDetails) {
			String[] parts = details.split(",");
			rows += "<tr>" + "<td style='border:1px solid #ddd; padding:8px;'>" + parts[0] + "</td>"
					+ "<td style='border:1px solid #ddd; padding:8px;'>" + parts[1] + "</td>"
					+ "<td style='border:1px solid #ddd; padding:8px;'>" + parts[2] + "</td>" + "</tr>";
		}

		return "<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<meta charset='UTF-8' />"
				+ "<meta name='viewport' content='width=device-width, initial-scale=1.0' />"
				+ "<title>Pentagon Space - Trainer Timetable</title>" + "</head>"
				+ "<body style='margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;'>"
				+ "<div style='max-width:600px; margin:40px auto; background:#fff; border-radius:12px; "
				+ "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px;'>"
				+ "<div style='font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;'>Pentagon Space</div>"
				+ "<p style='font-size:1.08rem; color:#263238; font-weight:600; margin:0 0 1rem 0;'>Dear Trainers,</p>"
				+ "<p style='margin:0 0 1.5rem 0; color:#525f7f; font-size:0.9rem; line-height:1.6;'>Here is your teaching timetable:</p>"
				+ "<table style='width:100%; border-collapse:collapse;'>" + "<thead>"
				+ "<tr style='background:#f5f7fa;'>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Technology</th>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Trainer</th>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Timing</th>" + "</tr>" + "</thead>"
				+ "<tbody>" + rows + "</tbody>" + "</table>"
				+ "<div style='width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;'>"
				+ "Pentagon Space Team" + "</div>" + "</div>" + "</body>" + "</html>";
	}

	public String getStudentTimeTableEmail(List<String> timeTableDetails) {
		String rows = "";
		for (String details : timeTableDetails) {
			String[] parts = details.split(",");
			rows += "<tr>" + "<td style='border:1px solid #ddd; padding:8px;'>" + parts[0] + "</td>"
					+ "<td style='border:1px solid #ddd; padding:8px;'>" + parts[1] + "</td>"
					+ "<td style='border:1px solid #ddd; padding:8px;'>" + parts[2] + "</td>" + "</tr>";
		}

		return "<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<meta charset='UTF-8' />"
				+ "<meta name='viewport' content='width=device-width, initial-scale=1.0' />"
				+ "<title>Pentagon Space - Student Timetable</title>" + "</head>"
				+ "<body style='margin:0; padding:10px; background:#f5f7fa; font-family: Arial, Helvetica, sans-serif;'>"
				+ "<div style='max-width:600px; margin:40px auto; background:#fff; border-radius:12px; "
				+ "box-shadow:0 4px 20px rgba(31,180,157,0.09); border:1px solid #e6e8ec; padding:32px;'>"
				+ "<div style='font-size:1.7rem; font-weight:700; color:#ec003f; letter-spacing:1px; text-align:center; margin-bottom:1.2rem;'>Pentagon Space</div>"
				+ "<p style='font-size:1.08rem; color:#263238; font-weight:600; margin:0 0 1rem 0;'>Dear Students,</p>"
				+ "<p style='margin:0 0 1.5rem 0; color:#525f7f; font-size:0.9rem; line-height:1.6;'>Here is your class timetable:</p>"
				+ "<table style='width:100%; border-collapse:collapse;'>" + "<thead>"
				+ "<tr style='background:#f5f7fa;'>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Technology</th>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Trainer</th>"
				+ "<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Timing</th>" + "</tr>" + "</thead>"
				+ "<tbody>" + rows + "</tbody>" + "</table>"
				+ "<div style='width:100%; text-align:center; margin-top:32px; font-size:0.8rem; letter-spacing:0.5px; border-top:1px solid #f0f1f3; padding-top:16px; color:#888;'>"
				+ "Pentagon Space Team" + "</div>" + "</div>" + "</body>" + "</html>";
	}

}
