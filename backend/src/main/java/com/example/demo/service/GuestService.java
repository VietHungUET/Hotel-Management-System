package com.example.demo.service;

import com.example.demo.entity.GuestEntity;
import com.example.demo.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    public Iterable<GuestEntity> getAllGuest() {
        return guestRepository.findAll();
    }

    public GuestEntity saveDetails(GuestEntity guest) {
        guestRepository.save(guest);
        return guest;
    }

    public GuestEntity getGuestById(int id) {
        return guestRepository.findById(id).orElse(null);
    }
}
