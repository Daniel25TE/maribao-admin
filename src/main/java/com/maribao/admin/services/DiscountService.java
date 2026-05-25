package com.maribao.admin.services;

import com.maribao.admin.models.Discount;
import com.maribao.admin.repositories.DynamoDbDiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// handles all the business logic for discount dates set by the hotel owner.
// used to store everything in a list in memory — now it goes through DynamoDB so discounts don't disappear on restart.
@Service
public class DiscountService {

    private final DynamoDbDiscountRepository repository;

    public DiscountService(DynamoDbDiscountRepository repository) {
        this.repository = repository;
    }

    // returns all discounts, active or not — used in the admin panel table so the owner can see everything
    public List<Discount> getAll() {
        return repository.findAll();
    }

    // returns only the active discounts — the booking calendar uses this to show discounted prices
    public List<Discount> getActive() {
        List<Discount> result = new ArrayList<>();
        for (Discount d : repository.findAll()) {
            if (d.isActive()) {
                result.add(d);
            }
        }
        return result;
    }

    // stores a new discount the owner created from the admin panel
    public Discount create(Discount discount) {
        repository.save(discount);
        return discount;
    }

    // looks up a single discount by id — used internally by toggle and delete
    public Discount getById(String id) {
        return repository.findById(id);
    }

    // flips a discount on or off without deleting it — the owner uses this to temporarily disable a discount
    public Discount toggleActive(String id) {
        Discount discount = repository.findById(id);
        if (discount != null) {
            boolean newActive = !discount.isActive();
            repository.updateActive(id, newActive);
            discount.setActive(newActive);
        }
        return discount;
    }

    // deletes a discount for good — returns false if it doesn't exist
    public boolean delete(String id) {
        Discount discount = repository.findById(id);
        if (discount != null) {
            repository.delete(id);
            return true;
        }
        return false;
    }

    // checks if a date has an active discount and returns the adjusted price — if no discount found, returns the original price
    public double getPriceForDate(LocalDate date, double originalPrice) {
        for (Discount d : repository.findAll()) {
            if (d.isActive() && d.getDate().equals(date)) {
                return d.applyDiscount(originalPrice);
            }
        }
        return originalPrice;
    }
}
