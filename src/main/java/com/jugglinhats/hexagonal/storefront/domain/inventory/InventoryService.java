package com.jugglinhats.hexagonal.storefront.domain.inventory;

import com.jugglinhats.hexagonal.storefront.domain.product.InventoryAvailability;
import reactor.core.publisher.Mono;

public interface InventoryService {

    Mono<InventoryAvailability> getInventoryFor(String productId);

}
