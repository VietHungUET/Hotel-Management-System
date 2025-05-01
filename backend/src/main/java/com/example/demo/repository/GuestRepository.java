package com.example.demo.repository;

import com.example.demo.entity.GuestEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {
}
