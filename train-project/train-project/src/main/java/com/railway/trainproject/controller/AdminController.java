package com.railway.trainproject.controller;

import com.railway.trainproject.entity.Train;
import com.railway.trainproject.entity.Schedule;
import com.railway.trainproject.service.TrainService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final TrainService trainService;

    public AdminController(TrainService trainService) {
        this.trainService = trainService;
    }

    // POST: http://localhost:8080/api/admin/trains
    @PostMapping("/trains")
    public Train addTrain(@RequestBody Train train) {
        return trainService.addTrain(train);
    }

    // POST: /api/admin/schedules?trainId=1&date=2026-10-20&departure=08:00:00&arrival=14:00:00&fare=500&seats=100
    @PostMapping("/schedules")
    public Schedule addSchedule(
            @RequestParam Long trainId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime departure,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime arrival,

            @RequestParam double fare,
            @RequestParam int seats) {

        return trainService.addSchedule(
                trainId,
                date,
                departure,
                arrival,
                fare,
                seats
        );
    }
}