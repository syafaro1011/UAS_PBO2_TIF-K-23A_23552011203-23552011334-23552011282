// src/main/java/com/masakcuyy/masakcuyy/controller/AuthController.java
package com.example.MasakCuyyV2.controller;

import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Menampilkan halaman Register
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Objek User kosong untuk form binding
        return "register"; // Mengarah ke src/main/resources/templates/register.html
    }

    // Memproses form Register
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Cek apakah username sudah ada
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("errorMessage", "Username already exists!");
            return "register";
        }
        // Cek apakah email sudah ada
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "Email already registered!");
            return "register";
        }

        // Enkripsi password sebelum menyimpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login?registered"; // Redirect ke halaman login dengan pesan sukses
    }

    // Menampilkan halaman Login
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "registered", required = false) String registered,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please log in.");
        }
        return "login"; // Mengarah ke src/main/resources/templates/login.html
    }
}
