package net.chamman.shoppingmall_admin.domain.shipment;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Shipment s SET "
    		+ "s.deleted = true, "
    		+ "s.deletedAt = :now, "
    		+ "s.updatedAt = :now, "
    		+ "s.updatedBy = :updatedBy, "
    		+ "s.version = s.version + 1 "
    		+ "WHERE s.id IN (SELECT oi.shipment.id FROM OrderItem oi WHERE oi.order.member.id = :memberId)")
    void softDeleteForOrderItemsByMemberId(
    		@Param("memberId") Long memberId, 
    		@Param("now") LocalDateTime now, 
    		@Param("updatedBy") String updatedBy);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Shipment s SET "
    		+ "s.deleted = true, "
    		+ "s.deletedAt = :now, "
    		+ "s.updatedAt = :now, "
    		+ "s.updatedBy = :updatedBy, "
    		+ "s.version = s.version + 1 "
    		+ "WHERE s.id IN (SELECT ort.shipment.id FROM OrderReturn ort WHERE ort.orderItem.order.member.id = :memberId)")
    void softDeleteForOrderReturnsByMemberId(
    		@Param("memberId") Long memberId, 
    		@Param("now") LocalDateTime now, 
    		@Param("updatedBy") String updatedBy);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Shipment s SET "
    		+ "s.deleted = true, "
    		+ "s.deletedAt = :now, "
    		+ "s.updatedAt = :now, "
    		+ "s.updatedBy = :updatedBy, "
    		+ "s.version = s.version + 1 "
    		+ "WHERE s.id IN (SELECT oex.pickupShipment.id FROM OrderExchange oex WHERE oex.orderItem.order.member.id = :memberId) "
    		+ "OR s.id IN (SELECT oex.exchangeShippingShipment.id FROM OrderExchange oex WHERE oex.orderItem.order.member.id = :memberId)")
    void softDeleteForOrderExchangesByMemberId(
    		@Param("memberId") Long memberId, 
    		@Param("now") LocalDateTime now, 
    		@Param("updatedBy") String updatedBy);
    
}