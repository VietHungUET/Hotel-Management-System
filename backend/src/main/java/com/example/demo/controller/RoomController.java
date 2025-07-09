package com.example.demo.controller;

import com.example.demo.entity.ResponseAvailRoom;
import com.example.demo.entity.ResponseForListRoom;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomTypeEntity;
import com.example.demo.service.CustomDetails;
import com.example.demo.service.HotelService;
import com.example.demo.service.RoomService;
import com.example.demo.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final HotelService hotelService;

    @GetMapping(path = "/updateStatusRoomUsing2Active/{id}")
    public String updateStatusUsing2Active(@PathVariable int id) {
        return roomService.updateStatusUsing2Active(id);
    }

    //This function will set from Active room to using room
    @GetMapping(path = "/updateStatusRoomActive2Using/{id}")
    public String updateStatusActive2Using(@PathVariable int id) {
        return roomService.updateStatusActive2Using(id);
    }

    @PostMapping("/add")
    public RoomEntity addRoom(@RequestBody RoomEntity roomEntity,
                              @AuthenticationPrincipal CustomDetails userDetails) {

        int userId = userDetails.getUser().getId();

        // Lấy hotel_id của user
        Integer hotelId = hotelService.getHotelIdByUserId(userId);

        // Nếu không có hotel thì trả về null, không xử lý gì thêm
        if (hotelId == null) {
            return null;
        }

        // Gán hotelId cho room rồi lưu
        roomEntity.setHotelId(hotelId);
        return roomService.saveDetails(roomEntity);
    }


    @DeleteMapping(path = "/delete/{id}")
    public String deleteRoom(@PathVariable Integer id) {
        return roomService.deleteRoom(id);
    }

    @GetMapping(path = "/getAll/{hotelId}")
    public List<ResponseForListRoom> getListRoom(@AuthenticationPrincipal CustomDetails userDetails) {
        int userId = userDetails.getUser().getId();

        Integer hotelId = hotelService.getHotelIdByUserId(userId);
        if (hotelId == null) {
            return List.of(); // Trả về danh sách rỗng nếu user chưa có khách sạn
        }

        return lisResponse(hotelId, roomService, roomTypeService);
    }

    public List<ResponseForListRoom> lisResponse(int hotelId, RoomService roomService, RoomTypeService roomTypeService) {
        List<RoomEntity> listRoom =   roomService.getAllByHotelID(hotelId);
        List<ResponseForListRoom> listResponse = new ArrayList<>();
        for(RoomEntity room : listRoom) {
            String roomNum = room.getRoomNumber();
            int typeId = room.getTypeId();
            RoomTypeEntity roomType = roomTypeService.getRoomTypeById(typeId);
            ResponseForListRoom res = new ResponseForListRoom();
            res.setType(roomType.getName());
            res.setRoomNumber(roomNum);
            res.setDailyRate(roomType.getNightRate());
            res.setStatus(room.getStatus());
            res.setMaximumCapacity(roomType.getCapacity());
            res.setNotes(roomType.getDescription());
            res.setOvertimeRate(roomType.getOvertimePay());
            res.setDayRate(roomType.getDayRate());
            res.setNightRate(roomType.getNightRate());
            listResponse.add(res);
        }
        return listResponse;
    }


    @GetMapping(path = "/getAvailRoom/{id}")
    public List<ResponseAvailRoom> getAvailRoom(@PathVariable int id) {
        List<ResponseForListRoom> lisResponseDetail =lisResponse(id, roomService, roomTypeService);
        List<String> listType = new ArrayList<>();
        Set<String> set  = new HashSet<>();
        for (ResponseForListRoom lis : lisResponseDetail) {
            String status = lis.getStatus();
            if (status.equals("Available")) {
                set.add(lis.getType());
            }
        }
        for(String s : set) {
            listType.add(s);
        }

        List<ResponseAvailRoom> ans = new ArrayList<>();
        for (String type : listType) {
            int capa = 0;
            Double dayrate = (double) 0;
            Double nightrate = (double) 0;
            Double dailyrate = (double) 0;
            Double overtimepay = (double) 0;
            List<String> lisRoomNumber = new ArrayList<>();
            ResponseAvailRoom resAvail = new ResponseAvailRoom();
            int count = 0;
            for (ResponseForListRoom lis : lisResponseDetail) {
                String typeName = lis.getType();
                if (typeName.equals(type)) {
                    lisRoomNumber.add(lis.getRoomNumber());
                    capa = lis.getMaximumCapacity();
                    dayrate = lis.getDayRate();
                    nightrate = lis.getNightRate();
                    dailyrate = lis.getDailyRate();
                    overtimepay = lis.getOvertimeRate();
                }

            }
            resAvail.setCapacity(capa);
            resAvail.setDailyRate(dailyrate);
            resAvail.setType(type);
            resAvail.setDayRate(dayrate);
            resAvail.setListRoomNumber(lisRoomNumber);
            resAvail.setNightRate(nightrate);
            resAvail.setOvertimePay(overtimepay);
            ans.add(resAvail);
        }
        return ans;
    }
}
