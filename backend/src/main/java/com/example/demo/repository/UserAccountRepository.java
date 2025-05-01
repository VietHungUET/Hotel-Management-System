package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    @Query("SELECT u FROM UserAccount u WHERE u.userName = ?1")
    Optional<UserAccount> findByUserName(String userName);
}