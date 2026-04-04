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

    @Tool("Get all bookings in the current hotel system. Returns a list of bookings with guest name, room number, check-in/check-out dates and total price.")
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

    @Tool("Get bookings that have NOT been paid yet (no payment record exists). Returns booking ID, room number, guest name and expected check-out date.")
    public String getPendingBookings() {
        var pendingBookings = bookingService.getPendingBookings();

        if (pendingBookings.isEmpty()) {
            return "No pending bookings";
        }

        return bookingService.getConvertedBookings(pendingBookings).stream()
                .map(booking -> String.format("Booking #%d - Room: %d, Guest: %s, Check-out: %s",
                        booking.getBookingId(),
                        booking.getRoomNumber(),
                        getGuestName(booking.getGuestId()),
                        booking.getCheckoutDate()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Get full details of a specific booking by its booking ID. Use this when the user asks about a specific booking or mentions a booking number. The bookingId parameter must be taken from the user's message (e.g. 'booking #5').")
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