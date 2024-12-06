package com.blackfox.estate.mapper;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, HotelRoomMapper.class})
public interface BookingMapper {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "hotelRoom.id", target = "hotelRoomId")
    BookingDTO toDTO(Booking booking);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "hotelRoom", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "hotelRoom", ignore = true)
    void updateBookingFromDTO(BookingDTO bookingDTO, @MappingTarget Booking booking);
}