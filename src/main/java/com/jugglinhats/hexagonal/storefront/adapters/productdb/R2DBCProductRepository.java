package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import com.jugglinhats.hexagonal.storefront.core.ProductDetails;
import com.jugglinhats.hexagonal.storefront.core.ProductRepository;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class R2DBCProductRepository implements ProductRepository {

    private final ProductRecordRepository productRecordRepository;

    R2DBCProductRepository(ProductRecordRepository productRecordRepository) {
        this.productRecordRepository = productRecordRepository;
    }

    @Override
    public Flux<ProductSummary> findByTag(String tag) {
        return productRecordRepository.findByTag(tag)
                .map(ProductDBMapper.INSTANCE::productRecordToSummary);
    }

    @Override
    public Mono<ProductDetails> findById(String productId) {
        return productRecordRepository.findById(productId)
                .map(ProductDBMapper.INSTANCE::productRecordToDetails);
    }

}
