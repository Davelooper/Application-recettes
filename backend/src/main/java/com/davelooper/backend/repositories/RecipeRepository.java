package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  List<Recipe> findByUserId(Long userId);

  List<Recipe> findByTitleContainingIgnoreCase(String title);
}
