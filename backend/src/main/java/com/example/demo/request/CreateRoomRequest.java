package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new room.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {
    private String roomNumber;
    private Integer typeId;
    private String status;
}
