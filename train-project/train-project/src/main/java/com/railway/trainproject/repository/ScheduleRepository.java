package com.railway.trainproject.repository;

import com.railway.trainproject.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Journey date, source aur destination se schedules search karna
    List<Schedule>
    findByJourneyDateAndTrain_SourceIgnoreCaseAndTrain_DestinationIgnoreCase(
            LocalDate journeyDate,
            String source,
            String destination
    );
}