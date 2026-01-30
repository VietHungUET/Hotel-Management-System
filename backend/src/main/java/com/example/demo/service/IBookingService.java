package com.example.demo.service;

import com.example.demo.request.UpdateBookingRequest;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomTypeEntity;

import java.util.List;

/**
 * Interface for booking service operations.
 */
public interface IBookingService {

    // CRUD operations
    Iterable<BookingEntity> getAllBooking();

    BookingEntity getBookingById(int id);

    BookingEntity getBookingByRoomNumber(String roomNumber);

    BookingEntity addBooking(BookingEntity booking);

    BookingEntity updateBooking(int id, UpdateBookingRequest request);

    // Business logic
    Long calculateBookingPrice(BookingEntity booking, RoomEntity room, RoomTypeEntity roomType, int amount);

    List<BookingEntity> getPendingBookings();

    // DTO conversion
    BookingDto convertToDto(BookingEntity booking);

    List<BookingDto> getConvertedBookings(List<BookingEntity> bookings);
}
