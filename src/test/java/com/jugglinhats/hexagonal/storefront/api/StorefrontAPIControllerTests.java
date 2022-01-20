package com.jugglinhats.hexagonal.storefront.api;

import java.time.LocalDate;

import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.Product;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
import com.jugglinhats.hexagonal.storefront.core.StorefrontService;
import com.jugglinhats.hexagonal.storefront.core.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebFluxTest
class StorefrontAPIControllerTests {

    @Autowired
    WebTestClient webClient;

    @MockBean
    StorefrontService storefrontService;

    @BeforeEach
    void setup() {
        // overwrite default answers for reactive types
        when(storefrontService.queryProductsByTag(any())).thenReturn(Flux.empty());
        when(storefrontService.getProductDetails(any())).thenReturn(Mono.empty());
    }

    @Test
    void queryProductsByTagReturnsRequestedTagQuery() {
        var someTag = Tag.of("some tag");
        webClient.get().uri("/products?tag={tag}", someTag.name())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.tag_query").isEqualTo(someTag.name());
    }

    @Test
    void queryProductsByTagReturnsMultipleProducts() {
        var productA = new ProductSummary(
                "productAId",
                "productA",
                "productADescription");
        var productB = new ProductSummary(
                "productBId",
                "productB",
                "productBDescription");
        var someTag = Tag.of("some tag");

        given(storefrontService.queryProductsByTag(someTag))
                .willReturn(Flux.just(productA, productB));

        var productsJson = """
                {
                    "products": [
                        {
                            "id": "productAId",
                            "name": "productA",
                            "description": "productADescription"
                        },
                        {
                            "id": "productBId",
                            "name": "productB",
                            "description": "productBDescription"
                        }
                    ]
                }
                """;

        webClient.get().uri("/products?tag={tag}", someTag.name())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(productsJson);
    }

    @Test
    void queryProductsByTagReturnsNoProducts() {
        var missingTag = Tag.of("missing tag");

        given(storefrontService.queryProductsByTag(missingTag))
                .willReturn(Flux.empty());

        var productsJson = """
                {
                    "products": []
                }
                """;

        webClient.get().uri("/products?tag={tag}", missingTag.name())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(productsJson);
    }

    @Test
    void getProductDetailsForExistingProduct() {
        var product = new Product(
                "productId",
                "productName",
                "productDescription",
                LocalDate.of(2020, 2, 5),
                InventoryAvailability.IN_STOCK);

        given(storefrontService.getProductDetails("productId"))
                .willReturn(Mono.just(product));

        var productJson = """
                {
                    "id": "productId",
                    "name": "productName",
                    "description": "productDescription",
                    "dateAdded": "02/05/2020"
                }
                """;

        webClient.get().uri("/products/{id}", "productId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(productJson);
    }

    @Test
    void getProductDetailsReturnsInventoryAvailability() {
        var product = new Product(
                "productId",
                "productName",
                "productDescription",
                LocalDate.of(2020, 2, 5),
                InventoryAvailability.IN_STOCK);

        given(storefrontService.getProductDetails("productId"))
                .willReturn(Mono.just(product));

        var productJson = """
                {
                    "id": "productId",
                    "availability": "IN_STOCK"
                }
                """;

        webClient.get().uri("/products/{id}", "productId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(productJson);
    }

}