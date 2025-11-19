package net.chamman.shoppingmall_admin.infra.email;

public interface EmailSender {
	int sendEmail(String recipientEmail, String title, String message);
}
