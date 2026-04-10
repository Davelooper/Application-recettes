package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.RecipeIngredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

  List<RecipeIngredient> findByRecipeId(Long recipeId);
}
