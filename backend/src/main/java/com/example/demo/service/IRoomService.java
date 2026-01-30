package com.example.demo.service;

import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ResponseAvailRoom;
import com.example.demo.entity.ResponseForListRoom;
import com.example.demo.entity.RoomEntity;

import java.util.List;

/**
 * Interface for room service operations.
 */
public interface IRoomService {

    // CRUD operations
    RoomEntity saveDetails(RoomEntity room);

    RoomEntity getRoom(int roomNumber);

    String deleteRoom(Integer id);

    List<RoomEntity> getAllByHotelId(int hotelId);

    // Status operations
    String updateStatusToActive(int id);

    String updateStatusToInactive(int id);

    // Business logic
    int countByRoomType(int roomType);

    List<ResponseForListRoom> getRoomListWithDetails(int hotelId);

    List<ResponseAvailRoom> getAvailableRooms(int hotelId);

    // DTO conversion
    RoomDto convertToDto(RoomEntity room);

    List<RoomDto> getConvertedRooms(List<RoomEntity> rooms);
}
