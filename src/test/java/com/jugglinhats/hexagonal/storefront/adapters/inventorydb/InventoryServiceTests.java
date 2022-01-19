package com.jugglinhats.hexagonal.storefront.adapters.inventorydb;


import com.jugglinhats.hexagonal.storefront.adapters.productdb.ProductRepository;
import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {
    InventoryService service;

    @Mock(lenient = true)
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        // overwrite default answers for reactive types
        when(productRepository.getInventoryForProductWithId(any())).thenReturn(Mono.empty());

        service = new DefaultInventoryService(productRepository);
    }

    @Test
    void retrievesInventoryForExistingInStockProduct() {
        given(productRepository.getInventoryForProductWithId("existingProductId"))
                .willReturn(Mono.just(15));

        service.getInventoryFor("existingProductId")
                .as(StepVerifier::create)
                .expectNext(InventoryAvailability.IN_STOCK)
                .verifyComplete();
    }

    @Test
    void retrievesInventoryForExistingOutOfStockProduct() {
        given(productRepository.getInventoryForProductWithId("existingProductId"))
                .willReturn(Mono.just(0));

        service.getInventoryFor("existingProductId")
                .as(StepVerifier::create)
                .expectNext(InventoryAvailability.OUT_OF_STOCK)
                .verifyComplete();
    }

    @Test
    void retrievesInventoryForUnknownProduct() {
        given(productRepository.getInventoryForProductWithId("unknownProductId"))
                .willReturn(Mono.empty());

        service.getInventoryFor("unknownProductId")
                .as(StepVerifier::create)
                .verifyComplete();
    }

}