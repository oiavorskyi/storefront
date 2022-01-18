package com.jugglinhats.hexagonal.storefront.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorefrontService {
    Flux<Product> queryProductsByTag(Tag tag);
    Mono<Product> getProductDetails(String productId);
}
