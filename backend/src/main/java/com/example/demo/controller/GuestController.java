package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.entity.GuestEntity;
import com.example.demo.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllGuests() {
        Iterable<GuestEntity> guests = guestService.getAllGuest();
        return ResponseEntity.ok(new ApiResponse("Guests retrieved successfully", guests));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createGuest(@RequestBody GuestEntity guest) {
        GuestEntity saved = guestService.saveDetails(guest);
        return ResponseEntity.ok(new ApiResponse("Guest created successfully", saved));
    }
}
