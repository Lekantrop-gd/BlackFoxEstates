package com.blackfox.estate.repository;

import com.blackfox.estate.entity.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long>, JpaSpecificationExecutor<HotelRoom> {
    boolean existsByRoomType(String roomType);
}
