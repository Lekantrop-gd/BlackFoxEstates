package com.blackfox.estate.service;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.entity.HotelRoom;
import com.blackfox.estate.exception.ResourceNotFoundException;
import com.blackfox.estate.mapper.HotelRoomMapper;
import com.blackfox.estate.repository.HotelRoomRepository;
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

    public HotelRoomDTO createHotelRoom(HotelRoomDTO hotelRoomDTO) {
        HotelRoom hotelRoom = hotelRoomMapper.toEntity(hotelRoomDTO);
        hotelRoomRepository.save(hotelRoom);
        return hotelRoomMapper.toDTO(hotelRoom);
    }

    public HotelRoomDTO updateHotelRoom(Long id, HotelRoomDTO hotelRoomDTO) {
        HotelRoom hotelRoom = hotelRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel room not found with id: " + id));
        hotelRoomMapper.updateHotelRoomFromDTO(hotelRoomDTO, hotelRoom);
        hotelRoomRepository.save(hotelRoom);
        return hotelRoomMapper.toDTO(hotelRoom);
    }

    public void deleteHotelRoom(Long id) {
        hotelRoomRepository.deleteById(id);
    }

    private List<Sort.Order> parseSort(String[] sort) {
        return Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .collect(Collectors.toList());
    }
}