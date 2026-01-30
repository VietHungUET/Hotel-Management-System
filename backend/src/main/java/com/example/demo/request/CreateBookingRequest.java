package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for creating a new booking.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private Integer guestId;
    private Integer roomNumber;
    private Date checkinDate;
    private Date checkoutDate;
}
