package com.railway.trainproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pnr;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private int seats;

    @Column(nullable = false)
    private double totalFare;

    @Column(nullable = false)
    private String status = "CONFIRMED";

    @Column(nullable = false, updatable = false)
    private LocalDateTime bookingTime;

    public Booking() {
    }

    @PrePersist
    public void beforeSave() {
        if (bookingTime == null) {
            bookingTime = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getPnr() {
        return pnr;
    }

    public User getUser() {
        return user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getSeats() {
        return seats;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}
