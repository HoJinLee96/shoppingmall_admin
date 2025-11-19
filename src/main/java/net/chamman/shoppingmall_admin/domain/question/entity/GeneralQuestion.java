package net.chamman.shoppingmall_admin.domain.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "general_question")
@DiscriminatorValue("GENERAL") // 구분 컬럼(DTYPE)에 저장될 값
public class GeneralQuestion extends Question {
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 30)
	private GeneralCategory category;

	@Getter
	public enum GeneralCategory {
		SITE_USAGE("사이트 이용"), 
		MEMBERSHIP("회원/정보"), 
		EVENT("이벤트"), 
		ETC("기타 문의");

		private final String label;

		GeneralCategory(String label) {
			this.label = label;
		}
	}
}