package com.davelooper.backend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {
  @GetMapping("/")
  public String home() {
    log.info("Smoke test succeeded");
    return "Bienvenue dans l'application de recettes !";
  }
}
