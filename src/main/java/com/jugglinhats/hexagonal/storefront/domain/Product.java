package com.jugglinhats.hexagonal.storefront.domain;

import org.springframework.data.annotation.Id;

public record Product(@Id String id, String name, String description) {
}
