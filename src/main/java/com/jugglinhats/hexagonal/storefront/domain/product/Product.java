package com.jugglinhats.hexagonal.storefront.domain.product;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;

public record Product(
        @Id String id,
        String name,
        String description,
        @JsonFormat(pattern = "MM/dd/yyyy") LocalDate dateAdded,
        InventoryAvailability availability) {
}
