package com.example.MasakCuyyV2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes") // Nama tabel di database
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description; // <-- NEW: Tambahkan atribut deskripsi

    @Column(columnDefinition = "LONGTEXT")
    private String ingredients;

    @Column(columnDefinition = "LONGTEXT")
    private String instructions;

    private String imageUrl;

    @ManyToOne // Banyak resep bisa dimiliki oleh satu user
    @JoinColumn(name = "user_id", nullable = false) // Kolom foreign key di tabel recipes
    private User user; // User yang membuat resep ini

    private LocalDateTime publishDate; // NEW: Tambahkan atribut publishDate

    // Constructors
    public Recipe() {
        this.publishDate = LocalDateTime.now();
    }

    // Constructor utama yang akan sering digunakan
    public Recipe(String title, String description, String ingredients, String instructions, String imageUrl, User user) {
        this.title = title;
        this.description = description; // <-- NEW: Inisialisasi deskripsi
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.user = user;
        this.publishDate = LocalDateTime.now();
    }

    // Constructor opsional untuk data dummy (jika Anda butuh kontrol publishDate manual)
    public Recipe(String title, String description, String ingredients, String instructions, String imageUrl, User user, LocalDateTime publishDate) {
        this.title = title;
        this.description = description; // <-- NEW: Inisialisasi deskripsi
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.user = user;
        this.publishDate = publishDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // NEW: Getter dan Setter untuk description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }
}