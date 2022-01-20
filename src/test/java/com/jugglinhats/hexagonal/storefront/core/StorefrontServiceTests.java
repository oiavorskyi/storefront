package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorefrontServiceTests {
    static final Product PRODUCT_A = new Product(
            "productAId",
            "productAName",
            "productADescription",
            LocalDate.now(),
            InventoryAvailability.IN_STOCK);
    static final Product PRODUCT_B = new Product(
            "productBId",
            "productBName",
            "productBDescription",
            LocalDate.now(),
            InventoryAvailability.OUT_OF_STOCK);

    StorefrontService service;

    @Mock(lenient = true)
    ProductRepository productRepository;

    @Mock(lenient = true)
    InventoryService inventoryService;

    @BeforeEach
    void setup() {
        // overwrite default answers for reactive types
        when(productRepository.findByTag(any())).thenReturn(Flux.empty());
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        when(inventoryService.getInventoryFor(any())).thenReturn(Mono.empty());

        service = new DefaultStorefrontService(productRepository, inventoryService);
    }

    @Test
    void queriesProductsByTag() {
        var someTag = Tag.of("some tag");
        var productASummary = productSummaryOf(PRODUCT_A);
        var productBSummary = productSummaryOf(PRODUCT_B);

        given(productRepository.findByTag(someTag.name()))
                .willReturn(Flux.just(productASummary, productBSummary));

        service.queryProductsByTag(someTag)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> assertThat(products)
                        .containsExactlyInAnyOrder(productASummary, productBSummary))
                .verifyComplete();
    }

    @Test
    void returnsDetailsForProduct() {
        given(productRepository.findById(PRODUCT_A.id()))
                .willReturn(Mono.just(productDetailsOf(PRODUCT_A)));

        given(inventoryService.getInventoryFor(PRODUCT_A.id()))
                .willReturn(Mono.just(InventoryAvailability.IN_STOCK));

        service.getProductDetails(PRODUCT_A.id())
                .as(StepVerifier::create)
                .assertNext(p -> assertThat(p).isEqualTo(PRODUCT_A))
                .verifyComplete();
    }

    private ProductSummary productSummaryOf(Product product) {
        return new ProductSummary(product.id(), product.name(), product.description());
    }

    private ProductDetails productDetailsOf(Product product) {
        return new ProductDetails(product.id(), product.name(), product.description(), product.dateAdded());
    }

}