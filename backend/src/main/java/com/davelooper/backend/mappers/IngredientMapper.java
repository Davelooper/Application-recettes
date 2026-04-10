package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.IngredientCreateRequestDTO;
import com.davelooper.backend.dtos.IngredientResponseDTO;
import com.davelooper.backend.dtos.IngredientUpdateRequestDTO;
import com.davelooper.backend.entities.Ingredient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = SeasonalityMapper.class)
public interface IngredientMapper {

  IngredientResponseDTO toResponse(Ingredient ingredient);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "imageUrl", ignore = true),
    // seasonalityIds est facultatif et doit etre resolu en entites dans le service.
    @Mapping(target = "seasonalities", ignore = true)
  })
  Ingredient toEntity(IngredientCreateRequestDTO request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "imageUrl", ignore = true),
    // Si seasonalityIds est absent du formulaire, on ne modifie pas les saisonnalites.
    @Mapping(target = "seasonalities", ignore = true)
  })
  void updateEntityFromRequest(
      IngredientUpdateRequestDTO request, @MappingTarget Ingredient ingredient);
}
