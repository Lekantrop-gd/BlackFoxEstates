package com.blackfox.estate.controller;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.service.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotelRooms")
public class HotelRoomController {

    private final HotelRoomService hotelRoomService;

    @Autowired
    public HotelRoomController(HotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    // Створення нового номера
    @PostMapping
    public ResponseEntity<HotelRoomDTO> createHotelRoom(@RequestBody HotelRoomDTO hotelRoomDTO) {
        HotelRoomDTO createdRoom = hotelRoomService.createHotelRoom(hotelRoomDTO);
        return ResponseEntity.ok(createdRoom);
    }

    // Отримання всіх номерів
    @GetMapping
    public ResponseEntity<List<HotelRoomDTO>> getAllHotelRooms() {
        List<HotelRoomDTO> rooms = hotelRoomService.getAllHotelRooms();
        return ResponseEntity.ok(rooms);
    }

    // Отримання номера за ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelRoomDTO> getHotelRoomById(@PathVariable Long id) {
        HotelRoomDTO room = hotelRoomService.getHotelRoomById(id);
        return ResponseEntity.ok(room);
    }

    // Оновлення номера
    @PutMapping("/{id}")
    public ResponseEntity<HotelRoomDTO> updateHotelRoom(@PathVariable Long id, @RequestBody HotelRoomDTO hotelRoomDTO) {
        HotelRoomDTO updatedRoom = hotelRoomService.updateHotelRoom(id, hotelRoomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    // Видалення номера
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelRoom(@PathVariable Long id) {
        hotelRoomService.deleteHotelRoom(id);
        return ResponseEntity.noContent().build();
    }
}
