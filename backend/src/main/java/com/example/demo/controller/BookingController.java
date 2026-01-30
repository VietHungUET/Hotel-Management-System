package com.example.demo.controller;

import com.example.demo.request.UpdateBookingRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.service.IBookingService;
import com.example.demo.service.IPaymentService;
import com.example.demo.service.IRoomService;
import com.example.demo.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final RoomTypeService roomTypeService;
    private final IPaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBooking() {
        Iterable<BookingEntity> bookings = bookingService.getAllBooking();
        List<BookingDto> bookingDtos = bookingService.getConvertedBookings((List<BookingEntity>) bookings);
        return ResponseEntity.ok(new ApiResponse("Bookings retrieved successfully", bookingDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBookingById(@PathVariable int id) {
        BookingEntity booking = bookingService.getBookingById(id);
        BookingDto bookingDto = bookingService.convertToDto(booking);
        return ResponseEntity.ok(new ApiResponse("Booking retrieved successfully", bookingDto));
    }

    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<ApiResponse> getBookingByRoomNumber(@PathVariable String roomNumber) {
        BookingEntity booking = bookingService.getBookingByRoomNumber(roomNumber);
        BookingDto bookingDto = bookingService.convertToDto(booking);
        return ResponseEntity.ok(new ApiResponse("Booking retrieved successfully", bookingDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addBooking(@RequestBody BookingEntity booking) {
        BookingEntity savedBooking = bookingService.addBooking(booking);
        BookingDto bookingDto = bookingService.convertToDto(savedBooking);
        return ResponseEntity.ok(new ApiResponse("Booking created successfully", bookingDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBooking(@PathVariable int id, @RequestBody UpdateBookingRequest request) {
        BookingEntity updatedBooking = bookingService.updateBooking(id, request);
        BookingDto bookingDto = bookingService.convertToDto(updatedBooking);
        return ResponseEntity.ok(new ApiResponse("Booking updated successfully", bookingDto));
    }

    @PostMapping("/calculate-price")
    public ResponseEntity<ApiResponse> calculateBookingPrice(@RequestBody BookingEntity booking,
            @RequestParam int amount) {
        int roomNumber = booking.getRoomNumber();
        RoomEntity room = roomService.getRoom(roomNumber);
        Integer roomTypeId = room.getTypeId();
        RoomTypeEntity roomType = roomTypeService.getRoomTypeById(roomTypeId);
        Long price = bookingService.calculateBookingPrice(booking, room, roomType, amount);
        return ResponseEntity.ok(new ApiResponse("Price calculated successfully", price));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingBookings() {
        List<BookingEntity> pendingBookings = bookingService.getPendingBookings();
        List<BookingDto> bookingDtos = bookingService.getConvertedBookings(pendingBookings);
        return ResponseEntity.ok(new ApiResponse("Pending bookings retrieved successfully", bookingDtos));
    }

    @PostMapping("/payments/process")
    public ResponseEntity<ApiResponse> processPaymentsFromBookings(@RequestBody List<String> bookingIds) {
        List<PaymentEntity> payments = paymentService.processPaymentsFromBookings(bookingIds);
        return ResponseEntity.ok(new ApiResponse("Payments processed successfully", payments));
    }
}
