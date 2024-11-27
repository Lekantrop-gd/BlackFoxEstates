package com.blackfox.estate.repository;

import com.blackfox.estate.entity.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long> {
    // Можна додавати методи для пошуку номерів за різними критеріями
}
