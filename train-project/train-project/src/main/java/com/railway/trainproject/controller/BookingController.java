package com.railway.trainproject.controller;

import com.railway.trainproject.dto.BookingRequest;
import com.railway.trainproject.entity.Booking;
import com.railway.trainproject.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST: Book a ticket
    // http://localhost:8085/api/bookings
    @PostMapping
    public Booking bookTicket(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        return bookingService.bookTicket(
                authentication.getName(),
                request.getScheduleId(),
                request.getSeats()
        );
    }

    // GET: Get logged-in user's bookings
    // http://localhost:8085/api/bookings
    @GetMapping
    public List<Booking> getMyBookings(
            Authentication authentication) {

        return bookingService.getMyBookings(
                authentication.getName()
        );
    }

    // GET: Get booking using PNR
    // http://localhost:8085/api/bookings/{pnr}
    @GetMapping("/{pnr}")
    public Booking getBooking(
            @PathVariable String pnr,
            Authentication authentication) {

        return bookingService.getMyBooking(
                pnr,
                authentication.getName()
        );
    }

    // PUT: Cancel booking
    // http://localhost:8085/api/bookings/{pnr}/cancel
    @PutMapping("/{pnr}/cancel")
    public Booking cancelBooking(
            @PathVariable String pnr,
            Authentication authentication) {

        return bookingService.cancelTicket(
                pnr,
                authentication.getName()
        );
    }
}
