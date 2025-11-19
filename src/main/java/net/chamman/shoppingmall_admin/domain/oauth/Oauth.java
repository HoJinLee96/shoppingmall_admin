package net.chamman.shoppingmall_admin.domain.oauth;

import java.util.UUID;

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
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "oauth")
public class Oauth extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OauthProvider provider;

	@Column(unique = true, nullable = false)
	private String providerId;

	public enum OauthProvider {
		NAVER("네이버"), KAKAO("카카오");

		private final String label;

		OauthProvider(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}
	
	// 회원 탈퇴 이후 30일지나 모든 정보 익명화
	public void anonymize() {
		
		String anonymized = "_deleted_" + this.id + "_" + UUID.randomUUID().toString().substring(0, 8);
		this.providerId = providerId.substring(0, 3) + "****" + anonymized;

	}

}
