package com.railway.trainproject.service;

import com.railway.trainproject.entity.Train;
import com.railway.trainproject.entity.Schedule;
import com.railway.trainproject.entity.SeatInventory;

import com.railway.trainproject.repository.TrainRepository;
import com.railway.trainproject.repository.ScheduleRepository;
import com.railway.trainproject.repository.SeatInventoryRepository;

import com.railway.trainproject.exception.DuplicateResourceException;
import com.railway.trainproject.exception.ResourceNotFoundException;
import com.railway.trainproject.exception.InvalidBookingException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    public TrainService(
            TrainRepository trainRepository,
            ScheduleRepository scheduleRepository,
            SeatInventoryRepository seatInventoryRepository) {

        this.trainRepository = trainRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatInventoryRepository = seatInventoryRepository;
    }

    @Transactional
    public Train addTrain(Train train) {

        if (trainRepository.findByTrainNumber(
                train.getTrainNumber()).isPresent()) {
            throw new DuplicateResourceException(
                    "Train number already exists"
            );
        }

        return trainRepository.save(train);
    }

    @Transactional
    public Schedule addSchedule(
            Long trainId,
            LocalDate date,
            LocalTime departure,
            LocalTime arrival,
            double fare,
            int seats) {

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new InvalidBookingException(
                    "Journey date cannot be in the past"
            );
        }

        if (departure == null || arrival == null
                || !arrival.isAfter(departure)) {
            throw new InvalidBookingException(
                    "Arrival must be after departure"
            );
        }

        if (!Double.isFinite(fare) || fare < 0 || seats < 1) {
            throw new InvalidBookingException(
                    "Fare must be non-negative and seats must be positive"
            );
        }

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Train not found: " + trainId
                        ));

        Schedule schedule = new Schedule();
        schedule.setTrain(train);
        schedule.setJourneyDate(date);
        schedule.setDepartureTime(departure);
        schedule.setArrivalTime(arrival);
        schedule.setFare(fare);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        SeatInventory inventory = new SeatInventory();
        inventory.setSchedule(savedSchedule);
        inventory.setTotalSeats(seats);
        inventory.setAvailableSeats(seats);

        seatInventoryRepository.save(inventory);

        return savedSchedule;
    }

    public List<Schedule> searchTrains(
            String source,
            String destination,
            LocalDate date) {

        if (source == null || source.isBlank()
                || destination == null || destination.isBlank()
                || date == null) {
            throw new InvalidBookingException(
                    "Source, destination and date are required"
            );
        }

        return scheduleRepository
                .findByJourneyDateAndTrain_SourceIgnoreCaseAndTrain_DestinationIgnoreCase(
                        date, source.trim(), destination.trim()
                );
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
}