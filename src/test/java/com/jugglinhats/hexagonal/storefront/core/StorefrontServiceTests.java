package com.jugglinhats.hexagonal.storefront.core;

import java.time.LocalDate;

import com.jugglinhats.hexagonal.storefront.adapters.productdb.ProductRepository;
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
            null);
    static final Product PRODUCT_B = new Product(
            "productBId",
            "productBName",
            "productBDescription",
            LocalDate.now(),
            null);

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

        given(productRepository.findByTag(someTag.name()))
                .willReturn(Flux.just(PRODUCT_A, PRODUCT_B));

        service.queryProductsByTag(someTag)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> assertThat(products)
                        .containsExactlyInAnyOrder(PRODUCT_A, PRODUCT_B))
                .verifyComplete();
    }

    @Test
    void returnsDetailsForProduct() {
        var productId = "productIdA";
        var productWithAvailability = new Product(
                PRODUCT_A.id(),
                PRODUCT_A.name(),
                PRODUCT_A.description(),
                PRODUCT_A.dateAdded(),
                InventoryAvailability.IN_STOCK);

        given(productRepository.findById(productId))
                .willReturn(Mono.just(PRODUCT_A));
        given(inventoryService.getInventoryFor(PRODUCT_A.id()))
                .willReturn(Mono.just(InventoryAvailability.IN_STOCK));

        service.getProductDetails(productId)
                .as(StepVerifier::create)
                .assertNext(p -> assertThat(p).isEqualTo(productWithAvailability))
                .verifyComplete();
    }

}