package com.jugglinhats.hexagonal.storefront.app.api;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jugglinhats.hexagonal.storefront.core.InventoryAvailability;
import com.jugglinhats.hexagonal.storefront.core.Product;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
import com.jugglinhats.hexagonal.storefront.core.StorefrontService;
import com.jugglinhats.hexagonal.storefront.core.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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
                .map(ApiMapper.INSTANCE::toDTO)
                .collectList()
                .map(products -> new ProductsQueryResponse(tag, products));
    }

    @GetMapping("/products/{productId}")
    Mono<ProductDTO> productDetails(@PathVariable String productId) {
        return storefrontService.getProductDetails(productId)
                .map(ApiMapper.INSTANCE::toDTO);
    }

    record ProductSummaryDTO(
            String id,
            String name,
            String description) {
    }

    record ProductDTO(
            String id,
            String name,
            String description,
            @JsonFormat(pattern = "MM/dd/yyyy") LocalDate dateAdded,
            InventoryAvailability availability) {
    }

    record ProductsQueryResponse(String tag_query, List<ProductSummaryDTO> products) {
    }

    @Mapper
    interface ApiMapper {
        ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

        ProductDTO toDTO(Product product);

        ProductSummaryDTO toDTO(ProductSummary productSummary);
    }

}
