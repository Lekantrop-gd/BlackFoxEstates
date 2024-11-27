package com.blackfox.estate.service;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import com.blackfox.estate.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Створення нового бронювання
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking(
                bookingDTO.customerId(),
                bookingDTO.hotelRoomId(),
                bookingDTO.checkInDate(),
                bookingDTO.checkOutDate());
        booking = bookingRepository.save(booking);

        return new BookingDTO(booking.getId(), booking.getCustomerId(), booking.getHotelRoomId(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }

    // Отримання всіх бронювань
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(b -> new BookingDTO(b.getId(), b.getCustomerId(), b.getHotelRoomId(),
                        b.getCheckInDate(), b.getCheckOutDate()))
                .toList();
    }

    // Отримання бронювання за ID
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return new BookingDTO(booking.getId(), booking.getCustomerId(), booking.getHotelRoomId(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }

    // Оновлення бронювання
    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setCustomerId(bookingDTO.customerId());
        booking.setHotelRoomId(bookingDTO.hotelRoomId());
        booking.setCheckInDate(bookingDTO.checkInDate());
        booking.setCheckOutDate(bookingDTO.checkOutDate());

        booking = bookingRepository.save(booking);

        return new BookingDTO(booking.getId(), booking.getCustomerId(), booking.getHotelRoomId(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }

    // Видалення бронювання
    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }
}
