package com.jugglinhats.hexagonal.storefront.dao;

import com.jugglinhats.hexagonal.storefront.domain.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

    @Query("SELECT P.ID, P.NAME, P.DESCRIPTION FROM PRODUCT_TAG PT JOIN PRODUCT P on P.ID = PT.PRODUCT_ID WHERE PT.TAG = :tag")
    Flux<Product> findByTag(String tag);

    @Query("SELECT INVENTORY FROM PRODUCT_INVENTORY WHERE PRODUCT_ID = :productId")
    Mono<Integer> getInventoryForProductWithId(String productId);
}
