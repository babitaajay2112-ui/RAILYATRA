package com.railway.trainproject.repository;

import com.railway.trainproject.entity.SeatInventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatInventoryRepository
        extends JpaRepository<SeatInventory, Long>
{
    // Schedule ID se seat inventory find karna
    Optional<SeatInventory> findBySchedule_Id(Long scheduleId);
    // Concurrent bookings ke time inventory row ko lock karna
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SeatInventory s WHERE s.schedule.id = :scheduleId")
    Optional<SeatInventory> findByScheduleIdForUpdate
    (
            @Param("scheduleId") Long scheduleId
    );
}