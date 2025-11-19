package net.chamman.shoppingmall_admin.domain.orderItem;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OrderItem oi SET " +
           "oi.deleted = true, " +
           "oi.deletedAt = :now, " +
           "oi.updatedAt = :now, " +
           "oi.updatedBy = :updatedBy, " +
           "oi.version = oi.version + 1 " +
           "WHERE oi.order.member.id = :memberId")
    void softDeleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("now") LocalDateTime now,
        @Param("updatedBy") String updatedBy
    );
    
}