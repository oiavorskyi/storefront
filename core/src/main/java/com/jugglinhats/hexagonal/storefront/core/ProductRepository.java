package com.jugglinhats.hexagonal.storefront.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Flux<ProductSummary> findByTag(String tag);
    Mono<ProductDetails> findById(String productId);
}
