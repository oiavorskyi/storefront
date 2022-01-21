package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

public record ProductDetails(String id,
                             String name,
                             String description,
                             LocalDate dateAdded) {
}
