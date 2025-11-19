package net.chamman.shoppingmall_admin.domain.order;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.address.Address;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;
import net.chamman.shoppingmall_admin.domain.orderPayment.OrderPayment;
import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String orderNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@BatchSize(size = 100)
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private List<OrderPayment> orderPayments = new ArrayList<>();
	
//	@BatchSize(size = 100)
//	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
//	private List<ReturnPayment> returnPayments = new ArrayList<>();
//	
//	public void addReturnPayment(ReturnPayment returnPayment) {
//		returnPayments.add(returnPayment);
//	}
}