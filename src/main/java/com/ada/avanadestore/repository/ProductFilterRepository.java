package com.ada.avanadestore.repository;

import com.ada.avanadestore.dto.ProductFilterDTO;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.entitity.QProduct;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductFilterRepository extends QuerydslRepositorySupport {

    @PersistenceContext
    private EntityManager entityManager;

    public ProductFilterRepository() {
        super(Product.class);
    }

    public List<Product> findByFilter(ProductFilterDTO filter) {
        QProduct product = QProduct.product;
        List<Predicate> predicates = new ArrayList<>();
        if (filter.isAvailable() != null) {
            BooleanExpression expression = filter.isAvailable() ? product.stock.goe(0) : product.stock.loe(1);
            predicates.add(expression);
        }
        if (filter.category() != null) {
            System.out.println(filter.category());
            predicates.add(product.category.likeIgnoreCase("%" + filter.category() + "%"));
        }
        if (filter.brand() != null) {
            predicates.add(product.brand.likeIgnoreCase("%" + filter.brand() + "%"));
        }
        if (filter.minRating() != null) {
            predicates.add(product.rating.goe(filter.minRating()));
        }
        if (filter.maxRating() != null) {
            predicates.add(product.rating.loe(filter.maxRating()));
        }
        if (filter.minPrice() != null) {
            predicates.add(product.price.goe(filter.minPrice()));
        }
        if (filter.maxPrice() != null) {
            predicates.add(product.price.loe(filter.maxPrice()));
        }
        if (filter.minDiscount() != null) {
            predicates.add(product.discountPercentage.goe(filter.minDiscount()));
        }
        if (filter.maxDiscount() != null) {
            predicates.add(product.discountPercentage.loe(filter.maxDiscount()));
        }

        return new JPAQueryFactory(entityManager)
                .selectFrom(product)
                .where(predicates.toArray(new Predicate[0]))
                .fetch();
    }
}
