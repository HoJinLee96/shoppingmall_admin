package net.chamman.shoppingmall_admin.domain.shipment;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter @Builder
@AllArgsConstructor
@Table(name = "shipment")
@SQLRestriction("deleted = false")
public class Shipment extends BaseEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String shippingCompany; // 택배 회사
	
	@Column(nullable = false)
	private String trackingNumber; // 운송장 번호

//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	private ShipmentStatus status;
	
//	@Getter @AllArgsConstructor
//	public enum ShipmentStatus {
//		SHIPPED("배송중"),
//		DELIVERED("배송 완료");
//		
//		private final String label;
//	}
}
