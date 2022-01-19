package com.jugglinhats.hexagonal.storefront.core;

import reactor.core.publisher.Mono;

public interface InventoryService {

    Mono<InventoryAvailability> getInventoryFor(String productId);

}
