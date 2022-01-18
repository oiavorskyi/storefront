package com.jugglinhats.hexagonal.storefront.domain;

import java.time.LocalDate;

import com.jugglinhats.hexagonal.storefront.dao.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StorefrontServiceTests {

    StorefrontService service;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        service = new DefaultStorefrontService(productRepository);
    }

    @Test
    void queriesProductsByTag() {
        var productA = new Product("productAId", "productAName", "productADescription", LocalDate.now());
        var productB = new Product("productBId", "productBName", "productBDescription", LocalDate.now());
        var someTag = Tag.of("some tag");

        given(productRepository.findByTag(someTag.name()))
                .willReturn(Flux.just(productA, productB));

        service.queryProductsByTag(someTag)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> assertThat(products)
                        .containsExactlyInAnyOrder(productA, productB))
                .verifyComplete();
    }

    @Test
    void returnsProductDetails() {
        var productId = "productId";
        var product = new Product(productId, "productName", "productDescription", LocalDate.now());

        given(productRepository.findById(productId))
                .willReturn(Mono.just(product));

        service.getProductDetails(productId)
                .as(StepVerifier::create)
                .assertNext(p -> assertThat(p).isEqualTo(product))
                .verifyComplete();
    }

}