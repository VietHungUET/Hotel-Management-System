package com.example.demo.repository;

import com.example.demo.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity,Integer> {
}
