package com.blackfox.estate.service;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.entity.HotelRoom;
import com.blackfox.estate.exception.ResourceNotFoundException;
import com.blackfox.estate.mapper.HotelRoomMapper;
import com.blackfox.estate.repository.HotelRoomRepository;
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
public class HotelRoomService {

    private final HotelRoomRepository hotelRoomRepository;
    private final HotelRoomMapper hotelRoomMapper;

    public HotelRoomService(HotelRoomRepository hotelRoomRepository, HotelRoomMapper hotelRoomMapper) {
        this.hotelRoomRepository = hotelRoomRepository;
        this.hotelRoomMapper = hotelRoomMapper;
    }

    @Cacheable(value = "hotelRooms", key = "#roomType + '-' + #capacity + '-' + #page + '-' + #size + '-' + T(java.util.Arrays).toString(#sort)")
    public Page<HotelRoomDTO> getHotelRooms(String roomType, Integer capacity, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return hotelRoomRepository.findAll((root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (roomType != null) {
                predicates.add(criteriaBuilder.equal(root.get("roomType"), roomType));
            }
            if (capacity != null) {
                predicates.add(criteriaBuilder.equal(root.get("capacity"), capacity));
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable).map(hotelRoomMapper::toDTO);
    }

    @Transactional
    @CacheEvict(value = "hotelRooms", allEntries = true)
    public HotelRoomDTO createHotelRoom(HotelRoomDTO hotelRoomDTO) {
        if (hotelRoomRepository.existsByRoomType(hotelRoomDTO.roomType())) {
            throw new IllegalArgumentException("Room type " + hotelRoomDTO.roomType() + " already exists");
        }
        HotelRoom hotelRoom = hotelRoomMapper.toEntity(hotelRoomDTO);
        hotelRoom = hotelRoomRepository.save(hotelRoom);
        return hotelRoomMapper.toDTO(hotelRoom);
    }

    @Transactional
    @CacheEvict(value = "hotelRooms", allEntries = true)
    public HotelRoomDTO updateHotelRoom(Long id, HotelRoomDTO hotelRoomDTO) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel room not found with id: " + id));
        hotelRoomMapper.updateHotelRoomFromDTO(hotelRoomDTO, hotelRoom);
        hotelRoomRepository.save(hotelRoom);
        return hotelRoomMapper.toDTO(hotelRoom);
    }

    @CacheEvict(value = "hotelRooms", allEntries = true)
    public void deleteHotelRoom(Long id) {
        hotelRoomRepository.deleteById(id);
    }

    private List<Sort.Order> parseSort(String[] sort) {
        return Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Sort parameter must be in the format 'field,direction'");
                    }
                    String property = parts[0]; // Перше значення — це поле сортування
                    Sort.Direction direction;
                    try {
                        direction = Sort.Direction.fromString(parts[1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid sort direction: " + parts[1]);
                    }
                    return new Sort.Order(direction, property);
                })
                .collect(Collectors.toList());
    }

}