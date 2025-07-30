// src/main/java/com/example/MasakCuyyV2.service/RecipeService.java
package com.example.MasakCuyyV2.service;

import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipesByUser(User user) {
        return recipeRepository.findByUser(user);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // --- NEW: Untuk Dashboard Publik ---
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll(); 
    }

    public List<Recipe> searchRecipes(String keyword) {
        // Jika keyword kosong atau null, kembalikan semua resep
        if (keyword == null || keyword.trim().isEmpty()) {
            return recipeRepository.findAll();
        }
        return recipeRepository.searchRecipes(keyword);
    }

    // NEW: Method untuk pencarian resep milik user
    public List<Recipe> searchRecipesByUser(User user, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return recipeRepository.findByUser(user);
        }
        return recipeRepository.searchRecipesByUser(user, keyword);
    }
}