package com.example.demo.controller;

import com.example.demo.entity.GuestEntity;
import com.example.demo.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    @GetMapping(path= "/guest/getAll")
    public @ResponseBody Iterable<GuestEntity> getAllGuests() {
        return guestService.getAllGuest();
    }


    @PostMapping(path = "/guest/add")
    public GuestEntity addRoom(@RequestBody GuestEntity guest) {
        return guestService.saveDetails(guest);
    }
}
