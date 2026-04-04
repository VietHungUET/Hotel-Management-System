package com.example.demo.tools;

import com.example.demo.service.RoomService;
import com.example.demo.service.HotelService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomTools {

    private final RoomService roomService;
    private final HotelService hotelService;

    @Tool("Get all available rooms for the current user's hotel. The userId parameter is already provided in the system context - use it directly without asking the user. Returns a list of room types with capacity, pricing and available room numbers.")
    public String getAvailableRooms(int userId) {
        Integer hotelId = hotelService.getHotelIdByUserId(userId);
        if (hotelId == null) {
            return "No hotel found for this user";
        }

        var availableRooms = roomService.getAvailableRooms(hotelId);

        if (availableRooms.isEmpty()) {
            return "No available rooms at the moment";
        }

        return availableRooms.stream()
                .map(room -> String.format("Room Type: %s, Capacity: %d, Rate: %.0f VND/night, Available Rooms: %s",
                        room.getType(),
                        room.getCapacity(),
                        room.getNightRate(),
                        room.getListRoomNumber()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Get detailed information about ALL rooms in the current user's hotel, including room number, type, status (ACTIVE/INACTIVE) and pricing. The userId parameter is already in the system context - use it directly without asking the user.")
    public String getAllRoomsInfo(int userId) {
        Integer hotelId = hotelService.getHotelIdByUserId(userId);
        if (hotelId == null) {
            return "No hotel found for this user";
        }

        var rooms = roomService.getRoomListWithDetails(hotelId);

        if (rooms.isEmpty()) {
            return "No rooms found";
        }

        return rooms.stream()
                .map(room -> String.format("Room #%s - Type: %s, Status: %s, Rate: %.0f VND, Capacity: %d",
                        room.getRoomNumber(),
                        room.getType(),
                        room.getStatus(),
                        room.getNightRate(),
                        room.getMaximumCapacity()))
                .collect(Collectors.joining("\n"));
    }

    @Tool("Count and summarize rooms by their current status (Available vs Unavailable) in the current user's hotel. The userId parameter is already in the system context - use it directly without asking the user.")
    public String countRoomsByStatus(int userId) {
        Integer hotelId = hotelService.getHotelIdByUserId(userId);
        if (hotelId == null) {
            return "No hotel found for this user";
        }

        var rooms = roomService.getAllByHotelId(hotelId);

        long activeRooms = rooms.stream()
                .filter(r -> "Available".equalsIgnoreCase(r.getStatus()))
                .count();

        long unavailableRooms = rooms.stream()
                .filter(r -> "Unavailable".equalsIgnoreCase(r.getStatus()))
                .count();

        return String.format("Total rooms: %d\nAvailable: %d\nUnavailable: %d",
                rooms.size(), activeRooms, unavailableRooms);
    }
}