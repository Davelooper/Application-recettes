package com.davelooper.backend.seeds;

public interface Seeder {
    void seed();
    int getOrder(); // Pour définir qui passe avant qui (ex: Saison avant Ingrédient)
}