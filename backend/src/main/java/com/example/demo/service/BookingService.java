package com.example.demo.service;

import com.example.demo.request.UpdateBookingRequest;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Iterable<BookingEntity> getAllBooking() {
        return bookingRepository.findAll();
    }

    @Override
    public BookingEntity addBooking(BookingEntity booking) {
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public BookingEntity getBookingById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));
    }

    @Override
    public BookingEntity getBookingByRoomNumber(String roomNumber) {
        BookingEntity booking = bookingRepository.findByRoomNumber(roomNumber);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking", "roomNumber", roomNumber);
        }
        return booking;
    }

    @Override
    public BookingEntity updateBooking(int id, UpdateBookingRequest request) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));

        if (request.getGuestId() != null) {
            booking.setGuestId(request.getGuestId());
        }
        if (request.getRoomNumber() != null) {
            booking.setRoomNumber(request.getRoomNumber());
        }
        if (request.getCheckinDate() != null) {
            booking.setCheckinDate(request.getCheckinDate());
        }
        if (request.getCheckoutDate() != null) {
            booking.setCheckoutDate(request.getCheckoutDate());
        }
        if (request.getMoney() != null) {
            booking.setMoney(request.getMoney());
        }

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Long calculateBookingPrice(BookingEntity booking, RoomEntity room, RoomTypeEntity roomType, int amount) {
        Date checkinDate = booking.getCheckinDate();
        Date checkoutDate = booking.getCheckoutDate();
        long checkinTime = checkinDate.getTime();
        long checkoutTime = checkoutDate.getTime();

        long timeDifference = checkoutTime - checkinTime;
        long daysDifference = timeDifference / (24 * 60 * 60 * 1000);

        String statusRoom = room.getStatus();
        if ("INACTIVE".equalsIgnoreCase(statusRoom)) {
            return -1L;
        }

        Double price = roomType.getNightRate();
        Double total = price * daysDifference;
        booking.setMoney(total);

        return (long) (price * daysDifference * amount);
    }

    @Override
    public List<BookingEntity> getPendingBookings() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        List<BookingEntity> allBookings = (List<BookingEntity>) bookingRepository.findAll();
        List<BookingEntity> pendingBookings = new ArrayList<>();

        for (BookingEntity booking : allBookings) {
            Integer bookingId = booking.getBookingId();
            boolean isPending = true;

            for (PaymentEntity payment : payments) {
                if (bookingId.equals(payment.getBookingId())) {
                    isPending = false;
                    break;
                }
            }

            if (isPending) {
                pendingBookings.add(booking);
            }
        }

        return pendingBookings;
    }

    @Override
    public BookingDto convertToDto(BookingEntity booking) {
        return new BookingDto(
                booking.getBookingId(),
                booking.getGuestId(),
                booking.getRoomNumber(),
                booking.getCheckinDate(),
                booking.getCheckoutDate(),
                booking.getMoney(),
                booking.getGuestNotes());
    }

    @Override
    public List<BookingDto> getConvertedBookings(List<BookingEntity> bookings) {
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
