package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.RecipeIngredientResponseDTO;
import com.davelooper.backend.entities.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {IngredientMapper.class})
public interface RecipeIngredientMapper {

  @Mapping(target = "unitName", source = "unit.name")
  RecipeIngredientResponseDTO toResponse(RecipeIngredient entity);
}
