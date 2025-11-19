package net.chamman.shoppingmall_admin.domain.orderExchange;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExchangeRepository extends JpaRepository<OrderExchange, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OrderExchange oex SET " +
           "oex.deleted = true, " +
           "oex.deletedAt = :now, " +
           "oex.updatedAt = :now, " +
           "oex.updatedBy = :updatedBy, " +
           "oex.version = oex.version + 1 " +
           "WHERE oex.orderItem.order.member.id = :memberId")
    void softDeleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("now") LocalDateTime now,
        @Param("updatedBy") String updatedBy
    );
}