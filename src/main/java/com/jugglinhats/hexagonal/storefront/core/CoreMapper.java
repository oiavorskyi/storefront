package com.jugglinhats.hexagonal.storefront.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
interface CoreMapper {
    CoreMapper INSTANCE = Mappers.getMapper(CoreMapper.class);

    Product productFromDetailsAndAvailability(ProductDetails productDetails, InventoryAvailability availability);
}
