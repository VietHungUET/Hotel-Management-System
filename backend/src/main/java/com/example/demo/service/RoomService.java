package com.example.demo.service;

import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ResponseAvailRoom;
import com.example.demo.entity.ResponseForListRoom;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public RoomEntity saveDetails(RoomEntity room) {
        roomRepository.save(room);
        return room;
    }

    @Override
    public RoomEntity getRoom(int roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", roomId));
    }

    @Override
    public String updateStatusToActive(int id) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        if ("INACTIVE".equalsIgnoreCase(room.getStatus())) {
            room.setStatus("ACTIVE");
            roomRepository.save(room);
            return "Room status updated to ACTIVE";
        } else {
            throw new BadRequestException("Room is not INACTIVE, cannot change to ACTIVE");
        }
    }

    @Override
    public String updateStatusToInactive(int id) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        if ("ACTIVE".equalsIgnoreCase(room.getStatus())) {
            room.setStatus("INACTIVE");
            roomRepository.save(room);
            return "Room status updated to INACTIVE";
        } else {
            throw new BadRequestException("Room is not ACTIVE, cannot change to INACTIVE");
        }
    }

    @Override
    public List<RoomEntity> getAllByHotelId(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public int countByRoomType(int roomType) {
        return roomRepository.countRoom(roomType, "ACTIVE");
    }

    @Override
    public String deleteRoom(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", id);
        }
        roomRepository.deleteById(id);
        return "Room deleted successfully";
    }

    @Override
    public List<ResponseForListRoom> getRoomListWithDetails(int hotelId) {
        List<RoomEntity> rooms = roomRepository.findByHotelId(hotelId);
        List<ResponseForListRoom> response = new ArrayList<>();

        for (RoomEntity room : rooms) {
            RoomTypeEntity roomType = roomTypeRepository.findById(room.getTypeId()).orElse(null);
            if (roomType == null)
                continue;

            ResponseForListRoom res = new ResponseForListRoom();
            res.setType(roomType.getName());
            res.setRoomNumber(room.getRoomNumber());
            res.setDailyRate(roomType.getNightRate());
            res.setStatus(room.getStatus());
            res.setMaximumCapacity(roomType.getCapacity());
            res.setNotes(roomType.getDescription());
            res.setOvertimeRate(roomType.getOvertimePay());
            res.setDayRate(roomType.getDayRate());
            res.setNightRate(roomType.getNightRate());
            response.add(res);
        }

        return response;
    }

    @Override
    public List<ResponseAvailRoom> getAvailableRooms(int hotelId) {
        List<ResponseForListRoom> roomDetails = getRoomListWithDetails(hotelId);

        // Get unique room types with ACTIVE status
        Set<String> activeRoomTypes = new HashSet<>();
        for (ResponseForListRoom room : roomDetails) {
            if ("ACTIVE".equalsIgnoreCase(room.getStatus())) {
                activeRoomTypes.add(room.getType());
            }
        }

        List<ResponseAvailRoom> result = new ArrayList<>();

        for (String type : activeRoomTypes) {
            ResponseAvailRoom availRoom = new ResponseAvailRoom();
            List<String> roomNumbers = new ArrayList<>();

            int capacity = 0;
            Double dayRate = 0.0;
            Double nightRate = 0.0;
            Double dailyRate = 0.0;
            Double overtimePay = 0.0;

            for (ResponseForListRoom room : roomDetails) {
                if (type.equals(room.getType()) && "ACTIVE".equalsIgnoreCase(room.getStatus())) {
                    roomNumbers.add(room.getRoomNumber());
                    capacity = room.getMaximumCapacity();
                    dayRate = room.getDayRate();
                    nightRate = room.getNightRate();
                    dailyRate = room.getDailyRate();
                    overtimePay = room.getOvertimeRate();
                }
            }

            availRoom.setType(type);
            availRoom.setCapacity(capacity);
            availRoom.setDayRate(dayRate);
            availRoom.setNightRate(nightRate);
            availRoom.setDailyRate(dailyRate);
            availRoom.setOvertimePay(overtimePay);
            availRoom.setListRoomNumber(roomNumbers);

            result.add(availRoom);
        }

        return result;
    }

    @Override
    public RoomDto convertToDto(RoomEntity room) {
        return new RoomDto(
                room.getRoomId(),
                room.getHotelId(),
                room.getTypeId(),
                room.getRoomNumber(),
                room.getStatus(),
                room.getNotes());
    }

    @Override
    public List<RoomDto> getConvertedRooms(List<RoomEntity> rooms) {
        return rooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
