package com.blackfox.estate.dto;

public record BookingDTO(Long id, Long customerId, Long hotelRoomId, String checkInDate, String checkOutDate) {
}
