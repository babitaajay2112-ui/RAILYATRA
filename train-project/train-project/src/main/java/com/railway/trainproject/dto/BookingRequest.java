package com.railway.trainproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingRequest {

    @NotNull(message = "Schedule ID is required")
    @Min(value = 1, message = "Schedule ID must be positive")
    private Long scheduleId;

    @Min(value = 1, message = "At least one seat must be booked")
    private int seats;

    public BookingRequest() {
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
