package com.jugglinhats.hexagonal.storefront.api;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.Product;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
import com.jugglinhats.hexagonal.storefront.core.StorefrontService;
import com.jugglinhats.hexagonal.storefront.core.Tag;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorefrontAPIController {

    private final StorefrontService storefrontService;

    public StorefrontAPIController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @GetMapping("/products")
    Mono<ProductsQueryResponse> productsQuery(@RequestParam String tag) {
        return storefrontService.queryProductsByTag(Tag.of(tag))
                .map(ProductSummaryDTO::fromProductSummary)
                .collectList()
                .map(products -> new ProductsQueryResponse(tag, products));
    }

    @GetMapping("/products/{productId}")
    Mono<ProductDTO> productDetails(@PathVariable String productId) {
        return storefrontService.getProductDetails(productId)
                .map(ProductDTO::fromProduct);
    }

    record ProductSummaryDTO(
            String id,
            String name,
            String description) {
        static ProductSummaryDTO fromProductSummary(ProductSummary productSummary) {
            return new ProductSummaryDTO(
                    productSummary.id(),
                    productSummary.name(),
                    productSummary.description()
            );
        }
    }

    record ProductDTO(
            String id,
            String name,
            String description,
            @JsonFormat(pattern = "MM/dd/yyyy") LocalDate dateAdded,
            InventoryAvailability availability) {
        static ProductDTO fromProduct(Product product) {
            return new ProductDTO(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.dateAdded(),
                    product.availability()
            );
        }
    }

    record ProductsQueryResponse(String tag_query, List<ProductSummaryDTO> products) {
    }

}
