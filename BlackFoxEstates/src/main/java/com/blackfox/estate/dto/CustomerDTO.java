package com.blackfox.estate.dto;

public record CustomerDTO(Long id, String name, String email, String phoneNumber) {
    // DTO не потребує додаткової логіки
}
