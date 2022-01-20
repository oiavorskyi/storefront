package com.jugglinhats.hexagonal.storefront.adapters.inventorydb;

import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.InventoryService;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

@Service
class R2DBCInventoryService implements InventoryService {

    private static final String GET_INVENTORY_SQL = "SELECT INVENTORY FROM PRODUCT_INVENTORY WHERE PRODUCT_ID = :productId";

    private final R2dbcEntityTemplate entityTemplate;

    public R2DBCInventoryService(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    @Override
    public Mono<InventoryAvailability> getInventoryFor(String productId) {
        return queryInventoryForProductWithId(productId)
                .map(this::toInventoryAvailability);
    }

    private Mono<Integer> queryInventoryForProductWithId(String productId) {
        return entityTemplate.getDatabaseClient().sql(GET_INVENTORY_SQL)
                .bind("productId", productId)
                .map((row, rowMetadata) -> row.get("inventory", Integer.class))
                .one();
    }

    private InventoryAvailability toInventoryAvailability(Integer quantity) {
        return quantity > 0 ? InventoryAvailability.IN_STOCK : InventoryAvailability.OUT_OF_STOCK;
    }
}
