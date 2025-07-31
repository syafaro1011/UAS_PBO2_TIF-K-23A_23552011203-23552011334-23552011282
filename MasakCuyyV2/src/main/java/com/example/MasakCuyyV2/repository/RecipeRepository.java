package com.example.MasakCuyyV2.repository;

import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUser(User user);

    @Query("SELECT r FROM Recipe r WHERE " +
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.instructions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> searchRecipes(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recipe r WHERE r.user = :recipeUser AND (" + 
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR " + 
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR " +
           "LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR " +
           "LOWER(r.instructions) LIKE LOWER(CONCAT('%', :searchKeyword, '%')))")
    List<Recipe> searchRecipesByUser(@Param("recipeUser") User user, @Param("searchKeyword") String keyword);
}