package com.maribao.admin.models;

import java.time.LocalDate;

// This class holds all the data for a single hotel reservation.
// Every time a guest books a room on the Maribao website, one of these gets created.
public class Reservation {

    private String id;
    private String guestName;
    private String email;
    private String phone;
    private String roomName;       // matches the room names from the website: Sol, Luna, Surf, Estrella
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;
    private String status;         // where the reservation is in its lifecycle: pending, active, paid, cancelled

    public Reservation(String id, String guestName, String email, String phone,
                       String roomName, LocalDate checkIn, LocalDate checkOut,
                       double totalPrice, String status) {
        this.id = id;
        this.guestName = guestName;
        this.email = email;
        this.phone = phone;
        this.roomName = roomName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // figures out how many nights the guest is staying based on check-in and check-out dates
    public long getNights() {
        return checkIn.until(checkOut).getDays();
    }

    // id is read-only once the reservation is created, so no setter here
    public String getId() { return id; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
