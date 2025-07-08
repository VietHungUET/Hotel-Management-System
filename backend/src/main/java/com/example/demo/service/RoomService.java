package com.example.demo.service;

import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomEntity saveDetails(RoomEntity room) {
        roomRepository.save(room);
        return room;
    }
    public RoomEntity getRoom(int roomNumber) {
        return roomRepository.findById(roomNumber).orElse(null);
    }


    public String updateStatusUsing2Active(int id) {
        RoomEntity room = roomRepository.findById(id).orElse(null);
        if (room == null) return null;

        String ans = room.getStatus();
        if(ans.equals("using")) {
            room.setStatus("Active");
            roomRepository.save(room);
            return "Done";
        }
        else {
            return "Fail!!!";
        }
    }

    public String updateStatusActive2Using(int id) {
        RoomEntity room = roomRepository.findById(id).orElse(null);
        if (room == null) return null;
        String ans = room.getStatus();
        if(ans.equals("Active")) {
            room.setStatus("using");
            roomRepository.save(room);
            return "Done";
        }
        else {
            return "Fail!!!";
        }
    }
    public List<RoomEntity> getAllByHotelID(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public int countByRoomType(int roomType) {
        return roomRepository.countRoom(roomType,"Available");
    }

    public String deleteRoom(Integer id) {
        roomRepository.deleteById(id);
        return "Delete Success";
    }
}
