package net.chamman.shoppingmall_admin.infra.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
	
	String uploadFile(InputStream inputStream, long size, String contentType, String originalFileName, String keyPrefix);
	
	/**
     * 파일을 지정된 디렉토리에 업로드하고, 접근 가능한 URL을 반환합니다.
     *
     * @param file 업로드할 MultipartFile
     * @param directory 저장할 디렉토리 (예: "products", "reviews")
     * @return 업로드된 파일의 전체 URL
     * @throws IOException 파일 처리 중 오류 발생 시
     */
    String uploadFile(MultipartFile file, String directory);

    /**
     * 파일 URL을 기반으로 저장소에서 파일을 삭제합니다.
     *
     * @param fileUrl 삭제할 파일의 전체 URL
     */
    void deleteFile(String fileUrl);
    
    
    /**
     * 파일 URL을 기반으로 저장소에서 파일을 삭제합니다.
     *
     * @param fileUrls 삭제할 파일의 전체 URL
     */
    void deleteFiles(List<String> fileUrls);
    
}
