package com.railway.trainproject.controller;

import com.railway.trainproject.entity.Train;
import com.railway.trainproject.entity.Schedule;
import com.railway.trainproject.service.TrainService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    // GET: http://localhost:8080/api/trains
    @GetMapping
    public List<Train> getAllTrains() {
        return trainService.getAllTrains();
    }

    // GET: /api/trains/search?source=Delhi&destination=Lucknow&date=2026-10-20
    @GetMapping("/search")
    public List<Schedule> searchTrains(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return trainService.searchTrains(
                source,
                destination,
                date
        );
    }
}
