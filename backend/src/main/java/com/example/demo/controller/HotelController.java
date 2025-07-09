package com.example.demo.controller;

import com.example.demo.entity.HotelEntity;
import com.example.demo.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping(path = "/hotel/getAll")
    public @ResponseBody Iterable<HotelEntity> getAllHotel() {
        return hotelService.getAllHotel();
    }
//    This function will response all data about the payments which haved been payed


    //This function will set from using room to Active room

    @PostMapping(path="/hotel/add")
    public HotelEntity addHotelInfor(@RequestBody HotelEntity hotel) {
        return hotelService.saveDetails(hotel);
    }

}
