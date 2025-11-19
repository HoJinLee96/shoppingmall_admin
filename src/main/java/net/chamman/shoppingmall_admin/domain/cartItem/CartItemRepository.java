package net.chamman.shoppingmall_admin.domain.cartItem;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

//	List<CartItem> findByMemberId(long memberId);
	
	@Modifying(clearAutomatically = true) // 벌크 연산 시 해당 애노테이션 통해 영속성 컨테스트 초기화
	@Query("UPDATE CartItem ci SET " +
		   "ci.deleted = true, " +
		   "ci.deletedAt = :now, " +
		   "ci.updatedAt = :now, " +
		   "ci.updatedBy = :updatedBy, " +
		   "ci.version = ci.version + 1 " +
		   "WHERE ci.member.id = :memberId")
	void softDeleteByMemberId(
		@Param("memberId") Long memberId,
		@Param("now") LocalDateTime now,
		@Param("updatedBy") String updatedBy
	);
	
}
