package com.maribao.admin.controllers;

import com.maribao.admin.models.Reservation;
import com.maribao.admin.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// REST endpoints for managing hotel reservations.
// this is what the admin panel frontend calls to view, update and delete reservations.
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    // Spring automatically injects the service here
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // GET /api/reservations — returns all reservations
    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    // GET /api/reservations/{id} — returns one reservation by id
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable String id) {
        Reservation reservation = reservationService.getById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    // POST /api/reservations — creates a new reservation
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation created = reservationService.create(reservation);
        return ResponseEntity.ok(created);
    }

    // PATCH /api/reservations/{id}/status — updates the status of a reservation
    @PatchMapping("/{id}/status")
    public ResponseEntity<Reservation> updateStatus(@PathVariable String id,
                                                    @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        Reservation updated = reservationService.updateStatus(id, newStatus);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/reservations/{id} — deletes a reservation permanently
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = reservationService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // GET /api/reservations/status/{status} — filters reservations by status
    @GetMapping("/status/{status}")
    public List<Reservation> getByStatus(@PathVariable String status) {
        return reservationService.getByStatus(status);
    }

    // GET /api/reservations/range?from=2026-01-01&to=2026-01-31 — gets reservations in a date range
    @GetMapping("/range")
    public List<Reservation> getByDateRange(@RequestParam String from,
                                            @RequestParam String to) {
        return reservationService.getByDateRange(LocalDate.parse(from), LocalDate.parse(to));
    }

    // GET /api/reservations/summary — returns count of reservations per status
    @GetMapping("/summary")
    public Map<String, Long> getSummary() {
        return reservationService.getStatusSummary();
    }

    // GET /api/reservations/booked-dates?roomName=Sol — returns all booked dates for a room
    @GetMapping("/booked-dates")
    public List<LocalDate> getBookedDates(@RequestParam String roomName) {
        return reservationService.getBookedDatesByRoom(roomName);
    }

    // PATCH /api/reservations/{id}/comment — saves a guest review on a reservation
    @PatchMapping("/{id}/comment")
    public ResponseEntity<Reservation> addComment(@PathVariable String id,
                                                  @RequestBody Map<String, String> body) {
        String comment = body.get("comment");
        Reservation updated = reservationService.addComment(id, comment);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
