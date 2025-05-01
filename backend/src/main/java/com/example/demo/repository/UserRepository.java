package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> { // Thay đổi này

    @Query("SELECT u FROM User u WHERE u.user_name = ?1")
    User findByUserName(String user_name);
}