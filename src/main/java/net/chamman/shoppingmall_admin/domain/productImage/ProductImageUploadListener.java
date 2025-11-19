package net.chamman.shoppingmall_admin.domain.productImage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.productImage.dto.ProductImageUploadEvent;
import net.chamman.shoppingmall_admin.exception.domain.product.ProductIntegrityException;
import net.chamman.shoppingmall_admin.infra.file.FileStorage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductImageUploadListener {

	private final ProductImageRepository productImageRepository;
	private final FileStorage fileStorage;

	@Async // 1. 요청 스레드와 분리 (비동기)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 2. 메인 DB 커밋 *성공* 시에만 실행
	@Transactional(propagation = Propagation.REQUIRES_NEW) // 3. 새 트랜잭션으로 실행 (실패해도 메인 롤백 안 함)
	public void handleImageUpload(ProductImageUploadEvent event) {
		log.info("* S3 업로드 작업 시작 (비동기). ProductImage ID: {}", event.productImageId());

		ProductImage image = null;
		try {
			image = productImageRepository.findById(event.productImageId()).orElseThrow(
					() -> new ProductIntegrityException("Image not found for async upload: " + event.productImageId()));

			// 1. S3 경로 생성
			String folderPath = "products/" + event.productCode() + "/" + event.imageType().name().toLowerCase();

			// 2. S3 업로드 (byte[]를 InputStream으로 변환)
			InputStream inputStream = new ByteArrayInputStream(event.fileData());
			String s3Path = fileStorage.uploadFile(
					inputStream, 
					event.fileData().length, 
					event.contentType(),
					event.originalFileName(), 
					folderPath);
			inputStream.close();

			// 3. DB에 S3 경로 및 상태 업데이트
			image.markAsUploaded(s3Path);
			productImageRepository.save(image); // 새 트랜잭션에서 저장
			log.info("* S3 업로드 작업 성공. ProductImage ID: {}", event.productImageId());

		} catch (Exception e) {
			log.error("* S3 업로드 작업 실패. ProductImage ID: {}. Error: {}", event.productImageId(), e.getMessage());

			// 4. 실패 시 DB에 '실패' 상태 기록
			if (image != null) {
				image.markAsFailed();
				productImageRepository.save(image);
			}
		}
	}
}