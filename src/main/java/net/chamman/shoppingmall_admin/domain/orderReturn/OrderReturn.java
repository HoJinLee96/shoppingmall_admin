package net.chamman.shoppingmall_admin.domain.orderReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.exception.domain.order.OrderReturnIllegalException;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder @AllArgsConstructor
@SQLRestriction("deleted = false")
@Table(name = "order_return")
public class OrderReturn extends BaseEntity{
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem; // 반품 주문 상품
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address; // 반품 주소
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id")
	private Shipment shipment; // 운송
	
    @Column(nullable = false)
	private int returnCount; // 반품 수량
    
	@Enumerated(EnumType.STRING)
    private ReturnReason returnReason; // 반품 사유
	
	@Enumerated(EnumType.STRING)
	private OrderReturnStatus orderReturnStatus;
	
	@Builder.Default
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "orderReturn", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<ReturnPayment> returnPayment = new ArrayList<>();
	
	@Getter @AllArgsConstructor
	public enum ReturnReason {
		
	    CUSTOMER_FAULT("고객 귀책사유"),
	    SELLER_FAULT("판매자 귀책사유");
		
		private final String label;
		
	}
	
	@Getter @AllArgsConstructor
	public enum OrderReturnStatus {
		
	    RETURN_REQUESTED("반품중"), 
	    RETURN_INSPECTING("반품 검수중"),
	    RETURN_COMPLETED("반품 완료");
	    
		private final String label;

	}

	protected void shipmentStart(Shipment shipment) {
		if(shipment == null) {
			throw new OrderReturnIllegalException("반품 운송장 기입 처리 불가. shipment == null");
		}
		if (this.shipment != null) {
			throw new OrderReturnIllegalException("반품 운송장 기입 처리 불가. 이미 운송장 정보가 등록되어 있음.");
		}
		this.shipment = shipment;
	}
	
	protected void shipmentUpdate(Shipment shipment) {
		if(shipment == null) {
			throw new OrderReturnIllegalException("반품 운송장 수정 처리 불가. shipment == null");
		}
		this.shipment = shipment;
	}
	
	protected void arrvied() {
		
		if(!Objects.equals(OrderReturnStatus.RETURN_REQUESTED, this.orderReturnStatus)) {
			throw new OrderReturnIllegalException("반품 입고 처리 불가. 반품 상품 상태가 반품중이 아님.");
		}
		
		if (this.shipment == null) {
			throw new OrderReturnIllegalException("반품 입고 처리 불가. 운송장 정보가 등록되어 있지 않음.");
		}
		
		this.orderReturnStatus=OrderReturnStatus.RETURN_INSPECTING;
		
	}
	
	protected void refund(ReturnPayment returnPayment) {
		this.returnPayment.add(returnPayment);
		this.orderReturnStatus=OrderReturnStatus.RETURN_COMPLETED;
	}
	
}
