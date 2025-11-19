package net.chamman.shoppingmall_admin.domain.question;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.chamman.shoppingmall_admin.domain.question.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Question q SET " +
           "q.deleted = true, " +
           "q.deletedAt = :now, " +
           "q.updatedAt = :now, " +
           "q.updatedBy = :updatedBy, " +
           "q.version = q.version + 1 " +
           "WHERE q.member.id = :memberId")
    void softDeleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("now") LocalDateTime now,
        @Param("updatedBy") String updatedBy
    );
    
}
