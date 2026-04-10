package com.davelooper.backend.services;

import com.davelooper.backend.dtos.RecipeCreateRequestDTO;
import com.davelooper.backend.dtos.RecipeFullResponseDTO;
import com.davelooper.backend.dtos.RecipeSummaryResponseDTO;
import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.RecipeIngredient;
import com.davelooper.backend.entities.RecipeStep;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.mappers.RecipeMapper;
import com.davelooper.backend.repositories.IngredientRepository;
import com.davelooper.backend.repositories.RecipeRepository;
import com.davelooper.backend.repositories.UnitRepository;
import com.davelooper.backend.repositories.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecipeService {

  private final RecipeRepository recipeRepository;
  private final UserRepository userRepository;
  private final IngredientRepository ingredientRepository;
  private final UnitRepository unitRepository;
  private final RecipeMapper recipeMapper;

  public RecipeFullResponseDTO getById(Long id) {
    return recipeRepository
        .findById(id)
        .map(recipeMapper::toFullResponseDTO)
        .orElseThrow(() -> new NoSuchElementException("Recipe not found with id: " + id));
  }

  public List<RecipeSummaryResponseDTO> getLatest(int limit) {
    PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
    return recipeRepository.findAll(pageRequest).getContent().stream()
        .map(recipeMapper::toSummaryResponseDTO)
        .toList();
  }

  @Transactional
  public RecipeFullResponseDTO createOne(RecipeCreateRequestDTO request, MultipartFile imageFile) {
    User author =
        userRepository
            .findById(request.authorId())
            .orElseThrow(
                () -> new NoSuchElementException("User not found with id: " + request.authorId()));

    Recipe recipe = recipeMapper.toEntity(request);
    recipe.setUser(author);

    // Gestion de l'image (exemple : stockage local, à adapter selon besoin)
    if (imageFile != null && !imageFile.isEmpty()) {
      // TODO: enregistrer l'image et stocker l'URL dans recipe.setImageUrl(...)
      recipe.setImageUrl("/images/" + imageFile.getOriginalFilename());
    }

    if (request.ingredients() != null) {
      List<RecipeIngredient> recipeIngredients =
          request.ingredients().stream()
              .map(
                  dto ->
                      RecipeIngredient.builder()
                          .recipe(recipe)
                          .ingredient(
                              ingredientRepository
                                  .findById(dto.ingredientId())
                                  .orElseThrow(
                                      () ->
                                          new NoSuchElementException(
                                              "Ingredient not found with id: "
                                                  + dto.ingredientId())))
                          .unit(
                              unitRepository
                                  .findById(dto.unitId())
                                  .orElseThrow(
                                      () ->
                                          new NoSuchElementException(
                                              "Unit not found with id: " + dto.unitId())))
                          .quantity(dto.quantity())
                          .build())
              .toList();
      recipe.setRecipeIngredients(recipeIngredients);
    }

    if (request.steps() != null) {
      List<RecipeStep> recipeSteps =
          request.steps().stream()
              .map(
                  dto ->
                      RecipeStep.builder()
                          .recipe(recipe)
                          .stepNumber(dto.stepNumber())
                          .description(dto.description())
                          .build())
              .toList();
      recipe.setRecipeSteps(recipeSteps);
    }

    Recipe savedRecipe = recipeRepository.save(recipe);
    return recipeMapper.toFullResponseDTO(savedRecipe);
  }
}
