package net.chamman.shoppingmall_admin.domain.cartItem;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.chamman.shoppingmall_admin.domain.member.Member;
import net.chamman.shoppingmall_admin.domain.productVariant.ProductVariant;
import net.chamman.shoppingmall_admin.support.BaseEntity;

@Entity
@Getter
@SQLRestriction("deleted = false")
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private int quantity;
	
    public static CartItem createCartItem(Member member, ProductVariant productVariant, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.member = member;
        cartItem.productVariant = productVariant;
        cartItem.quantity = quantity;
        return cartItem;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}