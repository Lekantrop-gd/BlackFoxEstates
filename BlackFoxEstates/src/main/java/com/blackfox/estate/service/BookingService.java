package com.blackfox.estate.service;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import com.blackfox.estate.mapper.BookingMapper;
import com.blackfox.estate.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    public Page<BookingDTO> getBookings(Long customerId, Long hotelRoomId, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));

        if (customerId != null && hotelRoomId != null) {
            return bookingRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("customer").get("id"), customerId),
                    criteriaBuilder.equal(root.get("hotelRoom").get("id"), hotelRoomId)
            ), pageable).map(bookingMapper::toDTO);
        } else if (customerId != null) {
            return bookingRepository.findAll((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("customer").get("id"), customerId), pageable)
                    .map(bookingMapper::toDTO);
        } else if (hotelRoomId != null) {
            return bookingRepository.findAll((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("hotelRoom").get("id"), hotelRoomId), pageable)
                    .map(bookingMapper::toDTO);
        } else {
            return bookingRepository.findAll(pageable).map(bookingMapper::toDTO);
        }
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toEntity(bookingDTO);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(savedBooking);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
        bookingMapper.updateBookingFromDTO(bookingDTO, existingBooking);
        Booking updatedBooking = bookingRepository.save(existingBooking);
        return bookingMapper.toDTO(updatedBooking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private Sort parseSort(String[] sort) {
        if (sort.length == 2) {
            return Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        }
        return Sort.by(Sort.Direction.ASC, "id"); // Default sort
    }
}
