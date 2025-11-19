package net.chamman.shoppingmall_admin.domain.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.domain.member.QMember;
import net.chamman.shoppingmall_admin.domain.order.dto.OrderSearchCondition;
import net.chamman.shoppingmall_admin.domain.orderItem.OrderItem.OrderItemStatus;
import net.chamman.shoppingmall_admin.domain.orderItem.QOrderItem;
import net.chamman.shoppingmall_admin.domain.orderPayment.QOrderPayment;
import net.chamman.shoppingmall_admin.domain.productVariant.QProductVariant;

// 클래스명은 꼭 "Repository이름 + Impl" 이어야 Spring Data JPA가 찾을 수 있음
@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // Q-Type 엔티티 (QueryDSL이 빌드 시 자동으로 생성)
    private final QOrder order = QOrder.order;
    private final QMember member = QMember.member;
    private final QOrderItem orderItem = QOrderItem.orderItem;
    private final QProductVariant productVariant = QProductVariant.productVariant;
    private final QOrderPayment orderPayment = QOrderPayment.orderPayment;

    @Override
    public Page<Order> searchOrders(OrderSearchCondition condition, Pageable pageable) {
        
        // 1. 데이터 조회 쿼리 (fetch join으로 N+1 문제 해결)
        // Dto 변환에 Member, OrderItems, ProductVariant, OrderPayments가 필요하므로 fetchJoin
        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.productVariant, productVariant).fetchJoin()
                .leftJoin(order.orderPayments, orderPayment).fetchJoin()
                .where(
                    // 동적 쿼리 조건들
                    dateRange(condition.startDate(), condition.endDate()),
                    orderStatusEq(condition.orderItemStatus()),
                    keywordContains(condition.searchType(), condition.keyword())
                )
                .distinct() // OneToMany 조인으로 인한 중복 Order 제거
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        
        // TODO: Pageable의 Sort 정보를 동적으로 처리하는 로직 (현재는 기본 정렬만)
        query.orderBy(order.createdAt.desc()); 

        List<Order> orders = query.fetch();

        // 2. 카운트 쿼리 (최적화를 위해 별도 실행)
        JPAQuery<Long> countQuery = queryFactory
                .select(order.id.countDistinct())
                .from(order)
                .join(order.member, member) // where절에서 사용되므로 join
                .leftJoin(order.orderItems, orderItem) // where절에서 사용되므로 join
                .leftJoin(orderItem.productVariant, productVariant) // where절에서 사용되므로 join
                .where(
                    dateRange(condition.startDate(), condition.endDate()),
                    orderStatusEq(condition.orderItemStatus()),
                    keywordContains(condition.searchType(), condition.keyword())
                );

        Long total = countQuery.fetchOne();
        total = total != null ? total : 0;
        
        return new PageImpl<>(orders, pageable, total);
    }

    // --- 동적 쿼리 조건 메서드 ---

    private BooleanExpression dateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            return order.createdAt.between(startDateTime, endDateTime);
        } else if (startDate != null) {
            return order.createdAt.goe(startDate.atStartOfDay());
        } else if (endDate != null) {
            return order.createdAt.loe(endDate.atTime(23, 59, 59));
        }
        return null; // 조건이 없으면 null 반환
    }

    private BooleanExpression orderStatusEq(OrderItemStatus status) {
        if (status != null) {
            // 한 주문에 여러 OrderItem이 있을 수 있으므로, 해당 상태의 OrderItem이
            // 하나라도 포함된 Order를 찾습니다. (exists subquery 또는 any() 사용 가능)
            // 여기서는 join된 orderItem의 상태를 직접 비교합니다.
            return orderItem.orderItemStatus.eq(status);
        }
        return null;
    }

    private BooleanExpression keywordContains(String searchType, String keyword) {
        if (!StringUtils.hasText(keyword) || !StringUtils.hasText(searchType)) {
            return null;
        }

        switch (searchType) {
            case "ORDER_NUMBER": // 주문 번호
                return order.orderNumber.containsIgnoreCase(keyword);
            case "MEMBER_NAME": // 주문자명
                return member.name.containsIgnoreCase(keyword);
            case "PRODUCT_NAME": // 상품명
                return productVariant.name.containsIgnoreCase(keyword);
            default:
                return null;
        }
    }
}