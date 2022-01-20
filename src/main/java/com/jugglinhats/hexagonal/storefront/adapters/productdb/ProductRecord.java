package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("PRODUCT")
record ProductRecord(
        @Id String id,
        String name,
        String description,
        LocalDate dateAdded) {
}
