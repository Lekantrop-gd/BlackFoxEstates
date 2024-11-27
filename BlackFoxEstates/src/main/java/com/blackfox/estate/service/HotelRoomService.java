package com.blackfox.estate.service;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.entity.HotelRoom;
import com.blackfox.estate.repository.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelRoomService {

    private final HotelRoomRepository hotelRoomRepository;

    @Autowired
    public HotelRoomService(HotelRoomRepository hotelRoomRepository) {
        this.hotelRoomRepository = hotelRoomRepository;
    }

    // Створення нового номера
    @Transactional
    public HotelRoomDTO createHotelRoom(HotelRoomDTO hotelRoomDTO) {
        HotelRoom hotelRoom = new HotelRoom(
                hotelRoomDTO.roomNumber(),
                hotelRoomDTO.roomType(),
                hotelRoomDTO.capacity(),
                hotelRoomDTO.price());
        hotelRoom = hotelRoomRepository.save(hotelRoom);

        return new HotelRoomDTO(hotelRoom.getId(), hotelRoom.getRoomNumber(), hotelRoom.getRoomType(),
                hotelRoom.getCapacity(), hotelRoom.getPrice());
    }

    // Отримання всіх номерів
    public List<HotelRoomDTO> getAllHotelRooms() {
        List<HotelRoom> rooms = hotelRoomRepository.findAll();
        return rooms.stream()
                .map(r -> new HotelRoomDTO(r.getId(), r.getRoomNumber(), r.getRoomType(), r.getCapacity(), r.getPrice()))
                .toList();
    }

    // Отримання номера за ID
    public HotelRoomDTO getHotelRoomById(Long id) {
        HotelRoom room = hotelRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel room not found"));

        return new HotelRoomDTO(room.getId(), room.getRoomNumber(), room.getRoomType(),
                room.getCapacity(), room.getPrice());
    }

    // Оновлення номера
    @Transactional
    public HotelRoomDTO updateHotelRoom(Long id, HotelRoomDTO hotelRoomDTO) {
        HotelRoom room = hotelRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel room not found"));

        room.setRoomNumber(hotelRoomDTO.roomNumber());
        room.setRoomType(hotelRoomDTO.roomType());
        room.setCapacity(hotelRoomDTO.capacity());
        room.setPrice(hotelRoomDTO.price());

        room = hotelRoomRepository.save(room);

        return new HotelRoomDTO(room.getId(), room.getRoomNumber(), room.getRoomType(),
                room.getCapacity(), room.getPrice());
    }

    // Видалення номера
    @Transactional
    public void deleteHotelRoom(Long id) {
        if (!hotelRoomRepository.existsById(id)) {
            throw new RuntimeException("Hotel room not found");
        }
        hotelRoomRepository.deleteById(id);
    }
}
