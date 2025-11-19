package net.chamman.shoppingmall_admin.domain.productImage;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.support.BaseEntity;


@Entity
@Getter @Builder
@NoArgsConstructor // 엔티티는 기본 생성자가 필요 (JPA가 프록시 만들 때 씀)
@AllArgsConstructor // Builder를 쓰려면 필요
@SQLRestriction("deleted = false")
@Table(name = "product_img")
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImageType imageType;

    @Column(length = 200, nullable = true)
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ImageStatus status = ImageStatus.PENDING_UPLOAD;
    
    public enum ImageType {
    	MAIN,    // 메인 이미지
    	DETAIL,  // 상세 이미지
    	CONTENT  // 상품 내용(설명) 이미지
    }

    public enum ImageStatus {
        PENDING_UPLOAD, // 업로드 대기 중
        UPLOADED,       // 업로드 완료
        UPLOAD_FAILED   // 업로드 실패
    }
    
    protected void markAsUploaded(String imagePath) {
        this.imagePath = imagePath;
        this.status = ImageStatus.UPLOADED;
    }
    
    protected void markAsFailed() {
        this.status = ImageStatus.UPLOAD_FAILED;
    }
}
