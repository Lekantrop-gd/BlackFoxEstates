package com.blackfox.estate.repository;

import com.blackfox.estate.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Можна додати методи для пошуку бронювань, якщо потрібно
}
