package com.jugglinhats.hexagonal.storefront.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.jugglinhats.hexagonal.storefront.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
class ProductRepositoryTests {
    @Autowired
    R2dbcEntityTemplate template;
    DatabaseClient dbClient;

    @Value("classpath:/schema.sql")
    Resource schemaSql;
    @Value("classpath:/test-data.sql")
    Resource testDataSql;

    @Autowired
    ProductRepository repository;

    @BeforeEach
    void setup() throws IOException {
        dbClient = template.getDatabaseClient();
        executeSqlLoadedFrom(schemaSql);
        executeSqlLoadedFrom(testDataSql);
    }

    @Test
    void findsProductsByTag() {
        var telecaster = new Product(
                "f32e5442-2610-4d0c-a266-1dfc646c9d96",
                "Fender American Professional II Telecaster Deluxe",
                "A New Spin on the American Professional Telecaster Deluxe"
        );
        var stratocaster = new Product(
                "48c1ccf1-ee72-4b90-82f1-e07390578c96",
                "Squier Classic Vibe Stratocaster",
                "The Stratocaster Never Goes Out of Style"
        );

        repository.findByTag("electric guitar")
                .as(StepVerifier::create)
                .assertNext(p -> assertThat(p).isEqualTo(telecaster))
                .assertNext(p -> assertThat(p).isEqualTo(stratocaster))
                .verifyComplete();

        repository.findByTag("unknown")
                .as(StepVerifier::create)
                .expectNextCount(0)
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