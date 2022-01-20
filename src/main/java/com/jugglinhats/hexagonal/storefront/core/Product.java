package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Product(
        String id,
        String name,
        String description,
        @JsonFormat(pattern = "MM/dd/yyyy") LocalDate dateAdded,
        InventoryAvailability availability) {
}
