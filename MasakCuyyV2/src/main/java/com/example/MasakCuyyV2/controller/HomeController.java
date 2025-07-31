package com.example.MasakCuyyV2.controller;

import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.UserRepository;
import com.example.MasakCuyyV2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserRepository userRepository; 

    // Update endpoint home untuk menerima parameter pencarian
    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Recipe> recipes;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Jika ada keyword, lakukan pencarian
            recipes = recipeService.searchRecipes(keyword);
            model.addAttribute("keyword", keyword); 
        } else {
            // Jika tidak ada keyword, tampilkan semua resep
            recipes = recipeService.getAllRecipes();
        }

        model.addAttribute("recipes", recipes);

        // Cek apakah user sudah login untuk menampilkan elemen UI yang relevan
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                  !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));

        if (isAuthenticated) {
            String currentUsername = authentication.getName();
            Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
            currentUserOptional.ifPresent(user -> model.addAttribute("user", user));
        }

        return "home"; 
    }
}