package com.jugglinhats.hexagonal.storefront.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorefrontService {
    Flux<ProductSummary> queryProductsByTag(Tag tag);
    Mono<Product> getProductDetails(String productId);
}
