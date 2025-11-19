package net.chamman.shoppingmall_admin.domain.orderReturn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.chamman.shoppingmall_admin.domain.returnPayment.ReturnPayment;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {

}