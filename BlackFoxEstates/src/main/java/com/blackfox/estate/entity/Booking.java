package com.blackfox.estate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long hotelRoomId;
    private String checkInDate;
    private String checkOutDate;

    // Конструктор без параметрів
    public Booking() {}

    // Конструктор з параметрами
    public Booking(Long customerId, Long hotelRoomId, String checkInDate, String checkOutDate) {
        this.customerId = customerId;
        this.hotelRoomId = hotelRoomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Геттери та сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(Long hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
