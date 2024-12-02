package com.blackfox.estate.mapper;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, HotelRoomMapper.class})
public interface BookingMapper {

    BookingDTO toDTO(Booking booking);

    Booking toEntity(BookingDTO bookingDTO);

    @Mapping(target = "id", ignore = true)
    void updateBookingFromDTO(BookingDTO bookingDTO, @MappingTarget Booking booking);
}
