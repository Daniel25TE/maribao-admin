package com.maribao.admin.services;

import com.maribao.admin.repositories.DynamoDbStatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// tracks every visit to the Maribao website so the owner can see traffic in the admin panel.
// used to store visits in a list in memory — now it goes through DynamoDB so the count survives restarts.
@Service
public class StatsService {

    private final DynamoDbStatsRepository repository;

    public StatsService(DynamoDbStatsRepository repository) {
        this.repository = repository;
    }

    // logs a new visit — called every time someone lands on the website
    public void logVisit() {
        repository.saveVisit();
    }

    // returns the total visit count — the admin dashboard shows this as the top-level number
    public int getTotalVisits() {
        return repository.getTotalVisits();
    }

    // looks up today's date in the grouped results and returns the count — defaults to 0 if nobody visited yet
    public long getVisitsToday() {
        String today = LocalDate.now().toString();
        Map<String, Long> byDate = repository.getVisitsByDate();
        return byDate.getOrDefault(today, 0L);
    }

    // returns visit counts grouped by day — the admin panel uses this to build the daily chart
    public Map<String, Long> getVisitsPerDay() {
        return repository.getVisitsByDate();
    }

    // groups daily visits into monthly totals — e.g. "2026-05" → 142 visits
    public Map<String, Long> getVisitsPerMonth() {
        Map<String, Long> byDate = repository.getVisitsByDate();
        Map<String, Long> result = new HashMap<>();
        for (Map.Entry<String, Long> entry : byDate.entrySet()) {
            String month = entry.getKey().substring(0, 7); // grabs "2026-05" from "2026-05-18"
            result.put(month, result.getOrDefault(month, 0L) + entry.getValue());
        }
        return result;
    }

    // puts together the full stats summary the admin dashboard shows at a glance
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", getTotalVisits());
        summary.put("today", getVisitsToday());
        summary.put("perDay", getVisitsPerDay());
        summary.put("perMonth", getVisitsPerMonth());
        return summary;
    }
}
