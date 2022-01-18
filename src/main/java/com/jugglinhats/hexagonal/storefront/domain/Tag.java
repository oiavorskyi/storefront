package com.jugglinhats.hexagonal.storefront.domain;

public record Tag(String name) {
    public static Tag of(String name) {
        return new Tag(name);
    }
}
