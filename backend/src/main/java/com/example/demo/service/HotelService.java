package com.example.demo.service;

import com.example.demo.entity.HotelEntity;
import com.example.demo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    public Iterable<HotelEntity> getAllHotel() {
        return hotelRepository.findAll();
    }
    public HotelEntity saveDetails(HotelEntity hotel) {
        hotelRepository.save(hotel);
        return hotel;
    }
}
