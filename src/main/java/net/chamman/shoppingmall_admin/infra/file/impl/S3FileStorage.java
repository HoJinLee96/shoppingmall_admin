package net.chamman.shoppingmall_admin.infra.file.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.infra.file.FileDeleteException;
import net.chamman.shoppingmall_admin.exception.infra.file.FileIllegalException;
import net.chamman.shoppingmall_admin.exception.infra.file.FileUploadException;
import net.chamman.shoppingmall_admin.infra.file.FileStorage;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    @Override
    public String uploadFile(InputStream inputStream, long size, String contentType, String originalFileName, String keyPrefix) {
    	
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        String s3Key = keyPrefix + "/" + uniqueFileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));
            
            log.info("* S3 파일 업로드 성공. Key: [{}]", s3Key);

            GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(s3Key).build();
            URL fileUrl = s3Client.utilities().getUrl(getUrlRequest);
            return fileUrl.toString();

        } catch (Exception e) {
            log.error("* S3 파일 업로드 실패. Key: [{}]", s3Key, e);
            throw new FileUploadException("S3 파일 업로드 실패", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String keyPrefix) {
        if (file == null || file.isEmpty()) {
            throw new FileIllegalException("업로드할 파일이 비어있습니다.");
        }
        
        try {
        	return uploadFile(
        			file.getInputStream(), 
        			file.getSize(), 
        			file.getContentType(), 
        			file.getOriginalFilename(), 
        			keyPrefix);
        	
        } catch (IOException e) {
            log.error("* MultipartFile에서 InputStream을 가져오는데 실패.", e);
            throw new FileUploadException("파일 스트림 읽기 실패", e);
        }
    }
    
	/**
	 * 파일들 삭제
	 *  
	 * @param fileUrls
	 * 
	 * @throws FileDeleteException
	 */
	@Override
	public void deleteFiles(List<String> fileUrls) {

		if (fileUrls == null || fileUrls.isEmpty()) {
            log.warn("* 삭제할 fileUrls이 null 또는 비어있습니다.");
			return;
		}
		for(String s : fileUrls) {
			log.debug("* S3 파일 삭제 요청. fileUrl: [{}]", s);
	        if (!StringUtils.hasText(s)) {
	            log.warn("* 삭제할 fileUrl이 null 또는 비어있습니다.");
	        }
		}

		try {
			// S3 Object Key 리스트 변환
			List<ObjectIdentifier> objectIdentifiers = fileUrls.stream()
					.map(path -> ObjectIdentifier.builder().key(path).build())
					.collect(Collectors.toList());
			
			// DeleteObjectsRequest 생성
			DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
					.bucket(bucketName)
					.delete(builder -> builder.objects(objectIdentifiers))
					.build();
			
			s3Client.deleteObjects(deleteRequest);
		} catch (Exception e) {
            throw new FileDeleteException("S3 파일 삭제 실패. 알 수 없는 익셉션 발생.", e);
		}
	}

    /**
     * 단일 파일 삭제
     * 
	 * @param fileUrl
	 * 
     * @throws FileDeleteException
     */
    @Override
    public void deleteFile(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            log.warn("* 삭제할 fileUrl이 null 또는 비어있습니다.");
            return;
        }

        try {
            // 전체 URL에서 S3 Key(경로) 추출
            String s3Key = getKeyFromUrl(fileUrl);
        	
        	DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("* S3 파일 삭제 성공. Key: [{}]", s3Key);

        } catch (Exception e) {
            log.error("* S3 파일 삭제 실패. URL: [{}]", fileUrl, e);
            throw new FileDeleteException("S3 파일 삭제 실패. 알 수 없는 익셉션 발생.", e);
        }
    }

    /**
     * S3의 전체 URL에서 객체 키(버킷 내 경로)를 추출합니다.
     * @param fileUrl (예: https://bucket-name.s3.region.amazonaws.com/products/image.jpg)
     * @return 버킷 내 경로 (예: products/image.jpg)
     */
    private String getKeyFromUrl(String fileUrl) {
        try {
            // 네가 말한대로 URL(String) 생성자 대신 URI 사용
            URI uri = new URI(fileUrl);
            String path = uri.getPath(); // 예: /products/image.jpg
            
            // S3 key는 맨 앞의 '/'를 제외해야 함 (e.g., 'products/image.jpg')
            if (path.startsWith("/")) {
                return path.substring(1);
            }
            return path;
        } catch (URISyntaxException e) { // URL(String)은 MalformedURLException, URI(String)은 URISyntaxException을 던짐
            log.error("* S3 URL 구문 분석 실패. URL: [{}]", fileUrl, e);
            throw new FileIllegalException("유효하지 않은 S3 URL 형식입니다.", e);
        }
    }

}