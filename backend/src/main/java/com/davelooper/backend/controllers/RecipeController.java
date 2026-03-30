package com.davelooper.backend.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.davelooper.backend.dtos.RecipeCreateRequestDTO;
import com.davelooper.backend.dtos.RecipeFullResponseDTO;
import com.davelooper.backend.dtos.RecipeSummaryResponseDTO;
import com.davelooper.backend.services.RecipeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/latest")
    public List<RecipeSummaryResponseDTO> getLatest(@RequestParam(defaultValue = "10") int limit) {
        return recipeService.getLatest(limit);
    }

    @GetMapping("/{id}")
    public RecipeFullResponseDTO getById(@PathVariable Long id) {
        return recipeService.getById(id);
    }

    @PostMapping(consumes = "multipart/form-data")
    public RecipeFullResponseDTO create(
        @RequestPart("data") @Valid RecipeCreateRequestDTO request,
        @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return recipeService.createOne(request, imageFile);
    }
}