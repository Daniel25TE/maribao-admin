package com.maribao.admin.services;

import com.maribao.admin.models.Reservation;
import com.maribao.admin.repositories.DynamoDbReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// handles all the business logic for reservations.
// used to store everything in a list in memory — now it goes through DynamoDB so data survives restarts.
@Service
public class ReservationService {

    private final DynamoDbReservationRepository repository;

    public ReservationService(DynamoDbReservationRepository repository) {
        this.repository = repository;
    }

    // returns all reservations — used in the admin panel table
    public List<Reservation> getAll() {
        return repository.findAll();
    }

    // looks up one reservation by id — returns null if it doesn't exist
    public Reservation getById(String id) {
        return repository.findById(id);
    }

    // saves a new reservation — called when a guest books through the website
    public Reservation create(Reservation reservation) {
        repository.save(reservation);
        return reservation;
    }

    // changes the status of a reservation — e.g. from pending to confirmed
    public Reservation updateStatus(String id, String newStatus) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            repository.updateStatus(id, newStatus);
            reservation.setStatus(newStatus);
        }
        return reservation;
    }

    // deletes a reservation for good — only if it actually exists
    public boolean delete(String id) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            repository.delete(id);
            return true;
        }
        return false;
    }

    // filters reservations by status — loops through everything and returns the ones that match
    public List<Reservation> getByStatus(String status) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : repository.findAll()) {
            if (r.getStatus().equals(status)) {
                result.add(r);
            }
        }
        return result;
    }

    // returns reservations that overlap with a date range — the booking calendar uses this to block unavailable dates
    public List<Reservation> getByDateRange(LocalDate from, LocalDate to) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : repository.findAll()) {
            if (!r.getCheckOut().isBefore(from) && !r.getCheckIn().isAfter(to)) {
                result.add(r);
            }
        }
        return result;
    }

    // counts how many reservations exist per status — e.g. pending: 3, confirmed: 10
    public Map<String, Long> getStatusSummary() {
        Map<String, Long> summary = new HashMap<>();
        for (Reservation r : repository.findAll()) {
            String status = r.getStatus();
            summary.put(status, summary.getOrDefault(status, 0L) + 1);
        }
        return summary;
    }

    // expands each reservation into individual dates for a specific room — the booking calendar uses this to show which days are taken
    public List<LocalDate> getBookedDatesByRoom(String roomName) {
        List<LocalDate> bookedDates = new ArrayList<>();
        for (Reservation r : repository.findAll()) {
            if (r.getRoomName().equals(roomName) && !r.getStatus().equals("cancelled")) {
                LocalDate date = r.getCheckIn();
                while (!date.isAfter(r.getCheckOut())) {
                    bookedDates.add(date);
                    date = date.plusDays(1);
                }
            }
        }
        return bookedDates;
    }

    // attaches a comment to a reservation — guests use this to leave notes when booking
    public Reservation addComment(String id, String comment) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            repository.updateComment(id, comment);
            reservation.setComment(comment);
        }
        return reservation;
    }
}
