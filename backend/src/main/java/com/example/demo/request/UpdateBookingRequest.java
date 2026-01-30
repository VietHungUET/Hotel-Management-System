package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for updating an existing booking.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingRequest {
    private Integer guestId;
    private Integer roomNumber;
    private Date checkinDate;
    private Date checkoutDate;
    private Double money;
}
