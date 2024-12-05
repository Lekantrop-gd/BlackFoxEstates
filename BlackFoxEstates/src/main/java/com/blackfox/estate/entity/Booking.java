package com.blackfox.estate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="booking")
@Setter
@Getter
@Entity
@NoArgsConstructor
public class Booking {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="customer_id")
    private Long customerId;

    @Column(name="hotel_room_id")
    private Long hotelRoomId;

    @Column(name="check_in_date")
    private String checkInDate;

    @Column(name="check_out_date")
    private String checkOutDate;
}