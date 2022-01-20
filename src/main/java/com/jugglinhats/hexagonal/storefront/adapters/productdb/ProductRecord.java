package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import java.time.LocalDate;

import com.jugglinhats.hexagonal.storefront.core.ProductDetails;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("PRODUCT")
record ProductRecord(
        @Id String id,
        String name,
        String description,
        LocalDate dateAdded) {
    ProductSummary toProductSummary() {
        return new ProductSummary(id(), name(), description());
    }

    ProductDetails toProductDetails() {
        return new ProductDetails(id(), name(), description(), dateAdded());
    }
}
