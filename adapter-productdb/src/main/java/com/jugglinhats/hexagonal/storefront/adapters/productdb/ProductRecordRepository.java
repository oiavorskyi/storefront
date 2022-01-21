package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

interface ProductRecordRepository extends ReactiveCrudRepository<ProductRecord, String> {

    @Query("SELECT P.ID, P.NAME, P.DESCRIPTION FROM PRODUCT_TAG PT JOIN PRODUCT P on P.ID = PT.PRODUCT_ID WHERE PT.TAG = :tag")
    Flux<ProductRecord> findByTag(String tag);

}
