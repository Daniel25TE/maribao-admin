package com.maribao.admin.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// tracks every visit to the Maribao website so the owner can see traffic in the admin panel.
// for now visits live in memory — in module 2 this moves to DynamoDB.
@Service
public class StatsService {

    // each entry is a date string representing one visit e.g. "2026-05-11"
    private final List<String> visits = new ArrayList<>();

    // logs a new visit for today — called every time someone lands on the website
    public void logVisit() {
        visits.add(LocalDate.now().toString());
    }

    // returns the total number of visits since the app started
    public int getTotalVisits() {
        return visits.size();
    }

    // returns how many visits happened today
    public long getVisitsToday() {
        String today = LocalDate.now().toString();
        long count = 0;
        for (String visit : visits) {
            if (visit.equals(today)) {
                count++;
            }
        }
        return count;
    }

    // returns visit counts grouped by day — used to build the daily chart in the admin panel
    public Map<String, Long> getVisitsPerDay() {
        Map<String, Long> result = new HashMap<>();
        for (String visit : visits) {
            result.put(visit, result.getOrDefault(visit, 0L) + 1);
        }
        return result;
    }

    // returns visit counts grouped by month e.g. "2026-05" -> 142 visits
    public Map<String, Long> getVisitsPerMonth() {
        Map<String, Long> result = new HashMap<>();
        for (String visit : visits) {
            String month = visit.substring(0, 7); // grabs "2026-05" from "2026-05-11"
            result.put(month, result.getOrDefault(month, 0L) + 1);
        }
        return result;
    }

    // returns a full summary the admin dashboard shows at a glance
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", getTotalVisits());
        summary.put("today", getVisitsToday());
        summary.put("perDay", getVisitsPerDay());
        summary.put("perMonth", getVisitsPerMonth());
        return summary;
    }
}
