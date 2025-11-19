package net.chamman.shoppingmall_admin.domain.coupon;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

//	List<MemberCoupon> findByMemberId(long memberId);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE MemberCoupon mc SET " +
		   "mc.deleted = true, " +
		   "mc.deletedAt = :now, " +
		   "mc.updatedAt = :now, " +
		   "mc.updatedBy = :updatedBy, " +
		   "mc.version = mc.version + 1 " +
		   "WHERE mc.member.id = :memberId")
	void softDeleteByMemberId(
		@Param("memberId") Long memberId,
		@Param("now") LocalDateTime now,
		@Param("updatedBy") String updatedBy
	);
	
}