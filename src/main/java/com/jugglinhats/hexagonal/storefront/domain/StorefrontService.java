package com.jugglinhats.hexagonal.storefront.domain;

import reactor.core.publisher.Flux;

public interface StorefrontService {
    Flux<Product> queryProductsByTag(Tag tag);
}
