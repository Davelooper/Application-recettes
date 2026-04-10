package com.davelooper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.davelooper.backend.dtos.RecipeCreateRequestDTO;
import com.davelooper.backend.dtos.RecipeFullResponseDTO;
import com.davelooper.backend.dtos.RecipeSummaryResponseDTO;
import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.Unit;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.mappers.RecipeMapper;
import com.davelooper.backend.repositories.IngredientRepository;
import com.davelooper.backend.repositories.RecipeRepository;
import com.davelooper.backend.repositories.UnitRepository;
import com.davelooper.backend.repositories.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests - RecipeService")
class RecipeServiceTest {

  @Mock private RecipeRepository recipeRepository;

  @Mock private UserRepository userRepository;

  @Mock private IngredientRepository ingredientRepository;

  @Mock private UnitRepository unitRepository;

  @Mock private RecipeMapper recipeMapper;

  @InjectMocks private RecipeService recipeService;

  @Test
  @DisplayName("Doit renvoyer une réponse complète quand la recette existe")
  void getByIdShouldReturnRecipe() {
    Recipe recipe = Recipe.builder().id(1L).title("Pasta").build();
    RecipeFullResponseDTO dto =
        new RecipeFullResponseDTO(
            1L, "Pasta", "desc", 2, 1, null, 10, 15, 25, null, 5L, List.of(), List.of());

    when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
    when(recipeMapper.toFullResponseDTO(recipe)).thenReturn(dto);

    RecipeFullResponseDTO result = recipeService.getById(1L);

    assertThat(result).isEqualTo(dto);
  }

  @Test
  @DisplayName("Doit lever une exception quand la recette n'existe pas")
  void getByIdShouldThrowWhenNotFound() {
    when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> recipeService.getById(99L))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("Recipe not found with id: 99");
  }

  @Test
  @DisplayName("Doit renvoyer les résumés triés du plus récent au plus ancien")
  void getLatestShouldMapPageContent() {
    Recipe first = Recipe.builder().id(10L).title("A").build();
    Recipe second = Recipe.builder().id(11L).title("B").build();
    RecipeSummaryResponseDTO firstDto = new RecipeSummaryResponseDTO(10L, "A", 1, 20, 2, null);
    RecipeSummaryResponseDTO secondDto = new RecipeSummaryResponseDTO(11L, "B", 2, 30, 4, null);

    PageRequest expectedRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    when(recipeRepository.findAll(expectedRequest))
        .thenReturn(new PageImpl<>(List.of(first, second)));
    when(recipeMapper.toSummaryResponseDTO(first)).thenReturn(firstDto);
    when(recipeMapper.toSummaryResponseDTO(second)).thenReturn(secondDto);

    List<RecipeSummaryResponseDTO> result = recipeService.getLatest(2);

    assertThat(result).containsExactly(firstDto, secondDto);
  }

  @Test
  @DisplayName("Doit créer et persister une recette avec ingrédients et étapes")
  void createOneShouldCreateRecipeWithRelations() {
    RecipeCreateRequestDTO request =
        new RecipeCreateRequestDTO(
            "Soup",
            "Hot",
            2,
            1,
            10,
            20,
            7L,
            List.of(
                new RecipeCreateRequestDTO.RecipeIngredientRequestDTO(
                    3L, BigDecimal.valueOf(1.5), 4L)),
            List.of(new RecipeCreateRequestDTO.RecipeStepRequestDTO(1, "Mix everything")));

    User author = User.builder().id(7L).build();
    Ingredient ingredient = Ingredient.builder().id(3L).name("Carrot").build();
    Unit unit = Unit.builder().id(4L).name("kg").build();
    Recipe mappedEntity = Recipe.builder().title("Soup").build();
    Recipe savedEntity = Recipe.builder().id(42L).title("Soup").build();
    RecipeFullResponseDTO responseDto =
        new RecipeFullResponseDTO(
            42L, "Soup", "Hot", 2, 1, null, 10, 20, 30, null, 7L, List.of(), List.of());

    when(userRepository.findById(7L)).thenReturn(Optional.of(author));
    when(recipeMapper.toEntity(request)).thenReturn(mappedEntity);
    when(ingredientRepository.findById(3L)).thenReturn(Optional.of(ingredient));
    when(unitRepository.findById(4L)).thenReturn(Optional.of(unit));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(savedEntity);
    when(recipeMapper.toFullResponseDTO(savedEntity)).thenReturn(responseDto);

    RecipeFullResponseDTO result = recipeService.createOne(request, null);

    ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
    verify(recipeRepository).save(captor.capture());
    Recipe persisted = captor.getValue();

    assertThat(result).isEqualTo(responseDto);
    assertThat(persisted.getUser()).isEqualTo(author);
    assertThat(persisted.getRecipeIngredients()).hasSize(1);
    assertThat(persisted.getRecipeIngredients().get(0).getRecipe()).isEqualTo(persisted);
    assertThat(persisted.getRecipeIngredients().get(0).getIngredient()).isEqualTo(ingredient);
    assertThat(persisted.getRecipeIngredients().get(0).getUnit()).isEqualTo(unit);
    assertThat(persisted.getRecipeSteps()).hasSize(1);
    assertThat(persisted.getRecipeSteps().get(0).getRecipe()).isEqualTo(persisted);
    assertThat(persisted.getRecipeSteps().get(0).getStepNumber()).isEqualTo(1);
  }

  @Test
  @DisplayName("Doit lever une exception quand l'auteur n'existe pas")
  void createOneShouldThrowWhenAuthorMissing() {
    RecipeCreateRequestDTO request =
        new RecipeCreateRequestDTO("Soup", "Hot", 2, 1, 10, 20, 999L, List.of(), List.of());

    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> recipeService.createOne(request, null))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("User not found with id: 999");
    verify(recipeRepository, never()).save(any(Recipe.class));
  }

  @Test
  @DisplayName("Doit lever une exception et ne pas sauvegarder si l'ingrédient est introuvable")
  void createOneShouldThrowWhenIngredientMissing() {
    RecipeCreateRequestDTO request =
        new RecipeCreateRequestDTO(
            "Soup",
            "Hot",
            2,
            1,
            10,
            20,
            7L,
            List.of(
                new RecipeCreateRequestDTO.RecipeIngredientRequestDTO(
                    55L, BigDecimal.valueOf(1.5), 4L)),
            List.of());
    User author = User.builder().id(7L).build();
    Recipe mappedEntity = Recipe.builder().title("Soup").build();

    when(userRepository.findById(7L)).thenReturn(Optional.of(author));
    when(recipeMapper.toEntity(request)).thenReturn(mappedEntity);
    when(ingredientRepository.findById(55L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> recipeService.createOne(request, null))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("Ingredient not found with id: 55");
    verify(recipeRepository, never()).save(any(Recipe.class));
  }

  @Test
  @DisplayName(
      "Doit lever une exception et ne pas sauvegarder si l'unité d'un ingrédient est introuvable")
  void createOneShouldThrowWhenIngredientUnitMissing() {
    RecipeCreateRequestDTO request =
        new RecipeCreateRequestDTO(
            "Soup",
            "Hot",
            2,
            1,
            10,
            20,
            7L,
            List.of(
                new RecipeCreateRequestDTO.RecipeIngredientRequestDTO(
                    55L, BigDecimal.valueOf(1.5), 4L)),
            List.of());
    User author = User.builder().id(7L).build();
    Recipe mappedEntity = Recipe.builder().title("Soup").build();
    Ingredient ingredient = Ingredient.builder().id(55L).build();

    when(userRepository.findById(7L)).thenReturn(Optional.of(author));
    when(recipeMapper.toEntity(request)).thenReturn(mappedEntity);
    when(ingredientRepository.findById(55L)).thenReturn(Optional.of(ingredient));
    when(unitRepository.findById(4L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> recipeService.createOne(request, null))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("Unit not found with id: 4");
    verify(recipeRepository, never()).save(any(Recipe.class));
  }
}
