package net.chamman.shoppingmall_admin.domain.payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Optional<Payment> findByPaymentKey(String paymentKey); 
    
    // 오늘 전체 매출 조회 (주문, 반품, 교환 합산)
    @Query("SELECT p FROM Payment p WHERE p.approvedAt BETWEEN :start AND :end")
    List<Payment> findAllDailyTransactions(LocalDateTime start, LocalDateTime end);
    
}
