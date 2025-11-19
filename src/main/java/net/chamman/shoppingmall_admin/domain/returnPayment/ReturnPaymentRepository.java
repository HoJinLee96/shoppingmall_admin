package net.chamman.shoppingmall_admin.domain.returnPayment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.chamman.shoppingmall_admin.domain.orderReturn.OrderReturn;

public interface ReturnPaymentRepository extends JpaRepository<ReturnPayment, Long>{

	Optional<ReturnPayment> findByOrderReturn(OrderReturn orderReturn);
	
}
