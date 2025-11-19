package net.chamman.shoppingmall_admin.infra.sms;

public interface SmsSender {
	int sendSms(String recipientPhone, String message);
}
