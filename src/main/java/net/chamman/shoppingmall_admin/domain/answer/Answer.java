package net.chamman.shoppingmall_admin.domain.answer;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.chamman.shoppingmall_admin.domain.admin.Admin;
import net.chamman.shoppingmall_admin.domain.question.entity.Question;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "answer")
public class Answer extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	@Setter
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false)
	@Setter
	private Admin admin;

	@Column(nullable = false, length = 1000)
	private String content;

	public void modify(String content) {
		if (content != null && !content.isBlank()) {
			this.content = content;
		}
	}

	public boolean verifyAdmin(Long currentAdminId) {
		return this.admin.getId() == currentAdminId;
	}

}