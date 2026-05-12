package com.maribao.admin.services;

import com.maribao.admin.models.Discount;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// handles all the business logic for discount dates set by the hotel owner.
// these discounts show up highlighted on the booking calendar on the website.
@Service
public class DiscountService {

    // in-memory list of all discounts while the app is running
    private final List<Discount> discounts = new ArrayList<>();

    // returns every discount the owner has created, active or not — used in the admin panel table
    public List<Discount> getAll() {
        return discounts;
    }

    // returns only the discounts that are currently active — this is what the booking calendar uses
    public List<Discount> getActive() {
        List<Discount> result = new ArrayList<>();
        for (Discount d : discounts) {
            if (d.isActive()) {
                result.add(d);
            }
        }
        return result;
    }

    // saves a new discount date the owner set from the admin panel
    public Discount create(Discount discount) {
        discounts.add(discount);
        return discount;
    }

    // looks up a single discount by its id — used internally by toggle and delete
    public Discount getById(String id) {
        for (Discount d : discounts) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    // toggles a discount on or off without deleting it
    public Discount toggleActive(String id) {
        Discount discount = getById(id);
        if (discount != null) {
            discount.setActive(!discount.isActive());
        }
        return discount;
    }

    // deletes a discount completely — owner uses this when a promo is no longer needed at all
    public boolean delete(String id) {
        Discount discount = getById(id);
        if (discount != null) {
            discounts.remove(discount);
            return true;
        }
        return false;
    }

    // checks if a specific date has an active discount and returns the discounted price
    public double getPriceForDate(LocalDate date, double originalPrice) {
        for (Discount d : discounts) {
            if (d.isActive() && d.getDate().equals(date)) {
                return d.applyDiscount(originalPrice);
            }
        }
        return originalPrice;
    }
}
