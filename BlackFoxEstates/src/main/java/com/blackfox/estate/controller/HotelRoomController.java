package com.blackfox.estate.controller;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.service.HotelRoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotelRooms")
public class HotelRoomController {

    private final HotelRoomService hotelRoomService;

    public HotelRoomController(HotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    @GetMapping
    public ResponseEntity<Page<HotelRoomDTO>> getHotelRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Page<HotelRoomDTO> rooms = hotelRoomService.getHotelRooms(roomType, capacity, page, size, sort);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<HotelRoomDTO> createHotelRoom(@Valid @RequestBody HotelRoomDTO hotelRoomDTO) {
        return new ResponseEntity<>(hotelRoomService.createHotelRoom(hotelRoomDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelRoomDTO> updateHotelRoom(
            @PathVariable Long id,
            @Valid @RequestBody HotelRoomDTO hotelRoomDTO
    ) {
        return ResponseEntity.ok(hotelRoomService.updateHotelRoom(id, hotelRoomDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelRoom(@PathVariable Long id) {
        hotelRoomService.deleteHotelRoom(id);
        return ResponseEntity.noContent().build();
    }
}