package com.example.MasakCuyyV2.service;

import com.example.MasakCuyyV2.model.Favorite;
import com.example.MasakCuyyV2.model.Recipe;
import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user);
    }

    public Optional<Favorite> getFavoriteByUserAndRecipe(User user, Recipe recipe) {
        return favoriteRepository.findByUserAndRecipe(user, recipe);
    }

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}