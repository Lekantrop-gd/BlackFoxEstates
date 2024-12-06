package com.blackfox.estate.controller;

import com.blackfox.estate.dto.BookingDTO;
import com.blackfox.estate.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(
            summary = "Get Bookings",
            description = "Fetch a paginated and sorted list of bookings filtered by optional parameters.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved bookings.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Page<BookingDTO>> getBookings(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long hotelRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Page<BookingDTO> bookings = bookingService.getBookings(customerId, hotelRoomId, page, size, sort);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    @Operation(
            summary = "Create Booking",
            description = "Create a new booking.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid booking details.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Booking",
            description = "Update an existing booking.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Booking",
            description = "Delete a booking by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Booking deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}