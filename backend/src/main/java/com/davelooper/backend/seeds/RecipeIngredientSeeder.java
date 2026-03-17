package com.davelooper.backend.seeds;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.RecipeIngredient;
import com.davelooper.backend.entities.Unit;
import com.davelooper.backend.repositories.IngredientRepository;
import com.davelooper.backend.repositories.RecipeIngredientRepository;
import com.davelooper.backend.repositories.RecipeRepository;
import com.davelooper.backend.repositories.UnitRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class RecipeIngredientSeeder implements Seeder {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    private final Faker faker = new Faker(new Locale("fr"));

    @Override
    public void seed() {
        if (recipeIngredientRepository.count() == 0) {
            System.out.println(">> Seeding Recipe Ingredients...");

            List<Recipe> recipes = recipeRepository.findAll();
            List<Ingredient> allIngredients = ingredientRepository.findAll();
            List<Unit> units = unitRepository.findAll();

            if (recipes.isEmpty() || allIngredients.isEmpty()) {
                System.out.println(">> Warning: Missing recipes or ingredients to seed recipe_ingredients. Skipping.");
                return;
            }

            recipes.forEach(recipe -> {
                // Pour chaque recette, on choisit entre 3 et 8 ingrédients
                int nbIngredients = faker.random().nextInt(3, 9);
                
                // On mélange la liste des ingrédients pour en prendre au hasard sans doublon pour cette recette
                Collections.shuffle(allIngredients);
                List<Ingredient> recipeIngredients = allIngredients.subList(0, nbIngredients);

                recipeIngredients.forEach(ingredient -> {
                    // Choisir une unité au hasard (peut être null pour certains cas, mais on va en forcer une pour l'exemple)
                    Unit randomUnit = units.get(faker.random().nextInt(units.size()));
                    
                    // Générer une quantité aléatoire (ex: entre 1 et 500)
                    // On arrondit pour avoir des quantités réalistes, et on convertit en BigDecimal
                    int randomQty = faker.random().nextInt(1, 500);
                    
                    // Si c'est une "pincée", "gousse" etc, on limite la quantité (ex: max 3)
                    String unitName = randomUnit.getName().toLowerCase();
                    if (unitName.contains("pincée") || unitName.contains("gousse") || unitName.contains("cuillère")) {
                        randomQty = faker.random().nextInt(1, 4);
                    } else if (unitName.contains("kg") || unitName.contains("l")) {
                        randomQty = faker.random().nextInt(1, 3);
                    }

                    RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                            .recipe(recipe)
                            .ingredient(ingredient)
                            .unit(randomUnit)
                            .quantity(BigDecimal.valueOf(randomQty))
                            .build();

                    recipeIngredientRepository.save(recipeIngredient);
                });
            });
        }
    }

    @Override
    public int getOrder() {
        return 5; // Doit être exécuté en dernier (après Recipe, Ingredient et Unit)
    }
}
