package net.chamman.shoppingmall_admin.domain.exchangePayment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.orderExchange.OrderExchange;
import net.chamman.shoppingmall_admin.domain.payment.Payment;

@Entity
@Getter
@DiscriminatorValue("EXCHANGE")
public class ExchangePayment extends Payment {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_exchange_id", nullable = false)
    private OrderExchange orderExchange;
}