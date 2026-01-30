package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for room response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Integer roomId;
    private Integer hotelId;
    private Integer typeId;
    private String roomNumber;
    private String status;
    private String notes;
}
