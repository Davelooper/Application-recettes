package com.davelooper.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import com.davelooper.backend.dtos.RecipeStepResponseDTO;
import com.davelooper.backend.entities.RecipeStep;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecipeStepMapper {

  RecipeStepResponseDTO toResponse(RecipeStep entity);
}

