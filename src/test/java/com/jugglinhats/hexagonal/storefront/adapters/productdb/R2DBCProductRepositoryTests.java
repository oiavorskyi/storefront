package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.function.Consumer;

import com.jugglinhats.hexagonal.storefront.core.ProductDetails;
import com.jugglinhats.hexagonal.storefront.core.ProductRepository;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
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
import static org.assertj.core.api.Assertions.from;

@DataR2dbcTest
class R2DBCProductRepositoryTests {
    static final ProductRecord TELECASTER = new ProductRecord(
            "f32e5442-2610-4d0c-a266-1dfc646c9d96",
            "Fender American Professional II Telecaster Deluxe",
            "A New Spin on the American Professional Telecaster Deluxe",
            LocalDate.of(2020, 12, 7)
    );
    static final ProductRecord STRATOCASTER = new ProductRecord(
            "48c1ccf1-ee72-4b90-82f1-e07390578c96",
            "Squier Classic Vibe Stratocaster",
            "The Stratocaster Never Goes Out of Style",
            LocalDate.of(2018, 3, 2)
    );
    static final String NON_EXISTENT_PRODUCT_ID = "unknown";

    @Autowired
    R2dbcEntityTemplate template;
    DatabaseClient dbClient;

    @Value("classpath:/schema-product.sql")
    Resource schemaSql;
    @Value("classpath:/test-data-product.sql")
    Resource testDataSql;

    ProductRepository repository;

    @Autowired
    ProductRecordRepository productRecordRepository;


    @BeforeEach
    void setup() throws IOException {
        dbClient = template.getDatabaseClient();
        executeSqlLoadedFrom(schemaSql);
        executeSqlLoadedFrom(testDataSql);

        repository = new R2DBCProductRepository(productRecordRepository);
    }

    @Test
    void findsProductsByTag() {
        repository.findByTag("electric guitar")
                .as(StepVerifier::create)
                .assertNext(matchesSummaryOf(TELECASTER))
                .assertNext(matchesSummaryOf(STRATOCASTER))
                .verifyComplete();

        repository.findByTag(NON_EXISTENT_PRODUCT_ID)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findsProductById() {
        repository.findById(TELECASTER.id())
                .as(StepVerifier::create)
                .assertNext(matchesDetailsOf(TELECASTER))
                .verifyComplete();

        repository.findById(STRATOCASTER.id())
                .as(StepVerifier::create)
                .assertNext(matchesDetailsOf(STRATOCASTER))
                .verifyComplete();

        repository.findById(NON_EXISTENT_PRODUCT_ID)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private Consumer<ProductSummary> matchesSummaryOf(ProductRecord record) {
        return p -> assertThat(p)
                .returns(record.id(), from(ProductSummary::id))
                .returns(record.name(), from(ProductSummary::name))
                .returns(record.description(), from(ProductSummary::description));
    }

    private Consumer<ProductDetails> matchesDetailsOf(ProductRecord record) {
        return p -> assertThat(p)
                .returns(record.id(), from(ProductDetails::id))
                .returns(record.name(), from(ProductDetails::name))
                .returns(record.description(), from(ProductDetails::description))
                .returns(record.dateAdded(), from(ProductDetails::dateAdded));
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