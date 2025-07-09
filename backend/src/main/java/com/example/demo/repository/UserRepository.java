package com.example.demo.repository;

import com.example.demo.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> { // Thay đổi này

    @Query("SELECT u FROM User u WHERE u.user_name = ?1")
    Optional<User> findByUserName(String user_name);
}