package com.davelooper.backend.seeds;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.Seasonality;
import com.davelooper.backend.repositories.IngredientRepository;
import com.davelooper.backend.repositories.SeasonalityRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class IngredientSeeder implements Seeder {
    private final IngredientRepository repository;
    private final SeasonalityRepository seasonalityRepository;
    private final Faker faker = new Faker(new Locale("fr"));

    @Override
    public void seed() {
        if (repository.count() == 0) {
            System.out.println(">> Seeding Ingredients...");
            
            List<Seasonality> seasons = seasonalityRepository.findAll();
            
            // Liste d'ingrédients courants en français car DataFaker n'a pas une bonne trad fr pour Food
            List<String> ingredientsFr = List.of(
                "Tomate", "Oignon", "Ail", "Pomme de terre", "Carotte", "Poivron", 
                "Courgette", "Aubergine", "Champignon", "Poulet", "Bœuf", "Porc", 
                "Saumon", "Crevette", "Œuf", "Lait", "Beurre", "Crème fraîche", 
                "Fromage râpé", "Pâtes", "Riz", "Farine", "Sucre", "Huile d'olive", 
                "Vinaigre", "Moutarde", "Sel", "Poivre", "Basilic", "Thym", 
                "Ciboulette", "Persil", "Citron", "Miel", "Chocolat"
            );

            // On boucle directement sur la liste pour être sûr d'avoir des noms uniques
            ingredientsFr.forEach(ingredientName -> {
                repository.save(Ingredient.builder()
                    .name(ingredientName)
                    .imageUrl("https://loremflickr.com/320/240/food?lock=" + faker.random().nextInt(1, 1000))
                    .seasonalities(Set.of(seasons.get(faker.random().nextInt(seasons.size()))))
                    .build());
            });
        }
    }

    @Override
    public int getOrder() { return 2; }
}