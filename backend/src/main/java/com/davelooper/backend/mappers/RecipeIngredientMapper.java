package com.davelooper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.davelooper.backend.dtos.RecipeIngredientResponseDTO;
import com.davelooper.backend.entities.RecipeIngredient;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {IngredientMapper.class})
public interface RecipeIngredientMapper {

  @Mapping(target = "unitName", source = "unit.name")
  RecipeIngredientResponseDTO toResponse(RecipeIngredient entity);
}
