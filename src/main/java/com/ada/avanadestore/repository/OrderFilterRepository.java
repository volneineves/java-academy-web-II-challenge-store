package com.ada.avanadestore.repository;

import com.ada.avanadestore.dto.OrderFilterDTO;
import com.ada.avanadestore.entitity.Order;
import com.ada.avanadestore.entitity.QOrder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderFilterRepository extends QuerydslRepositorySupport {

    @PersistenceContext
    private EntityManager entityManager;

    public OrderFilterRepository() {
        super(Order.class);
    }

    public List<Order> findByFilter(OrderFilterDTO filter) {
        QOrder order = QOrder.order;
        List<Predicate> predicates = new ArrayList<>();

        if (filter.customerId() != null) {
            predicates.add(order.customer.id.eq(filter.customerId()));
        }
        if (filter.minDate() != null) {
            predicates.add(order.orderDate.goe(filter.minDateConvertedToDate()));
        }
        if (filter.maxDate() != null) {
            predicates.add(order.orderDate.loe(filter.maxDateConvertedToDate()));
        }
        if (filter.status() != null) {
            predicates.add(order.status.eq(filter.status()));
        }

        return new JPAQueryFactory(entityManager)
                .selectFrom(order)
                .where(predicates.toArray(new Predicate[0]))
                .fetch();
    }
}
