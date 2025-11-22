package com.example.electronic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.electronic.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
}
