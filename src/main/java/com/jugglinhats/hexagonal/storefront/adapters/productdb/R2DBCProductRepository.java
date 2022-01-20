package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import com.jugglinhats.hexagonal.storefront.core.Product;
import com.jugglinhats.hexagonal.storefront.core.ProductRepository;
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
    public Flux<Product> findByTag(String tag) {
        return productRecordRepository.findByTag(tag)
                .map(this::toProduct);
    }

    @Override
    public Mono<Integer> getInventoryForProductWithId(String productId) {
        return productRecordRepository.getInventoryForProductWithId(productId);
    }

    @Override
    public Mono<Product> findById(String productId) {
        return productRecordRepository.findById(productId)
                .map(this::toProduct);
    }

    private Product toProduct(ProductRecord productRecord) {
        return new Product(
                productRecord.id(),
                productRecord.name(),
                productRecord.description(),
                productRecord.dateAdded(),
                null
        );
    }
}
