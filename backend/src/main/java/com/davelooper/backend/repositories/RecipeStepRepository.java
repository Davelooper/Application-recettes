package com.davelooper.backend.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.davelooper.backend.entities.RecipeStep;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

  List<RecipeStep> findByRecipeIdOrderByStepNumberAsc(Long recipeId);
}
