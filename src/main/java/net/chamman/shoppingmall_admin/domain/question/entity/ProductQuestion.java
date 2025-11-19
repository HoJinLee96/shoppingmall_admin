package net.chamman.shoppingmall_admin.domain.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.product.Product;

@Entity
@Getter
@Table(name = "product_question")
@DiscriminatorValue("PRODUCT")
public class ProductQuestion extends Question {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ProductCategory category;

    @Getter
    public enum ProductCategory {
        STOCK("재고 문의"),
        PRODUCT_DETAILS("상품 상세 문의"),
        SHIPPING_INFO("배송 문의"),
        RECOMMENDATION("상품 추천 문의"),
        ETC("기타 문의");

        private final String label;

        ProductCategory(String label) {
            this.label = label;
        }
    }
    
}