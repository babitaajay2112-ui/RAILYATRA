package com.railway.trainproject.service;

import com.railway.trainproject.entity.User;
import com.railway.trainproject.entity.Schedule;
import com.railway.trainproject.entity.Booking;
import com.railway.trainproject.entity.SeatInventory;

import com.railway.trainproject.repository.UserRepository;
import com.railway.trainproject.repository.ScheduleRepository;
import com.railway.trainproject.repository.BookingRepository;
import com.railway.trainproject.repository.SeatInventoryRepository;

import com.railway.trainproject.exception.ResourceNotFoundException;
import com.railway.trainproject.exception.InsufficientSeatsException;
import com.railway.trainproject.exception.InvalidBookingException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ScheduleRepository scheduleRepository,
            SeatInventoryRepository seatInventoryRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatInventoryRepository = seatInventoryRepository;
    }

    @Transactional
    public Booking bookTicket(
            String email,
            Long scheduleId,
            int seats) {

        if (seats < 1) {
            throw new InvalidBookingException(
                    "Number of seats must be at least 1"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found"));

        SeatInventory inventory =
                seatInventoryRepository.findByScheduleIdForUpdate(scheduleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Seat inventory not found"
                                ));

        if (inventory.getAvailableSeats() < seats) {
            throw new InsufficientSeatsException(
                    "Not enough seats available"
            );
        }

        inventory.setAvailableSeats(
                inventory.getAvailableSeats() - seats
        );

        Booking booking = new Booking();
        booking.setPnr(
                "PNR" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase()
        );
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setSeats(seats);
        booking.setTotalFare(schedule.getFare() * seats);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    public Booking getMyBooking(String pnr, String email) {
        return bookingRepository.findByPnrAndUser_Email(pnr, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found for this user"
                        ));
    }

    public List<Booking> getMyBookings(String email) {
        return bookingRepository
                .findByUser_EmailOrderByBookingTimeDesc(email);
    }

    @Transactional
    public Booking cancelTicket(String pnr, String email) {

        Booking booking = bookingRepository
                .findByPnrAndUser_Email(pnr, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found for this user"
                        ));

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new InvalidBookingException(
                    "Only confirmed bookings can be cancelled"
            );
        }

        Long scheduleId = booking.getSchedule().getId();

        SeatInventory inventory =
                seatInventoryRepository.findByScheduleIdForUpdate(scheduleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Seat inventory not found"
                                ));

        inventory.setAvailableSeats(
                inventory.getAvailableSeats() + booking.getSeats()
        );

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}