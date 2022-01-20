package com.jugglinhats.hexagonal.storefront.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Flux<Product> findByTag(String tag);
    Mono<Product> findById(String productId);
}
