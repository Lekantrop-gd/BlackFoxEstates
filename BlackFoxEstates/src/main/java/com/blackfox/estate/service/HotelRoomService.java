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
import org.springframework.stereotype.Service;

@Service
public class HotelRoomService {

    private final HotelRoomRepository hotelRoomRepository;
    private final HotelRoomMapper hotelRoomMapper;

    @Autowired
    public HotelRoomService(HotelRoomRepository hotelRoomRepository, HotelRoomMapper hotelRoomMapper) {
        this.hotelRoomRepository = hotelRoomRepository;
        this.hotelRoomMapper = hotelRoomMapper;
    }

    public Page<HotelRoomDTO> getHotelRooms(String roomType, Integer capacity, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));

        if (roomType != null && capacity != null) {
            return hotelRoomRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("roomType"), roomType),
                    criteriaBuilder.equal(root.get("capacity"), capacity)
            ), pageable).map(hotelRoomMapper::toDTO);
        } else if (roomType != null) {
            return hotelRoomRepository.findAll((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("roomType"), roomType), pageable)
                    .map(hotelRoomMapper::toDTO);
        } else if (capacity != null) {
            return hotelRoomRepository.findAll((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("capacity"), capacity), pageable)
                    .map(hotelRoomMapper::toDTO);
        } else {
            return hotelRoomRepository.findAll(pageable).map(hotelRoomMapper::toDTO);
        }
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

    private Sort parseSort(String[] sort) {
        if (sort.length == 2) {
            return Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        }
        return Sort.by(Sort.Direction.ASC, "id");
    }
}