package com.jugglinhats.hexagonal.storefront.domain;


import com.jugglinhats.hexagonal.storefront.dao.ProductRepository;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

@Service
public class DefaultStorefrontService implements StorefrontService {

    private final ProductRepository productRepository;

    public DefaultStorefrontService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> queryProductsByTag(Tag tag) {
        return productRepository.findByTag(tag.name());
    }
}
