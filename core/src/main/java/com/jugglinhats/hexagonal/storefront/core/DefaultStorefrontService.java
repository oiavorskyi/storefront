package com.jugglinhats.hexagonal.storefront.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class DefaultStorefrontService implements StorefrontService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public DefaultStorefrontService(ProductRepository productRepository, InventoryService inventoryService) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public Flux<ProductSummary> queryProductsByTag(Tag tag) {
        return productRepository.findByTag(tag.name());
    }

    @Override
    public Mono<Product> getProductDetails(String productId) {
        return productRepository.findById(productId)
                .flatMap(p -> inventoryService.getInventoryFor(p.id())
                        .map(ia -> CoreMapper.INSTANCE.productFromDetailsAndAvailability(p, ia)));
    }
}
