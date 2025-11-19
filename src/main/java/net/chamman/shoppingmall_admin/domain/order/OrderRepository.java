package net.chamman.shoppingmall_admin.domain.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	/**
     * DTO 변환에 필요한 모든 연관 엔티티를 함께 조회합니다.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN FETCH o.member m " +
           "JOIN FETCH o.address a " +
           "LEFT JOIN FETCH o.orderPayments op " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.productVariant pv " +
           "LEFT JOIN FETCH oi.shipment s " +
           "WHERE o.id = :orderId")
    Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);
	
}
