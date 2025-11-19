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
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem;

@Entity
@Getter
@Table(name = "order_question")
@DiscriminatorValue("ORDER")
public class OrderQuestion extends Question {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 30)
	private OrderCategory category;

	@Getter
	public enum OrderCategory {
		PAYMENT("결제 문의"), 
		SHIPPING("배송 문의"), 
		CANCEL("취소 문의"), 
		EXCHANGE("교환 문의"), 
		RETURN("반품 문의"),
		ORDER_DETAILS("주문내역 확인"), 
		ETC("기타 문의");

		private final String label;

		OrderCategory(String label) {
			this.label = label;
		}
	}

}