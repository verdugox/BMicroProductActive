package com.ProductActive.ProductActive.web.mapper;

import com.ProductActive.ProductActive.entity.ProductActive;
import com.ProductActive.ProductActive.web.model.ProductActiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductActiveMapper {

    ProductActive modelToEntity(ProductActiveModel model);

    ProductActiveModel entityToModel(ProductActive event);

    @Mapping(target  = "id", ignore = true)
    void update(@MappingTarget ProductActive entity, ProductActive updateEntity);

}
