package net.chamman.shoppingmall_admin.domain.review;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Review r SET " +
		   "r.deleted = true, " +
		   "r.deletedAt = :now, " +
		   "r.updatedAt = :now, " +
		   "r.updatedBy = :updatedBy, " +
		   "r.version = r.version + 1 " +
		   "WHERE r.member.id = :memberId")
	void softDeleteByMemberId(
		@Param("memberId") Long memberId,
		@Param("now") LocalDateTime now,
		@Param("updatedBy") String updatedBy
	);
	
}