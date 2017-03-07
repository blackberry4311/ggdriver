package files.google.services;

import java.io.IOException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

public class AuthorizationCodeInstalledLocal extends AuthorizationCodeInstalledApp {
	EmailSenderObject emailObject;

	public EmailSenderObject getEmailObject() {
		return emailObject;
	}

	public void setEmailObject(EmailSenderObject emailObject) {
		this.emailObject = emailObject;
	}

	public AuthorizationCodeInstalledLocal(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver,
			EmailSenderObject emailObject) {
		super(flow, receiver);
		this.emailObject = emailObject;
	}

	@Override
	protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
		try {
			emailObject.setContent("Please open the link below to confirm access google driver: "
					+ authorizationUrl.build());
			new EmailService().sendMessage(emailObject);
		} catch (RuntimeException ex) {

		}
		super.onAuthorization(authorizationUrl);
	}
}
