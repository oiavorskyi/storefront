package com.jugglinhats.hexagonal.storefront.adapters.inventorydb;

import com.jugglinhats.hexagonal.storefront.core.InventoryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration(proxyBeanMethods = false)
public class InventoryDBAutoConfiguration {

    @Bean
    public InventoryService inventoryService(R2dbcEntityTemplate entityTemplate) {
        return new R2DBCInventoryService(entityTemplate);
    }

}
