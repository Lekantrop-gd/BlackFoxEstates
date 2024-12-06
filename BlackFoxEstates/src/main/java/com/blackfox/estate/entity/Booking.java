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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_room_id")
    private HotelRoom hotelRoom;

    @Column(name="check_in_date")
    private String checkInDate;

    @Column(name="check_out_date")
    private String checkOutDate;
}