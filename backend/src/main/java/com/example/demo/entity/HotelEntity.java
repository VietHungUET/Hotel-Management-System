package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hotel")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotelId")
    private Integer hotelId;

    private String name;
    private String address;
    private String phone;
    private String email;
    private Integer star;

    @Column(name = "checkin_time")
    private String checkinTime;

    @Column(name = "checkout_time")
    private String checkoutTime;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private User user;

}
