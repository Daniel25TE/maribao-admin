package com.maribao.admin.models;

import java.time.LocalDate;

// A discount the hotel owner sets for specific dates through the admin panel.
// These show up highlighted on the booking calendar on the website.
public class Discount {

    private String id;
    private LocalDate date;
    private double percentage;    // how much off, e.g. 15.0 means 15% discount
    private String description;   // a short label like "Lunes a Jueves" to show on the calendar
    private boolean active;       // owner can turn a discount on or off without deleting it

    public Discount(String id, LocalDate date, double percentage, String description, boolean active) {
        this.id = id;
        this.date = date;
        this.percentage = percentage;
        this.description = description;
        this.active = active;
    }

    // takes the original room price and returns the discounted price for this date.
    // if the discount is inactive, it just returns the original price unchanged.
    public double applyDiscount(double originalPrice) {
        if (!active) {
            return originalPrice;
        }
        return originalPrice - (originalPrice * (percentage / 100));
    }

    // id is set once when the discount is created, no setter needed
    public String getId() { return id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
