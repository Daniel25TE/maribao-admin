package com.maribao.admin.controllers;

import com.maribao.admin.services.StatsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// REST endpoints for the analytics section of the admin panel.
// the existing Maribao website also calls /visit every time someone loads a page.
@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    // POST /api/stats/visit — logs a new visit, called from the frontend on every page load
    @PostMapping("/visit")
    public void logVisit() {
        statsService.logVisit();
    }

    // GET /api/stats/total — returns total visit count since the app started
    @GetMapping("/total")
    public int getTotal() {
        return statsService.getTotalVisits();
    }

    // GET /api/stats/today — returns how many people visited today
    @GetMapping("/today")
    public long getToday() {
        return statsService.getVisitsToday();
    }

    // GET /api/stats/per-day — returns visit counts broken down by day
    @GetMapping("/per-day")
    public Map<String, Long> getPerDay() {
        return statsService.getVisitsPerDay();
    }

    // GET /api/stats/per-month — returns visit counts broken down by month
    @GetMapping("/per-month")
    public Map<String, Long> getPerMonth() {
        return statsService.getVisitsPerMonth();
    }

    // GET /api/stats/summary — returns everything at once for the admin dashboard
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return statsService.getSummary();
    }
}
