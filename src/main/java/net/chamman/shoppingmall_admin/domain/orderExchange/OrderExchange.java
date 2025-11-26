package net.chamman.shoppingmall_admin.domain.orderExchange;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.domain.exchangePayment.ExchangePayment;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.shipment.Shipment;
import net.chamman.shoppingmall_admin.support.BaseEntity;


@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "order_exchange")
public class OrderExchange extends BaseEntity{

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pickup_address_id", nullable = false)
	private Address pickupAddress;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "pickup_shipment_id")
	private Shipment pickupShipment;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_shipping_address_id", nullable = false)
	private Address exchangeShippingAddress;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "exchange_shipping_shipment_id")
	private Shipment exchangeShippingShipment;
	
    @Column(nullable = false)
	private int exchangeCount; // 교환 수량
    
	@Enumerated(EnumType.STRING)
    private ExchangeReason exchangeReason; // 교환 사유
	
    @OneToOne(mappedBy = "orderExchange", cascade = CascadeType.ALL)
    private ExchangePayment exchangePayment; // 공제
	
	@Enumerated(EnumType.STRING)
	private OrderExchangeStatus orderExchangeStatus;
	
	@Getter
	public enum ExchangeReason {
		
	    CUSTOMER_FAULT("고객 귀책사유"),
	    SELLER_FAULT("판매자 귀책사유");
		
		private final String label;
		
		ExchangeReason(String label) {
			this.label = label;
		}
	}
	
	@Getter
	public enum OrderExchangeStatus {
		
	    // --- 교환 흐름 ---
		PARTIAL_EXCHANGE_REQUESTED("일부 교환중"), 
	    EXCHANGE_REQUESTED("교환중"), 
	    EXCHANGE_INSPECTING("교환 검수중"),
	    EXCHANGE_COMPLETED("교환 완료");

		private final String label;

		OrderExchangeStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}