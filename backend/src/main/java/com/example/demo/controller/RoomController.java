package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ResponseAvailRoom;
import com.example.demo.entity.ResponseForListRoom;
import com.example.demo.entity.RoomEntity;
import com.example.demo.service.CustomDetails;
import com.example.demo.service.HotelService;
import com.example.demo.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRooms(@AuthenticationPrincipal CustomDetails userDetails) {
        int userId = userDetails.getUser().getId();
        Integer hotelId = hotelService.getHotelIdByUserId(userId);

        if (hotelId == null) {
            return ResponseEntity.ok(new ApiResponse("No hotel found for user", List.of()));
        }

        List<ResponseForListRoom> rooms = roomService.getRoomListWithDetails(hotelId);
        return ResponseEntity.ok(new ApiResponse("Rooms retrieved successfully", rooms));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableRooms(@AuthenticationPrincipal CustomDetails userDetails) {
        int userId = userDetails.getUser().getId();
        Integer hotelId = hotelService.getHotelIdByUserId(userId);

        if (hotelId == null) {
            return ResponseEntity.ok(new ApiResponse("No hotel found for user", List.of()));
        }

        List<ResponseAvailRoom> availableRooms = roomService.getAvailableRooms(hotelId);
        return ResponseEntity.ok(new ApiResponse("Available rooms retrieved successfully", availableRooms));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addRoom(@RequestBody RoomEntity roomEntity,
            @AuthenticationPrincipal CustomDetails userDetails) {
        int userId = userDetails.getUser().getId();
        Integer hotelId = hotelService.getHotelIdByUserId(userId);

        if (hotelId == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("No hotel found for user", null));
        }

        roomEntity.setHotelId(hotelId);
        RoomEntity savedRoom = roomService.saveDetails(roomEntity);
        RoomDto roomDto = roomService.convertToDto(savedRoom);
        return ResponseEntity.ok(new ApiResponse("Room created successfully", roomDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable Integer id) {
        String result = roomService.deleteRoom(id);
        return ResponseEntity.ok(new ApiResponse(result, null));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse> activateRoom(@PathVariable int id) {
        String result = roomService.updateStatusToActive(id);
        return ResponseEntity.ok(new ApiResponse(result, null));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateRoom(@PathVariable int id) {
        String result = roomService.updateStatusToInactive(id);
        return ResponseEntity.ok(new ApiResponse(result, null));
    }
}
