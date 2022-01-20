package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

public record Product(String id,
                      String name,
                      String description,
                      LocalDate dateAdded,
                      InventoryAvailability availability) {
    static Product fromDetailsAndAvailability(ProductDetails productDetails, InventoryAvailability availability) {
        return new Product(
                productDetails.id(),
                productDetails.name(),
                productDetails.description(),
                productDetails.dateAdded(),
                availability
        );
    }
}
