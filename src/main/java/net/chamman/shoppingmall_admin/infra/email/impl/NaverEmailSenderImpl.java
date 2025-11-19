package net.chamman.shoppingmall_admin.infra.email.impl;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.infra.email.EmailSendException;
import net.chamman.shoppingmall_admin.infra.email.EmailSender;
import net.chamman.shoppingmall_admin.infra.email.dto.NaverMailRecipientRequestDto;
import net.chamman.shoppingmall_admin.infra.email.dto.NaverMailRequestDto;
import net.chamman.shoppingmall_admin.infra.naver.NaverSignatureGenerator;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:application.properties")
public class NaverEmailSenderImpl implements EmailSender{
	
	private final NaverSignatureGenerator naverSignatureGenerator;
	
	@Value("${naver-api.accessKey}")
	private String accessKey;
	@Value("${naver-email.senderEmail}")
	private String senderEmail;
	
	private final String emailApiUrl = "https://mail.apigw.ntruss.com";
	private final String emailSendPath = "/api/v1/mails";
	
	/** 네이버 API 문자 발송 요청
	 * @param recipientEmail
	 * @param title
	 * @param message
	 * 
	 * @throws EmailSendException{@link #EMAIL_SEND_FAIL} {@link NaverEmailSenderImpl#sendEmail}
	 * @return 이메일 발송 요청 Response Status Code
	 */
	@Override
	public int sendEmail(String recipientEmail, String title, String message) {
		
		log.debug("* Naver Mail 발송 요청. Recipient: [{}], Title: [{}]",
				LogMaskingUtil.maskEmail(recipientEmail, MaskLevel.MEDIUM),
				title);
		
		// 수신자 설정
		NaverMailRecipientRequestDto naverMailRecipientRequestDto = new NaverMailRecipientRequestDto(recipientEmail,recipientEmail,"R");
		List<NaverMailRecipientRequestDto> mails = List.of(naverMailRecipientRequestDto);
		
		// 요청 데이터 생성
		NaverMailRequestDto naverMailRequestDto = NaverMailRequestDto.builder()
				.senderAddress(senderEmail)
				.senderName("달밤 청소")
				.title(title)
				.body(message)
				.advertising(false)
				.recipients(mails)
				.build();
		
		String time = Long.toString(System.currentTimeMillis());
		String naverSignature = naverSignatureGenerator.getNaverSignature("POST", emailSendPath, time);
		
		try {
			
			// 헤더 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("x-ncp-apigw-timestamp", time);
			headers.set("x-ncp-iam-access-key", accessKey);
			headers.set("x-ncp-apigw-signature-v2", naverSignature);
			
			// HTTP 요청
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<NaverMailRequestDto> entity = new HttpEntity<>(naverMailRequestDto, headers);
			ResponseEntity<String> response = restTemplate.exchange(new URI(emailApiUrl + emailSendPath),
					HttpMethod.POST, entity, String.class);
			
			
			return response.getStatusCode().value();
		} catch (Exception e) {
			throw new EmailSendException(e);
		}
	}
	
}
