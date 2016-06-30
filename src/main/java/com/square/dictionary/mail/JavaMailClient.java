package com.square.dictionary.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailClient {
	/*private static Properties properties = new Properties(); static {
		try {			
			properties.load(JavaMailClient.class.getResourceAsStream("/config.properties"));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public static void sendConfirmationMail(String username, String toEmailAddress) {
		final String user = "parasme.rahul@gmail.com";
		final String pass = "7737772424";
		
		Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "465");
 
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});
		
		try{ 
			System.out.println("Sending mail to " + toEmailAddress);
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(user));  
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("khan.square@gmail.com"));  
			message.setSubject("Dictionary Account Confirmation");  
			message.setText("Dear " + username + ", Please verify your account by clicking on below link...");  
			Transport.send(message);
			System.out.println("dONE");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}  
	}
}
