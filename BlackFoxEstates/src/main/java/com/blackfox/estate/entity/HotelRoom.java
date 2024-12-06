package com.blackfox.estate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="hotel_room")
@Setter
@Getter
@Entity
@NoArgsConstructor
public class HotelRoom {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_number", unique=true)
    private String roomNumber;

    @Column(name="room_type")
    private String roomType;

    @Column(name="capacity")
    private int capacity;

    @Column(name="price")
    private double price;
}