package net.chamman.shoppingmall_admin.security.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.HttpStatusCode;
import net.chamman.shoppingmall_admin.exception.security.crypto.DecryptException;
import net.chamman.shoppingmall_admin.exception.security.crypto.EncryptException;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class AesProvider {

	private final SecretKeySpec secretKeySpec;

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final int KEY_LENGTH = 16; // 16 bytes = 128 bits

	public AesProvider(@Value("${aes.secret}") String secretKeyRaw) {
		byte[] keyBytes = secretKeyRaw.substring(0, KEY_LENGTH).getBytes(StandardCharsets.UTF_8);
		this.secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	}

	/**
	 * @param plainText
	 * @throws EncryptException
	 * @return
	 */
	public String encrypt(String plainText) {
		log.debug("* 암호화. 대상: [{}]", LogMaskingUtil.maskText(plainText, MaskLevel.NONE));

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			byte[] iv = new byte[KEY_LENGTH];
			new SecureRandom().nextBytes(iv); // 랜덤 IV
			IvParameterSpec ivSpec = new IvParameterSpec(iv);

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			byte[] combined = new byte[iv.length + encrypted.length];
			System.arraycopy(iv, 0, combined, 0, iv.length);
			System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

			return Base64.getEncoder().encodeToString(combined);
		} catch (Exception e) {
			log.error("* AES 암호화 실패. plainText: [{}]",plainText, e);
			throw new EncryptException("AES 암호화 실패. " + e.getMessage(), e);
		}
	}

	/**
	 * @param encryptedText
	 * @throws DecryptException
	 * @return
	 */
	public String decrypt(String encryptedText) {
		log.debug("* 복호화. 대상: [{}]", LogMaskingUtil.maskText(encryptedText, MaskLevel.NONE));

		try {
			byte[] decoded = Base64.getDecoder().decode(encryptedText);

			byte[] iv = new byte[KEY_LENGTH];
			byte[] encrypted = new byte[decoded.length - KEY_LENGTH];
			System.arraycopy(decoded, 0, iv, 0, KEY_LENGTH);
			System.arraycopy(decoded, KEY_LENGTH, encrypted, 0, encrypted.length);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

			byte[] decrypted = cipher.doFinal(encrypted);
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error("* AES 복호화 실패. plainText: [{}]",encryptedText, e);
			throw new DecryptException("AES 복호화 실패. " + e.getMessage(), e);
		}
	}
}
