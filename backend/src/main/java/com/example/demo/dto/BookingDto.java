package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for booking response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer bookingId;
    private Integer guestId;
    private Integer roomNumber;
    private Date checkinDate;
    private Date checkoutDate;
    private Double money;
    private String guestNotes;
}
