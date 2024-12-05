package com.blackfox.estate.service;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import com.blackfox.estate.exception.ResourceNotFoundException;
import com.blackfox.estate.mapper.BookingMapper;
import com.blackfox.estate.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Cacheable(value = "bookings", key = "#customerId + '-' + #hotelRoomId + '-' + #page + '-' + #size + '-' + #sort")
    public Page<BookingDTO> getBookings(Long customerId, Long hotelRoomId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return bookingRepository.findAll((root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customer").get("id"), customerId));
            }
            if (hotelRoomId != null) {
                predicates.add(criteriaBuilder.equal(root.get("hotelRoom").get("id"), hotelRoomId));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable).map(bookingMapper::toDTO);
    }

    @Transactional
    @CacheEvict(value = "bookings", allEntries = true)
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toEntity(bookingDTO);
        bookingRepository.save(booking);
        return bookingMapper.toDTO(booking);
    }

    @Transactional
    @CacheEvict(value = "bookings", allEntries = true)
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        bookingMapper.updateBookingFromDTO(bookingDTO, booking);
        bookingRepository.save(booking);
        return bookingMapper.toDTO(booking);
    }

    @CacheEvict(value = "bookings", allEntries = true)
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private List<Sort.Order> parseSort(String sort) {
        String[] sortParts = sort.split(",");
        if (sortParts.length != 2) {
            throw new IllegalArgumentException("Sort parameter must be in the format 'field,direction'");
        }
        String field = sortParts[0];
        String direction = sortParts[1];

        try {
            return List.of(new Sort.Order(Sort.Direction.fromString(direction.toUpperCase()), field));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction);
        }
    }

}