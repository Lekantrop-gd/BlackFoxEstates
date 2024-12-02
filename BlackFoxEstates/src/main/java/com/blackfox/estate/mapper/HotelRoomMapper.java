package com.blackfox.estate.mapper;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.entity.HotelRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HotelRoomMapper {

    HotelRoomDTO toDTO(HotelRoom hotelRoom);

    HotelRoom toEntity(HotelRoomDTO hotelRoomDTO);

    @Mapping(target = "id", ignore = true)
    void updateHotelRoomFromDTO(HotelRoomDTO hotelRoomDTO, @MappingTarget HotelRoom hotelRoom);
}
