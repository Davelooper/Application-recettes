package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.RecipeStepResponseDTO;
import com.davelooper.backend.entities.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecipeStepMapper {

  RecipeStepResponseDTO toResponse(RecipeStep entity);
}
