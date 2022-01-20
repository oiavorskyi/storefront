package com.jugglinhats.hexagonal.storefront.adapters.productdb;

import com.jugglinhats.hexagonal.storefront.core.ProductDetails;
import com.jugglinhats.hexagonal.storefront.core.ProductSummary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface ProductDBMapper {
    ProductDBMapper INSTANCE = Mappers.getMapper(ProductDBMapper.class);

    ProductSummary productRecordToSummary(ProductRecord record);
    ProductDetails productRecordToDetails(ProductRecord record);
}
