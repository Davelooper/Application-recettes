package com.davelooper.backend.seeds;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.RecipeStep;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.repositories.RecipeRepository;
import com.davelooper.backend.repositories.RecipeStepRepository;
import com.davelooper.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class RecipeSeeder implements Seeder {

    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final UserRepository userRepository;
    
    private final Faker faker = new Faker(new Locale("fr"));

    @Override
    public void seed() {
        if (recipeRepository.count() == 0) {
            System.out.println(">> Seeding Recipes and Recipe Steps...");

            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                System.out.println(">> Warning: No users found to associate with recipes. Skipping recipe seeder.");
                return;
            }

            // Liste de verbes et d'actions culinaires en français pour des étapes réalistes
            List<String> actions = List.of(
                "Émincez finement", "Faites revenir", "Découpez en dés", "Mélangez vigoureusement",
                "Faites blanchir", "Laissez mijoter", "Faites dorer", "Assaisonnez", "Faites cuire à feu doux",
                "Portez à ébullition", "Incorporez délicatement", "Préchauffez le four pour", "Épluchez",
                "Faites rôtir", "Fraisez", "Déglacez avec", "Mixez le tout avec"
            );

            // Création de 20 recettes
            IntStream.range(0, 20).forEach(i -> {
                // Choisir un utilisateur aléatoire pour la recette
                User randomUser = users.get(faker.random().nextInt(users.size()));

                // On génère un titre de plat en Français (le faker locale "fr" gère déjà bien food().dish())
                String dishName = faker.food().dish();

                Recipe recipe = Recipe.builder()
                        .user(randomUser)
                        .title(dishName)
                        .description("Une délicieuse préparation traditionnelle. " + faker.lorem().paragraph(2))
                        .servings(faker.random().nextInt(1, 8))
                        .difficulty(faker.random().nextInt(1, 5))
                        .prepTimeMinutes(faker.random().nextInt(10, 120))
                        .imageUrl("https://picsum.photos/seed/recipe" + i + "/800/600")
                        .build();

                Recipe savedRecipe = recipeRepository.save(recipe);

                // Création de 5 à 10 étapes pour cette recette
                int nbSteps = faker.random().nextInt(5, 11); // nextInt(min, maxExclusive)
                
                IntStream.rangeClosed(1, nbSteps).forEach(stepNumber -> {
                    // Création d'une phrase d'étape réaliste sans la mesure
                    String action = actions.get(faker.random().nextInt(actions.size()));
                    String ingredient = faker.food().ingredient().toLowerCase();
                    
                    String stepDescription = String.format("%s %s. %s", 
                        action, 
                        ingredient, 
                        faker.lorem().sentence()
                    );

                    RecipeStep step = RecipeStep.builder()
                            .recipe(savedRecipe)
                            .stepNumber(stepNumber)
                            .description(stepDescription)
                            .build();
                            
                    recipeStepRepository.save(step);
                });
            });
        }
    }

    @Override
    public int getOrder() {
        return 4; // Doit être exécuté APRES UserSeeder (qui a l'ordre 3) car les recettes ont besoin d'un User
    }
}
