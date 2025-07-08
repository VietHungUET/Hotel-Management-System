package com.example.demo.controller;

import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomType")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping(path = "/getAll")
    public List<RoomTypeEntity> getAllRoomType() {
        return roomTypeService.getAllRoomType();
    }

    @PostMapping(path = "/add")
    public RoomTypeEntity addRoomType(@RequestBody RoomTypeEntity roomType) {
        return roomTypeService.saveRoomType(roomType);
    }

    @PutMapping(path = "/update/{id}")
    public RoomTypeEntity updateRoomType(@PathVariable Integer id, @RequestBody RoomTypeEntity roomType) {
        roomType.setTypeId(id);
        return roomTypeService.updateRoomType(roomType);
    }

    @DeleteMapping(path = "/delete/{id}")
    public String deleteRoomType(@PathVariable Integer id) {
        return roomTypeService.deleteRoomType(id);
    }
}
