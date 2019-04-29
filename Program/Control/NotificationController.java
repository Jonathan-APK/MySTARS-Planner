/**
	Represents a control class for the notifications to be sent out.
	Notification in the form of SMS/Email.
	
	@author Lim Boon Leng
	@version 1.0
	@since 2017-04-06
*/

/*The details of this notification is as such :
(1) The notification can be via email or SMS mode.
(2) For email, a actual email will be sent from the application with Subject "Waitlist notification" and message "You have been registered to <Course code>."   Fill the actual data for <Course code>.
(3) For SMS, it will just be a System.out display of "A SMS is sent to <mobileNo>". <mobileNo> is the mobile number recorded in the application.
(4) A Student function for selecting the mode of notification "Select Notification Mode". It can be email, SMS or both. You can also let the user enter the mobile number or email address besides through Admin's "Add Student" function.

About email :
(1) Look at JavaMail and use the API. There are available samples online.
(2) You can create an email account just for this assignment purpose (using coursecode + your labGroup + assignmentGroup should be quite an unique address).
    Change the password after the exam.*/

package Control;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificationController {
	/**
	 * The email of the sender (the program is the sender)
	 */
	private static final String SENDER_EMAIL = "CZ2002FSP7Grp1@gmail.com";
	/**
	 * The password used for the sender's email (the program is the sender)
	 */
	private static final String SENDER_PW = "P@ssw0rd123";
	
	/**
	* Simulates sending of SMS to recipient
	* @param recipientNum The phone number of the recipient
	*/
	public static void sendSMS(String recipientNum) {
		System.out.println("A SMS has been sent to " + recipientNum + "\n");
	}
	
	/**
	* Sends an email to recipient
	* @param recipient The email of the recipient
	* @param courseID The courseID of the course that the recipient has successfully joined
	*/
	public static void sendEmail(String recipient, String courseID) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SENDER_EMAIL, SENDER_PW);
				}
			});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SENDER_EMAIL));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipient));
			message.setSubject("Waitlist notification");
			message.setText("You have been registered to " + courseID);

			Transport.send(message);

			System.out.println("An email has been sent to " + recipient + "\n");

		} catch (MessagingException e) {
			System.out.println("You are not connected to the internet!");
		}
	}
}
