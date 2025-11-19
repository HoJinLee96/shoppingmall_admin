package net.chamman.shoppingmall_admin.domain.wishlist;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{

//	List<Wishlist> findByMemberId(long memberId);
	
	@Modifying(clearAutomatically = true) // 벌크 연산 시 해당 애노테이션 통해 영속성 컨테스트 초기화
	@Query("UPDATE Wishlist wl SET " +
		   "wl.deleted = true, " +
		   "wl.deletedAt = :now, " +
		   "wl.updatedAt = :now, " +
		   "wl.updatedBy = :updatedBy, " +
		   "wl.version = wl.version + 1 " +
		   "WHERE wl.member.id = :memberId")
	void softDeleteByMemberId(
		@Param("memberId") Long memberId,
		@Param("now") LocalDateTime now,
		@Param("updatedBy") String updatedBy
	);
	
}
