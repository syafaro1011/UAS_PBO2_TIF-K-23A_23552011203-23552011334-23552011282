// src/main/java/com/example/MasakCuyyV2.config/SecurityConfig.java
package com.example.MasakCuyyV2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Izinkan akses ke Dashboard Publik, Register, Login, CSS/JS/Images, dan detail resep
                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**", "/uploads/**", "/recipes/**").permitAll()
                // Semua request lainnya memerlukan otentikasi
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/my-recipes", true) // <-- UBAH: Redirect setelah login ke "/my-recipes"
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // Tetap redirect ke dashboard publik setelah logout
                .permitAll()
            );

        http.csrf(csrf -> csrf.disable()); // Untuk kasus spesifik seperti H2 Console, atau jika tidak pakai CSRF TOKEN. Jika ingin aktifkan CSRF, tambahkan hidden field di form.
        // http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Hapus jika tidak pakai H2 Console

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}