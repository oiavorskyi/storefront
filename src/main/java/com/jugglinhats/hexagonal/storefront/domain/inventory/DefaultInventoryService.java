package com.jugglinhats.hexagonal.storefront.domain.inventory;

import com.jugglinhats.hexagonal.storefront.dao.ProductRepository;
import com.jugglinhats.hexagonal.storefront.domain.product.InventoryAvailability;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
public class DefaultInventoryService implements InventoryService {

    private final ProductRepository productRepository;

    public DefaultInventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<InventoryAvailability> getInventoryFor(String productId) {
        return productRepository.getInventoryForProductWithId(productId)
                .map(this::toInventoryAvailability);
    }

    private InventoryAvailability toInventoryAvailability(Integer quantity) {
        return quantity > 0 ? InventoryAvailability.IN_STOCK : InventoryAvailability.OUT_OF_STOCK;
    }
}
