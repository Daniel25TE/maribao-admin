package com.maribao.admin.services;

import com.maribao.admin.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// handles all the business logic for reservations.
// for now data lives in memory using an ArrayList — in module 2 this gets replaced with DynamoDB.
@Service
public class ReservationService {

    // in-memory list that holds all reservations while the app is running
    private final List<Reservation> reservations = new ArrayList<>();

    // returns every reservation in the system
    public List<Reservation> getAll() {
        return reservations;
    }

    // finds one reservation by its id, returns null if it doesn't exist
    public Reservation getById(String id) {
        for (Reservation r : reservations) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    // adds a new reservation to the list
    public Reservation create(Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }

    // updates the status of a reservation — e.g. from pending to active
    public Reservation updateStatus(String id, String newStatus) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            reservation.setStatus(newStatus);
        }
        return reservation;
    }

    // removes a reservation permanently
    public boolean delete(String id) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            reservations.remove(reservation);
            return true;
        }
        return false;
    }

    // filters reservations by status — useful for showing only pending or only active ones
    public List<Reservation> getByStatus(String status) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getStatus().equals(status)) {
                result.add(r);
            }
        }
        return result;
    }

    // filters reservations that overlap with a given date range — used to block booked dates on the calendar
    public List<Reservation> getByDateRange(LocalDate from, LocalDate to) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (!r.getCheckOut().isBefore(from) && !r.getCheckIn().isAfter(to)) {
                result.add(r);
            }
        }
        return result;
    }

    // returns a summary of how many reservations exist per status
    public Map<String, Long> getStatusSummary() {
        Map<String, Long> summary = new HashMap<>();
        for (Reservation r : reservations) {
            String status = r.getStatus();
            summary.put(status, summary.getOrDefault(status, 0L) + 1);
        }
        return summary;
    }

    // returns all booked dates for a specific room — used by the booking calendar to block unavailable dates
    public List<LocalDate> getBookedDatesByRoom(String roomName) {
        List<LocalDate> bookedDates = new ArrayList<>();
        for (Reservation r : reservations) {
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

    // saves a guest comment on an existing reservation — called after the guest checks out
    public Reservation addComment(String id, String comment) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            reservation.setComment(comment);
        }
        return reservation;
    }
}
