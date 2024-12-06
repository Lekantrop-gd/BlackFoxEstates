package com.blackfox.estate.controller;

import com.blackfox.estate.dto.CustomerDTO;
import com.blackfox.estate.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(
            summary = "Get Customers",
            description = "Fetch a paginated and sorted list of customers, filtered by optional email.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved customers.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Page<CustomerDTO>> getCustomers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Page<CustomerDTO> customers = customerService.getCustomers(email, page, size, sort);
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    @Operation(
            summary = "Create Customer",
            description = "Create a new customer.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid customer details.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Customer",
            description = "Update an existing customer's details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Customer",
            description = "Delete a customer by their ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Customer deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}