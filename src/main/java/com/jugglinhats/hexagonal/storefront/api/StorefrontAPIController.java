package com.jugglinhats.hexagonal.storefront.api;

import java.util.List;

import com.jugglinhats.hexagonal.storefront.core.Product;
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
                .collectList()
                .map(products -> new ProductsQueryResponse(tag, products));
    }

    @GetMapping("/products/{productId}")
    Mono<Product> productDetails(@PathVariable String productId) {
        return storefrontService.getProductDetails(productId);
    }

    record ProductsQueryResponse(String tag_query, List<Product> products) {
    }
}
