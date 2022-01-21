package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

public record Product(String id,
                      String name,
                      String description,
                      LocalDate dateAdded,
                      InventoryAvailability availability) {
}
