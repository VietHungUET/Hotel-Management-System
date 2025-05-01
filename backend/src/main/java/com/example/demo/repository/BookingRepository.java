package com.example.demo.repository;

import com.example.demo.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
//    @Query("SELECT p FROM BookingEntity p WHERE p.guest_id = :guestId")
//    List<BookingEntity> findByGuestID(String guestId);

    @Query("SELECT p FROM BookingEntity p WHERE p.roomNumber = :roomNumber")
    BookingEntity findByRoomNumber(String roomNumber);
}