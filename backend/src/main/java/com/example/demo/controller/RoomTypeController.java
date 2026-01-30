package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRoomTypes() {
        List<RoomTypeEntity> roomTypes = roomTypeService.getAllRoomType();
        return ResponseEntity.ok(new ApiResponse("Room types retrieved successfully", roomTypes));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRoomType(@RequestBody RoomTypeEntity roomType) {
        RoomTypeEntity saved = roomTypeService.saveRoomType(roomType);
        return ResponseEntity.ok(new ApiResponse("Room type created successfully", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRoomType(@PathVariable Integer id, @RequestBody RoomTypeEntity roomType) {
        roomType.setTypeId(id);
        RoomTypeEntity updated = roomTypeService.saveRoomType(roomType);
        return ResponseEntity.ok(new ApiResponse("Room type updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.ok(new ApiResponse("Room type deleted successfully", null));
    }
}
