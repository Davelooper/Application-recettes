package com.davelooper.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    // Un commentaire pour voir ce qu'il se passe desous le capot de la voiture
    @GetMapping("/")
    public String home() {
        return "Bienvenue dans l'application de recettes !";
    }
}
