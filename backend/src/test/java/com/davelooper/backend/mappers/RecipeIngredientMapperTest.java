package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.davelooper.backend.dtos.RecipeIngredientResponseDTO;
import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.RecipeIngredient;
import com.davelooper.backend.entities.Unit;

@DisplayName("Unit Tests - RecipeIngredientMapper")
class RecipeIngredientMapperTest {

    private final RecipeIngredientMapper mapper = Mappers.getMapper(RecipeIngredientMapper.class);
    private final IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);
    private final SeasonalityMapper seasonalityMapper = Mappers.getMapper(SeasonalityMapper.class);

    public RecipeIngredientMapperTest() {
        // Injection manuelle de la chaîne de dépendances
        ReflectionTestUtils.setField(ingredientMapper, "seasonalityMapper", seasonalityMapper);
        ReflectionTestUtils.setField(mapper, "ingredientMapper", ingredientMapper);
    }

    @Test
    @DisplayName("Doit transformer une Entité RecipeIngredient en RecipeIngredientResponseDTO")
    void shouldMapEntityToResponse() {
        // GIVEN
        Unit unit = Unit.builder().id(1L).name("grammes").build();
        Ingredient ingredient = Ingredient.builder()
                .id(2L)
                .name("Farine")
                .seasonalities(new HashSet<>())
                .build();
        
        RecipeIngredient entity = RecipeIngredient.builder()
                .id(10L)
                .ingredient(ingredient)
                .unit(unit)
                .quantity(new BigDecimal("250.00"))
                .build();

        // WHEN
        RecipeIngredientResponseDTO response = mapper.toResponse(entity);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(entity.getId());
        assertThat(response.quantity()).isEqualTo(entity.getQuantity());
        assertThat(response.unitName()).isEqualTo("grammes");
        assertThat(response.ingredient().name()).isEqualTo("Farine");
    }

    @Test
    @DisplayName("Doit gérer l'unité null sans erreur")
    void shouldHandleNullUnit() {
        // GIVEN
        Ingredient ingredient = Ingredient.builder().id(1L).name("Sel").build();
        RecipeIngredient entity = RecipeIngredient.builder()
                .ingredient(ingredient)
                .unit(null)
                .build();

        // WHEN
        RecipeIngredientResponseDTO response = mapper.toResponse(entity);

        // THEN
        assertThat(response.unitName()).isNull();
    }
}
