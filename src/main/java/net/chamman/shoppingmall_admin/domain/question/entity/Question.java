package net.chamman.shoppingmall_admin.domain.question.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.answer.Answer;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "question")
@Inheritance(strategy = InheritanceType.JOINED) // JOINED 전략 사용
@DiscriminatorColumn(name = "DTYPE") // 어떤 자식 타입인지 구분하는 컬럼 자동 생성
public abstract class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();
    
    @Column(nullable = false, length = 50)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus;
    
	public enum QuestionStatus {
		PENDING("답변 대기 중"), ANSWER("답변 완료");
		
		private final String label;
		
		QuestionStatus(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
	}
	
	public void addAnswer(Answer answer) {
		answers.add(answer);
		answer.setQuestion(this);
	}

	public void modify(String title, String content) {
		if (title != null && !title.isBlank()) {
			this.title = title;
		}
		if (content != null && !content.isBlank()) {
			this.content = content;
		}
	}

	public void updateStatus(QuestionStatus questionStatus) {
		if (questionStatus != null) {
			this.questionStatus = questionStatus;
		}
	}

	public void revertToPending() {
		this.questionStatus = QuestionStatus.PENDING;
	}

	public void markAsAnswered() {
		this.questionStatus = QuestionStatus.ANSWER;
	}

}