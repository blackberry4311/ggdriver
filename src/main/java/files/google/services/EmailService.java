package files.google.services;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/*
 * Example email smtp sender service, consider make it as interface message
 * sender run as async service - Black
 */
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadFactoryBuilder().setDaemon(true)
					.setNameFormat("SmtpEmailSender-%s").build());

	public long sendMessage(EmailSenderObject object) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					sendMessageAsync(object);
				} catch (Exception e) {
					logger.error("Error on send email", e);
					throw new IllegalStateException(e);
				} finally {
				}
			}
		});
		return executorService.getTaskCount();
	}

	private void sendMessageAsync(EmailSenderObject object) {
		// precondition, consider using some util in apache common or other
		// similar
		if (object.getTo() == null || object.getTo().isEmpty() || object.getFrom().isEmpty()
				|| object.getFrom() == null) return;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(object.getUsername(), object.getPassword());
			}
		});

		try {
			logger.info("trying to send message: {} to {}", object.getContent(), object.getTo());
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(object.getFrom()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(object.getTo()));
			message.setSubject(object.getTitle());
			message.setText(object.getContent());
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
