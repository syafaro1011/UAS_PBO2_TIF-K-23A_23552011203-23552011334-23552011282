package com.example.MasakCuyyV2.service;



import com.example.MasakCuyyV2.model.User;
import com.example.MasakCuyyV2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Tidak ada role khusus, jadi gunakan ArrayList kosong

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cari user di database berdasarkan username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Buat objek UserDetails dari objek User kita
        // Perhatikan bahwa untuk saat ini kita tidak memiliki roles, jadi berikan ArrayList kosong.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // Empty list for authorities (no specific roles yet)
        );
    }
}
