package com.davelooper.backend.mappers;

import com.davelooper.backend.dtos.RecipeCreateRequestDTO;
import com.davelooper.backend.dtos.RecipeFullResponseDTO;
import com.davelooper.backend.dtos.RecipeSummaryResponseDTO;
import com.davelooper.backend.dtos.RecipeUpdateRequestDTO;
import com.davelooper.backend.entities.Recipe;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {RecipeIngredientMapper.class, RecipeStepMapper.class})
public interface RecipeMapper {

  @Mapping(target = "totalTimeMinutes", expression = "java(calculateTotalTime(recipe))")
  RecipeSummaryResponseDTO toSummaryResponseDTO(Recipe recipe);

  @Mapping(target = "totalTimeMinutes", expression = "java(calculateTotalTime(recipe))")
  @Mapping(target = "authorId", source = "user.id")
  @Mapping(target = "ingredients", source = "recipeIngredients")
  @Mapping(target = "steps", source = "recipeSteps")
  RecipeFullResponseDTO toFullResponseDTO(Recipe recipe);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "imageUrl", ignore = true)
  @Mapping(target = "user", ignore = true) // Géré dans le service via l'authorId
  @Mapping(target = "recipeIngredients", ignore = true) // Géré dans le service
  @Mapping(target = "recipeSteps", ignore = true) // Géré dans le service
  Recipe toEntity(RecipeCreateRequestDTO request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "imageUrl", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "recipeIngredients", ignore = true)
  @Mapping(target = "recipeSteps", ignore = true)
  void updateEntityFromRequest(RecipeUpdateRequestDTO request, @MappingTarget Recipe recipe);

  default Integer calculateTotalTime(Recipe recipe) {
    if (recipe == null) return 0;
    int prep = (recipe.getPrepTimeMinutes() != null) ? recipe.getPrepTimeMinutes() : 0;
    int cook = (recipe.getCookTimeMinutes() != null) ? recipe.getCookTimeMinutes() : 0;
    return prep + cook;
  }
}
