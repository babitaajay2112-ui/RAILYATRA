package com.railway.trainproject.repository;

import com.railway.trainproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // PNR se booking find karna
    Optional<Booking> findByPnr(String pnr);

    // PNR aur user email dono se booking find karna
    Optional<Booking> findByPnrAndUser_Email(
            String pnr,
            String email
    );

    // User ki saari bookings latest first order mein
    List<Booking> findByUser_EmailOrderByBookingTimeDesc(
            String email
    );
}