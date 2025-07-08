package com.example.demo.service;

import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.repository.RoomTypeRepository;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    public List<RoomTypeEntity> getAllRoomType() {
        return roomTypeRepository.findAll();
    }

    public RoomTypeEntity getRoomTypeById(int roomTypeId) {
        return roomTypeRepository.findById(roomTypeId).orElse(null);
    }

    public RoomTypeEntity saveRoomType(RoomTypeEntity roomType) {
        roomTypeRepository.save(roomType);
        return roomType;
    }

    public RoomTypeEntity updateRoomType(RoomTypeEntity roomType) {
        return roomTypeRepository.save(roomType);
    }

    public String deleteRoomType(Integer id) {
        roomTypeRepository.deleteById(id);
        return "Delete Success";
    }
}
