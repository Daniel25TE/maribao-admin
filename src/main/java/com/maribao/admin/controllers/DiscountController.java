package com.maribao.admin.controllers;

import com.maribao.admin.models.Discount;
import com.maribao.admin.services.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST endpoints for managing hotel discounts.
// the booking calendar on the Maribao website calls /active to highlight discounted dates.
@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "*")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // GET /api/discounts — returns all discounts (admin use)
    @GetMapping
    public List<Discount> getAll() {
        return discountService.getAll();
    }

    // GET /api/discounts/active — returns only active discounts (used by booking calendar)
    @GetMapping("/active")
    public List<Discount> getActive() {
        return discountService.getActive();
    }

    // GET /api/discounts/{id} — returns one discount by id
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getById(@PathVariable String id) {
        Discount discount = discountService.getById(id);
        if (discount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(discount);
    }

    // POST /api/discounts — creates a new discount date
    @PostMapping
    public ResponseEntity<Discount> create(@RequestBody Discount discount) {
        Discount created = discountService.create(discount);
        return ResponseEntity.ok(created);
    }

    // PATCH /api/discounts/{id}/toggle — turns a discount on or off
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Discount> toggle(@PathVariable String id) {
        Discount updated = discountService.toggleActive(id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/discounts/{id} — deletes a discount permanently
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = discountService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
