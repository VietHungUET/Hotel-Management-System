package com.example.demo.tools;

import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.GuestEntity;
import com.example.demo.service.BookingService;
import com.example.demo.service.GuestService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class BookingTools {

    private final BookingService bookingService;
    private final GuestService guestService;

    private String getGuestName(Integer guestId) {
        if (guestId == null)
            return "Unknown";
        GuestEntity guest = guestService.getGuestById(guestId);
        return guest != null ? guest.getFirstName() + " " + guest.getLastName() : "Unknown";
    }

    @Tool("Get all current bookings. Returns list of booking information including guest, room, and dates.")
    public String getAllBookings() {
        Iterable<BookingEntity> bookingsIterable = bookingService.getAllBooking();
        List<BookingEntity> bookings = StreamSupport.stream(bookingsIterable.spliterator(), false)
                .collect(Collectors.toList());

        return bookingService.getConvertedBookings(bookings).stream()
                .map(booking -> String.format(
                        "Booking #%d - Room: %d, Guest: %s, Check-in: %s, Check-out: %s, Price: %.0f VND",
                        booking.getBookingId(),
                        booking.getRoomNumber(),
                        getGuestName(booking.getGuestId()),
                        booking.getCheckinDate(),
                        booking.getCheckoutDate(),
                        booking.getMoney()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Get pending bookings that haven't been checked out yet")
    public String getPendingBookings() {
        var pendingBookings = bookingService.getPendingBookings();

        if (pendingBookings.isEmpty()) {
            return "No pending bookings";
        }

        return bookingService.getConvertedBookings(pendingBookings).stream()
                .map(booking -> String.format("Booking #%d - Room: %d, Guest: %s, Check-out: %s",
                        booking.getBookingId(),
                        booking.getRoomNumber(),
                        getGuestName(booking.getGuestId()), // NOTE: this was referencing existing code, but wait.
                                                            // bookingId? No, it should be guestId.
                        booking.getCheckoutDate()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Get booking information by booking ID")
    public String getBookingById(int bookingId) {
        try {
            var booking = bookingService.getBookingById(bookingId);
            var dto = bookingService.convertToDto(booking);

            return String.format("""
                    Booking ID: %d
                    Guest: %s
                    Room Number: %d
                    Check-in: %s
                    Check-out: %s
                    Total Price: %.0f VND
                    Notes: %s
                    """,
                    dto.getBookingId(),
                    getGuestName(dto.getGuestId()),
                    dto.getRoomNumber(),
                    dto.getCheckinDate(),
                    dto.getCheckoutDate(),
                    dto.getMoney(),
                    dto.getGuestNotes() != null ? dto.getGuestNotes() : "None");
        } catch (Exception e) {
            return "Booking not found with ID: " + bookingId;
        }
    }
}