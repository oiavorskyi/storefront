package com.jugglinhats.hexagonal.storefront.adapters.inventorydb;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.util.StreamUtils;

@DataR2dbcTest
class R2DBCInventoryServiceTests {
    static final String IN_STOCK_PRODUCT_ID = "f32e5442-2610-4d0c-a266-1dfc646c9d96";
    static final String OUT_OF_STOCK_PRODUCT_ID = "48c1ccf1-ee72-4b90-82f1-e07390578c96";
    static final String NON_EXISTENT_PRODUCT_ID = "unknown";

    InventoryService service;

    @Autowired
    R2dbcEntityTemplate template;
    DatabaseClient dbClient;

    @Value("classpath:/schema-inventory.sql")
    Resource schemaSql;
    @Value("classpath:/test-data-inventory.sql")
    Resource testDataSql;

    @BeforeEach
    void setup() throws IOException {
        dbClient = template.getDatabaseClient();
        executeSqlLoadedFrom(schemaSql);
        executeSqlLoadedFrom(testDataSql);

        service = new R2DBCInventoryService(template);
    }

    @Test
    void retrievesInventoryForExistingInStockProduct() {
        service.getInventoryFor(IN_STOCK_PRODUCT_ID)
                .as(StepVerifier::create)
                .expectNext(InventoryAvailability.IN_STOCK)
                .verifyComplete();
    }

    @Test
    void retrievesInventoryForExistingOutOfStockProduct() {
        service.getInventoryFor(OUT_OF_STOCK_PRODUCT_ID)
                .as(StepVerifier::create)
                .expectNext(InventoryAvailability.OUT_OF_STOCK)
                .verifyComplete();
    }

    @Test
    void retrievesInventoryForUnknownProduct() {
        service.getInventoryFor(NON_EXISTENT_PRODUCT_ID)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    void executeSqlLoadedFrom(Resource schemaSql) throws IOException {
        dbClient.sql(loadSqlFrom(schemaSql))
                .fetch()
                .rowsUpdated()
                .block(Duration.ofSeconds(1));
    }

    String loadSqlFrom(Resource sql) throws IOException {
        return StreamUtils.copyToString(sql.getInputStream(), StandardCharsets.UTF_8);
    }

}