package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import com.jugglinhats.hexagonal.storefront.core.ProductRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration(proxyBeanMethods = false)
@EnableR2dbcRepositories
public class ProductDBAutoConfiguration {

    @Bean
    public ProductRepository productRepository(ProductRecordRepository productRecordRepository) {
        return new R2DBCProductRepository(productRecordRepository);
    }

}
