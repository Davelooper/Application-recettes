package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.RecipeStep;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

  List<RecipeStep> findByRecipeIdOrderByStepNumberAsc(Long recipeId);
}
