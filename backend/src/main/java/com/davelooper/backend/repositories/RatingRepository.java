package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.Rating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

  List<Rating> findByRecipeId(Long recipeId);

  Optional<Rating> findByRecipeIdAndUserId(Long recipeId, Long userId);
}
