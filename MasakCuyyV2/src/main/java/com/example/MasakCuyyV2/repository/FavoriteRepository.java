package com.example.MasakCuyyV2.repository;

import com.example.MasakCuyyV2.model.Favorite;
import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe); 
}