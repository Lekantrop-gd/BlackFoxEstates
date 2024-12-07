package com.blackfox.estate.service;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.entity.HotelRoom;
import com.blackfox.estate.mapper.HotelRoomMapper;
import com.blackfox.estate.repository.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelRoomService {

    private final HotelRoomRepository hotelRoomRepository;
    private final HotelRoomMapper hotelRoomMapper;

    @Autowired
    public HotelRoomService(HotelRoomRepository hotelRoomRepository, HotelRoomMapper hotelRoomMapper) {
        this.hotelRoomRepository = hotelRoomRepository;
        this.hotelRoomMapper = hotelRoomMapper;
    }

    public Page<HotelRoomDTO> getHotelRooms(String roomType, Integer capacity, Double price, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));

        Specification<HotelRoom> spec = Specification.where(null);

        if (roomType != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("roomType"), roomType));
        }
        if (capacity != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("capacity"), capacity));
        }
        if (price != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("price"), price));
        }

        return hotelRoomRepository.findAll(spec, pageable)
                .map(hotelRoomMapper::toDTO);
    }

    public HotelRoomDTO createHotelRoom(HotelRoomDTO hotelRoomDTO) {
        HotelRoom hotelRoom = hotelRoomMapper.toEntity(hotelRoomDTO);
        HotelRoom savedHotelRoom = hotelRoomRepository.save(hotelRoom);
        return hotelRoomMapper.toDTO(savedHotelRoom);
    }

    public HotelRoomDTO updateHotelRoom(Long id, HotelRoomDTO hotelRoomDTO) {
        HotelRoom existingHotelRoom = hotelRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel room not found with ID: " + id));
        hotelRoomMapper.updateHotelRoomFromDTO(hotelRoomDTO, existingHotelRoom);
        HotelRoom updatedHotelRoom = hotelRoomRepository.save(existingHotelRoom);
        return hotelRoomMapper.toDTO(updatedHotelRoom);
    }

    public void deleteHotelRoom(Long id) {
        if (!hotelRoomRepository.existsById(id)) {
            throw new IllegalArgumentException("Hotel room not found with ID: " + id);
        }
        hotelRoomRepository.deleteById(id);
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