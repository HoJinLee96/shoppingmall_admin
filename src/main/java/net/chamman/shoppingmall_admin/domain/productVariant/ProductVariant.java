package net.chamman.shoppingmall_admin.domain.productVariant;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.product.Product;
import net.chamman.shoppingmall_admin.domain.productImage.ProductImage;
import net.chamman.shoppingmall_admin.exception.domain.product.variant.ProductVariantIllegalException;
import net.chamman.shoppingmall_admin.exception.domain.product.variant.StockNotEnoughException;
import net.chamman.shoppingmall_admin.exception.domain.product.variant.StockValidationException;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder
@SQLRestriction("deleted = false")
@Table(name = "product_variant")
public class ProductVariant extends BaseEntity {
	
	private static final int MAX_SELLINGPRICE = 100000000;
	private static final int MAX_COSTPRICE = 100000000;
	private static final int MAX_STOCKQUANTITY = 1000;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, updatable = false)
	private Product product; // 부모 상품

	@Column(nullable = false, length = 50)
	private String name; // 상품 옵션명
	
	@Column(unique = true, nullable = false, length = 50)
	private String code; // 상품 코드
	
	@Column(nullable = false)
	private int sellingPrice; // 판매 가격

	@Column(nullable = false)
	private int costPrice; // 소비자 가격

	@Column(nullable = false)
	private int stockQuantity; // 재고

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductVariantStatus status; 
	
	@Builder.Default
	@BatchSize(size = 100)
    @OneToMany(mappedBy = "productVariant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

	@Builder.Default
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "productVariant", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@Getter
	@AllArgsConstructor
	public enum ProductVariantStatus {
		ON_SALE("판매중"), DRAFT("임시 저장"), DISCONTINUED("판매 중지"), DELETED("삭제");

		private final String label;
	}
	
	public void addImage(ProductImage productImage) {
		if(productImage != null) {
			this.images.add(productImage);
		}
	}
	
    public void addOrderItem(OrderItem orderItem) {
		if(orderItem != null) {
			this.orderItems.add(orderItem);
		}
    }

	/**
	 * 상품 판매 이후 재고 수정
	 */
	protected void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new StockNotEnoughException("수량 부족");
		}
		this.stockQuantity = restStock;
	}
	
	public void modifyName(String name, String code) {
    	if(StringUtils.hasText(name)) {
    		this.name = name;
    	}
    	if(StringUtils.hasText(code)) {
    		this.code = code;
    	}
	}

	/**
	 * 판매 정보 수정
	 */
	public void modifySaleInfo(ProductVariantStatus newStatus, int sellingPrice, int costPrice, int stockQuantity) {
		
		// 1. 가격/재고 우선 수정 (기존 로직)
		if (0 <= sellingPrice && sellingPrice <= MAX_SELLINGPRICE) {
			this.sellingPrice = sellingPrice;
		} else {
			throw new StockValidationException("판매 가격 입력 값이 유효하지 않습니다. sellingPrice: " + sellingPrice);
		}
		if (0 <= costPrice && costPrice <= MAX_COSTPRICE) {
			this.costPrice = costPrice;
		} else {
			throw new StockValidationException("소비자 가격 입력 값이 유효하지 않습니다. costPrice: " + costPrice);
		}
		if (0 <= stockQuantity && stockQuantity <= MAX_STOCKQUANTITY) {
			this.stockQuantity = stockQuantity;
		} else {
			throw new StockValidationException("재고 입력 값이 유효하지 않습니다. quantity: " + stockQuantity);
		}

		if(newStatus == ProductVariantStatus.DRAFT || newStatus == ProductVariantStatus.DELETED) {
			throw new ProductVariantIllegalException("이미 등록된 상품을 임시저장 또는 삭제로 바꾸려고함.");
		} else {
			this.status = newStatus;
		}
	}

	/**
	 * 주문 가능 여부를 검증하는 메서드
	 * 
	 * @param requestedQuantity 주문 요청 수량
	 */
	protected void verifyPurchasable(int requestedQuantity) {
		// 1. 상품 상태 검증
		if (this.status != ProductVariantStatus.ON_SALE) {
			throw new IllegalStateException("현재 판매중인 상품이 아닙니다.");
		}

		// 2. 재고 수량 검증
		if (this.stockQuantity < requestedQuantity) {
			throw new StockNotEnoughException("상품의 재고가 부족합니다.");
		}
	}
	
	public void stockIncreaseByCancelCompleted(int count) {
		this.stockQuantity+=count;
	}
	
	public void stockIncreaseByReturnCompleted(int count) {
		this.stockQuantity+=count;
	}

	@Override
	public void softDelete() {
		super.softDelete();
		this.status = ProductVariantStatus.DELETED;
	}
}