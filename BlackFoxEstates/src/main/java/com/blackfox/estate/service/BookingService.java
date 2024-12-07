package com.blackfox.estate.service;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.entity.Booking;
import com.blackfox.estate.entity.Customer;
import com.blackfox.estate.entity.HotelRoom;
import com.blackfox.estate.mapper.BookingMapper;
import com.blackfox.estate.repository.BookingRepository;
import com.blackfox.estate.repository.CustomerRepository;
import com.blackfox.estate.repository.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          HotelRoomRepository hotelRoomRepository,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.hotelRoomRepository = hotelRoomRepository;
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
        Customer customer = customerRepository.findById(bookingDTO.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + bookingDTO.customerId()));
        HotelRoom hotelRoom = hotelRoomRepository.findById(bookingDTO.hotelRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel room not found with ID: " + bookingDTO.hotelRoomId()));

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setCustomer(customer);
        booking.setHotelRoom(hotelRoom);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(savedBooking);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));

        Customer customer = customerRepository.findById(bookingDTO.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + bookingDTO.customerId()));
        HotelRoom hotelRoom = hotelRoomRepository.findById(bookingDTO.hotelRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel room not found with ID: " + bookingDTO.hotelRoomId()));

        bookingMapper.updateBookingFromDTO(bookingDTO, existingBooking);
        existingBooking.setCustomer(customer);
        existingBooking.setHotelRoom(hotelRoom);

        Booking updatedBooking = bookingRepository.save(existingBooking);
        return bookingMapper.toDTO(updatedBooking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private Sort parseSort(String[] sortParams) {
        if (sortParams == null || sortParams.length == 0) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] split = param.split(",");
            if (split.length == 2) {
                String field = split[0];
                String direction = split[1];
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
            } else {
                throw new IllegalArgumentException("Invalid sort parameter: " + param);
            }
        }
        return Sort.by(orders);
    }
}