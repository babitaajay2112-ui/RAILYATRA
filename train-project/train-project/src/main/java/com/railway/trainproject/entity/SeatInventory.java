package com.railway.trainproject.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "seat_inventory",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_inventory_schedule",
                columnNames = "schedule_id"
        )
)
public class SeatInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    public SeatInventory() {
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
