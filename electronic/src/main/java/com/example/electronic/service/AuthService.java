package com.example.electronic.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.electronic.model.UserAccount;
import com.example.electronic.repository.UserAccountRepository;

@Service
public class AuthService {

    private final UserAccountRepository userRepo;

    public AuthService(UserAccountRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserAccount> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserAccount register(String username, String rawPassword, String email) {
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        UserAccount u = new UserAccount(username, hashed, email);
        return userRepo.save(u);
    }

    public Optional<UserAccount> authenticate(String username, String rawPassword) {
        return userRepo.findByUsername(username)
                .filter(u -> BCrypt.checkpw(rawPassword, u.getPassword()));
    }

    public Optional<UserAccount> findById(Long id) {
        return userRepo.findById(id);
    }
}
