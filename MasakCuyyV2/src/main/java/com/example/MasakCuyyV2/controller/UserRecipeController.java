package com.example.MasakCuyyV2.controller;

import com.example.MasakCuyyV2.model.Favorite;
import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.UserRepository;
import com.example.MasakCuyyV2.service.FavoriteService;
import com.example.MasakCuyyV2.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
// @RequestMapping("/my-recipes") 
public class UserRecipeController { 

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private FavoriteService favoriteService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Menampilkan halaman "Resep Saya"
    @GetMapping("/my-recipes") 
    public String showMyRecipes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            model.addAttribute("user", currentUser);

            List<Recipe> userRecipes = recipeService.getRecipesByUser(currentUser);
            model.addAttribute("recipes", userRecipes);
            
            model.addAttribute("newRecipe", new Recipe()); 
        } else {
            return "redirect:/login?error";
        }

        return "my-recipes-dashboard"; 
    }

    // Memproses form Tambah Resep
    @PostMapping("/my-recipes/add") 
    public String addRecipe(@ModelAttribute("newRecipe") Recipe newRecipe,
                            @RequestParam("imageFile") MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            newRecipe.setUser(currentUser);

            // === CLEAN INPUT ===
            newRecipe.setIngredients(
                newRecipe.getIngredients().lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining("\n"))
            );
            newRecipe.setInstructions(
                newRecipe.getInstructions().lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining("\n"))
            );


            if (!imageFile.isEmpty()) {
                try {
                    String originalFilename = imageFile.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    Path fileNameAndPath = Paths.get(uploadDir, uniqueFileName);
                    Files.write(fileNameAndPath, imageFile.getBytes());
                    newRecipe.setImageUrl("/uploads/" + uniqueFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "redirect:/my-recipes?uploadError";
                }
            } else {
                newRecipe.setImageUrl(null); 
            }
            recipeService.saveRecipe(newRecipe);
        } else {
            return "redirect:/login?error";
        }
        return "redirect:/my-recipes"; 
    }

    // Menampilkan halaman edit resep
    @GetMapping("/my-recipes/edit/{id}") 
    public String showEditRecipeForm(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
        if (!currentUserOptional.isPresent()) { return "redirect:/login?error"; }
        User currentUser = currentUserOptional.get();

        Optional<Recipe> recipeOptional = recipeService.getRecipeById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipeToEdit = recipeOptional.get();
            if (!recipeToEdit.getUser().getId().equals(currentUser.getId())) {
                return "redirect:/my-recipes?unauthorized";
            }
            model.addAttribute("recipe", recipeToEdit);
            return "edit-recipe";
        } else {
            return "redirect:/my-recipes?recipeNotFound";
        }
    }

    // Memproses form edit resep
    @PostMapping("/my-recipes/edit/{id}") 
    public String updateRecipe(@PathVariable Long id,
                               @ModelAttribute("recipe") Recipe updatedRecipe, 
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
        if (!currentUserOptional.isPresent()) { return "redirect:/login?error"; }
        User currentUser = currentUserOptional.get();

        Optional<Recipe> existingRecipeOptional = recipeService.getRecipeById(id);
        if (existingRecipeOptional.isPresent()) {
            Recipe existingRecipe = existingRecipeOptional.get();
            if (!existingRecipe.getUser().getId().equals(currentUser.getId())) { return "redirect:/my-recipes?unauthorized"; }

            existingRecipe.setTitle(updatedRecipe.getTitle());
            existingRecipe.setDescription(updatedRecipe.getDescription());
            // === CLEAN INPUT ===
        existingRecipe.setIngredients(
            updatedRecipe.getIngredients().lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining("\n"))
        );
        existingRecipe.setInstructions(
            updatedRecipe.getInstructions().lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining("\n"))
        );

            

            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    if (existingRecipe.getImageUrl() != null && !existingRecipe.getImageUrl().isEmpty()) {
                        Path oldFilePath = Paths.get(uploadDir, existingRecipe.getImageUrl().replace("/uploads/", ""));
                        Files.deleteIfExists(oldFilePath);
                    }
                    String originalFilename = imageFile.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    Path fileNameAndPath = Paths.get(uploadDir, uniqueFileName);
                    Files.write(fileNameAndPath, imageFile.getBytes());
                    existingRecipe.setImageUrl("/uploads/" + uniqueFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "redirect:/my-recipes?uploadError";
                }
            }
            
            recipeService.saveRecipe(existingRecipe);
        } else {
            return "redirect:/my-recipes?recipeNotFound";
        }
        return "redirect:/my-recipes"; 
    }

    // Endpoint untuk Hapus Resep
    @PostMapping("/my-recipes/delete/{id}") 
    public String deleteRecipe(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
        if (!currentUserOptional.isPresent()) { return "redirect:/login?error"; }
        User currentUser = currentUserOptional.get();

        Optional<Recipe> recipeOptional = recipeService.getRecipeById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipeToDelete = recipeOptional.get();
            if (!recipeToDelete.getUser().getId().equals(currentUser.getId())) { return "redirect:/my-recipes?unauthorized"; }

            // Hapus file gambar terkait jika ada
            if (recipeToDelete.getImageUrl() != null && !recipeToDelete.getImageUrl().isEmpty()) {
                try {
                    Path filePath = Paths.get(uploadDir, recipeToDelete.getImageUrl().replace("/uploads/", ""));
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    
                }
            }
            recipeService.deleteRecipeById(id);
        } else {
            System.out.println("Attempted to delete non-existent recipe with ID: " + id);
        }
        return "redirect:/my-recipes"; 
    }

    @GetMapping("/recipes/{id}") 
    public String showRecipeDetail(@PathVariable Long id, Model model) {
        Optional<Recipe> recipeOptional = recipeService.getRecipeById(id);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            model.addAttribute("recipe", recipe);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));

            if (isAuthenticated) {
                String currentUsername = authentication.getName();
                Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();
                    model.addAttribute("isOwner", recipe.getUser().getId().equals(currentUser.getId()));

                    // NEW: Cek apakah resep ini sudah difavoritkan oleh user ini
                    Optional<Favorite> favorite = favoriteService.getFavoriteByUserAndRecipe(currentUser, recipe);
                    model.addAttribute("isFavorited", favorite.isPresent());
                }
            } else {
                model.addAttribute("isOwner", false);
                model.addAttribute("isFavorited", false); 
            }
            return "recipe-detail";
        } else {
            return "redirect:/"; 
        }
    }
}