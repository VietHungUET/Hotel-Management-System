package com.example.demo.repository;

import com.example.demo.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<HotelEntity,Integer> {
}
