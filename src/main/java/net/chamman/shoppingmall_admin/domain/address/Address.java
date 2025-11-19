package net.chamman.shoppingmall_admin.domain.address;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.domain.order.Order;
import net.chamman.shoppingmall_admin.domain.orderExchange.OrderExchange;
import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "address")
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@Column(nullable = false, length = 20)
	private String sender; // 발송인

	@Column(nullable = false, length = 20)
	private String recipient; // 수령인

	@Column(nullable = false, length = 15)
	private String recipientPhone; // 수령인 연락처

	@Column(nullable = false, length = 10)
	private String postcode; // 우편번호

	@Column(nullable = false, length = 250)
	private String mainAddress; // 기본 주소

	@Column(nullable = false, length = 50)
	private String detailAddress; // 상세 주소

	@Column(length = 50)
	private String shippingMemo; // 배송 메모

//	@Builder.Default
	@Column(name = "is_primary")
	private boolean isPrimary = false;

//	@Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Order> orders = new ArrayList<>();

//	@Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<OrderReturn> orderReturns = new ArrayList<>();

//	@Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "pickupAddress", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<OrderExchange> pickupOrderExchanges = new ArrayList<>();

//	@Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "exchangeShippingAddress", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<OrderExchange> exchangeShippingOrderExchanges = new ArrayList<>();

	// 회원 탈퇴 이후 30일지나 모든 정보 익명화
	public void anonymize() {

		// 개인 식별 정보는 확실히 제거
		this.recipient = "알수없음";
		this.recipientPhone = "000-0000-0000";
		this.detailAddress = ""; // 상세 주소는 무조건 제거
		this.shippingMemo = "";

		// 통계에 필요한 정보는 가공해서 남긴다
		// 예: "서울 강남구 역삼동" 까지만 남기고 나머지는 자르기
//		if (this.mainAddress != null && this.mainAddress.length() > 10) { // 간단한 예시
//			this.mainAddress = extractCityAndDistrict(this.mainAddress); // 시/구/동만 추출하는 별도 로직 필요
//		} else {
//			this.mainAddress = "주소정보없음";
//		}

		// 우편번호도 앞 3자리만 남기기?
		if (this.postcode != null && this.postcode.length() == 5) {
			this.postcode = this.postcode.substring(0, 3) + "**";
		}

	}
	
//	public static Address createAddress(String sender, String recipient, String recipientPhone, String postcode, ) {
//		return new Address
//	}

}