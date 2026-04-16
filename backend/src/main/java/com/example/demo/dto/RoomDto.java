package com.example.demo.dto;

/**
 * DTO for room response.
 */
public record RoomDto(
        Integer roomId,
        Integer hotelId,
        Integer typeId,
        String roomNumber,
        String status,
        String notes
) {}
