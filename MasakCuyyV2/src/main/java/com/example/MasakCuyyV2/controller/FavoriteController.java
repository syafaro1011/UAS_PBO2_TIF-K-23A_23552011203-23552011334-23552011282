package com.example.MasakCuyyV2.controller;

import com.example.MasakCuyyV2.model.Favorite;
import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.UserRepository;
import com.example.MasakCuyyV2.service.FavoriteService;
import com.example.MasakCuyyV2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeService recipeService;

// Menampilkan halaman Favorite
@GetMapping("/my-favorites")
public String showMyFavorites(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);

    if (currentUserOptional.isPresent()) {
        User currentUser = currentUserOptional.get();
        List<Favorite> favorites = favoriteService.getFavoritesByUser(currentUser);
        model.addAttribute("favorites", favorites);
    } else {
        return "redirect:/login?error";
    }
    return "my-favorites"; // Template HTML baru
}

// Menambah/Menghapus resep ke/dari favorit
@PostMapping("/recipes/{id}/toggle-favorite")
public String toggleFavorite(@PathVariable Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);

    if (!currentUserOptional.isPresent()) {
        return "redirect:/login"; // Harus login untuk favorite
    }
    User currentUser = currentUserOptional.get();

    Optional<Recipe> recipeOptional = recipeService.getRecipeById(id);
    if (!recipeOptional.isPresent()) {
        return "redirect:/recipes/" + id + "?notFound"; // Resep tidak ditemukan
    }
    Recipe recipe = recipeOptional.get();

    Optional<Favorite> existingFavorite = favoriteService.getFavoriteByUserAndRecipe(currentUser, recipe);

    if (existingFavorite.isPresent()) {
        favoriteService.deleteFavorite(existingFavorite.get()); // Hapus dari favorit
    } else {
        Favorite newFavorite = new Favorite(currentUser, recipe);
        favoriteService.saveFavorite(newFavorite); // Tambah ke favorit
    }

    // Redirect kembali ke halaman detail resep atau dashboard
    return "redirect:/recipes/" + id;
}
}
