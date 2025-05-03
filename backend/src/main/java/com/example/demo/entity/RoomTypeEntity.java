package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room_type")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    private String name;
    private String description;

    @Column(name = "day_rate")
    private Double dayRate;
    @Column(name = "night_rate")
    private Double nightRate;
    @Column(name = "daily_rate")
    private Double dailyRate;
    @Column(name = "overtime_pay")
    private Double overtimePay;

    private int capacity;

    @Column(name ="price", nullable = false)
    private int price;
}
