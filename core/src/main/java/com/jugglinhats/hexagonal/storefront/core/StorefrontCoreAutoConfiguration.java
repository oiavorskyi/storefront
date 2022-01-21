package com.jugglinhats.hexagonal.storefront.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorefrontCoreAutoConfiguration {

    @Bean
    public StorefrontService storefrontService(ProductRepository productRepository, InventoryService inventoryService) {
        return new DefaultStorefrontService(productRepository, inventoryService);
    }
}
