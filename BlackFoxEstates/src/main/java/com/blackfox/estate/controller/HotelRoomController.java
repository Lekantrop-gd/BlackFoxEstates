package com.blackfox.estate.controller;

import com.blackfox.estate.dto.HotelRoomDTO;
import com.blackfox.estate.service.HotelRoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/hotel-rooms")
public class HotelRoomController {

    private final HotelRoomService hotelRoomService;

    public HotelRoomController(HotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    @GetMapping
    @Operation(
            summary = "Get Hotel Rooms",
            description = "Fetch a paginated and sorted list of hotel rooms, optionally filtered by room type or capacity.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved hotel rooms.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Page<HotelRoomDTO>> getHotelRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Page<HotelRoomDTO> hotelRooms = hotelRoomService.getHotelRooms(roomType, capacity, page, size, sort);
        return ResponseEntity.ok(hotelRooms);
    }

    @PostMapping
    @Operation(
            summary = "Create Hotel Room",
            description = "Create a new hotel room.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Hotel room created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelRoomDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid hotel room details.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<HotelRoomDTO> createHotelRoom(@Valid @RequestBody HotelRoomDTO hotelRoomDTO) {
        HotelRoomDTO createdRoom = hotelRoomService.createHotelRoom(hotelRoomDTO);
        return ResponseEntity.status(201).body(createdRoom);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Hotel Room",
            description = "Update an existing hotel room's details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Hotel room updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelRoomDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hotel room not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<HotelRoomDTO> updateHotelRoom(
            @PathVariable Long id,
            @Valid @RequestBody HotelRoomDTO hotelRoomDTO
    ) {
        HotelRoomDTO updatedRoom = hotelRoomService.updateHotelRoom(id, hotelRoomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Hotel Room",
            description = "Delete a hotel room by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Hotel room deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hotel room not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Void> deleteHotelRoom(@PathVariable Long id) {
        hotelRoomService.deleteHotelRoom(id);
        return ResponseEntity.noContent().build();
    }
}
