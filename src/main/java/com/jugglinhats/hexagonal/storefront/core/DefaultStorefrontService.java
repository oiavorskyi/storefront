package com.jugglinhats.hexagonal.storefront.core;


import com.jugglinhats.hexagonal.storefront.adapters.productdb.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
public class DefaultStorefrontService implements StorefrontService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public DefaultStorefrontService(ProductRepository productRepository, InventoryService inventoryService) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public Flux<Product> queryProductsByTag(Tag tag) {
        return productRepository.findByTag(tag.name());
    }

    @Override
    public Mono<Product> getProductDetails(String productId) {
        return productRepository.findById(productId)
                .flatMap(p -> inventoryService.getInventoryFor(p.id())
                        .map(ia -> productWithAvailability(p, ia)));
    }



    private Product productWithAvailability(Product p, InventoryAvailability availability) {
        return new Product(p.id(), p.name(), p.description(), p.dateAdded(), availability);
    }
}
