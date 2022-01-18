package com.jugglinhats.hexagonal.storefront.api;

import com.jugglinhats.hexagonal.storefront.domain.Product;
import com.jugglinhats.hexagonal.storefront.domain.StorefrontService;
import com.jugglinhats.hexagonal.storefront.domain.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

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
        var productA = new Product("productAId", "productA", "productADescription");
        var productB = new Product("productBId", "productB", "productBDescription");
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
}