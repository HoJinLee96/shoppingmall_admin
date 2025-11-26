package net.chamman.shoppingmall_admin.domain.orderItem;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.memberCoupon.MemberCoupon;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.orderExchange.OrderExchange;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.domain.review.Review;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.exception.domain.orderItem.OrderItemIllegalException;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "order_item")
public class OrderItem extends BaseEntity{
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "shipment_id")
	private Shipment shipment;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_coupon_id")
	private MemberCoupon memberCoupon;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;
	
	@Column(nullable = false)
	private String orderItemNumber;
	
	@Column(nullable = false)
	private int count; // 상품 주문 수량
    
	@Column
    private int couponDiscountAmount; // 쿠폰 통한 할인 금액
    
	@Column(nullable = false)
	private int originalAmount; // 쿠폰 할인 적용된 금액
	
    @Column(nullable = false)
    private int finalAmount; // 쿠폰 할인 적용된 금액 * 갯수 = 최종 금액
    
	@Enumerated(EnumType.STRING)
	private OrderItemStatus orderItemStatus;
	
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private List<OrderReturn> orderReturns = new ArrayList<>();
	
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private List<OrderExchange> orderExchanges = new ArrayList<>();

	@Getter @AllArgsConstructor
	public enum OrderItemStatus {
		PAYMENT_COMPLETED("결제 완료"), 
		PREPARING("상품/배송 준비중"), 
		SHIPPED("배송중"), 
		DELIVERED("배송 완료"),
		COMPLETED("구매 확정"),
		
		// --- 취소 흐름 ---
		CANCEL_REQUESTED("취소 요청"),
		CANCEL_COMPLETED("취소 완료");
//		
//		// --- 반품 흐름 ---
//	    RETURN_REQUESTED("반품 요청"), 
//	    RETURN_COMPLETED("반품 완료"),
//	    
//	    // --- 교환 흐름 ---
//	    EXCHANGE_REQUESTED("교환 요청"), 
//	    EXCHANGE_COMPLETED("교환 완료");


		private final String label;
	}
	
	protected void updateToPreparing() {
		if(!OrderItemStatus.PAYMENT_COMPLETED.equals(this.orderItemStatus)){
			throw new OrderItemIllegalException("상품 준비중 변경 불가. 결제 완료 상태가 아님.");
		}
		this.orderItemStatus = OrderItemStatus.PREPARING;
	}
	
	protected void updateToCancelCompleted() {
        if (!OrderItemStatus.CANCEL_REQUESTED.equals(this.orderItemStatus)) {
			throw new OrderItemIllegalException("취소 처리 불가. 취소 요청 상태가 아님.");
        }
        
		if (this.shipment != null) {
			throw new OrderItemIllegalException("취소 처리 불가. 운송장 정보가 등록되어 있음.");
		}
		
		this.orderItemStatus = OrderItemStatus.CANCEL_COMPLETED;
	}
	
	protected void shipmentStart(Shipment shipment) {
		if(this.shipment!=null){
			throw new OrderItemIllegalException("출고 처리 불가. 이미 배송 정보가 있음.");
		}
		this.shipment = shipment;
		this.orderItemStatus = OrderItemStatus.SHIPPED;
	}
	
	protected void cancelReturn(Shipment shipment) {
		if(this.shipment!=null){
			throw new OrderItemIllegalException("취소 반려 처리 불가. 이미 배송 정보가 있음.");
		}
		this.shipment = shipment;
	}
	
	protected void cancelReturn(OrderReturn orderReturn) {
		if(this.shipment==null){
			throw new OrderItemIllegalException("취소 반려의 반품 등록 불가. 배송 정보가 없음.");
		}
		this.orderReturns.add(orderReturn);
//		this.orderItemStatus = OrderItemStatus.RETURN_REQUESTED;
	}
}