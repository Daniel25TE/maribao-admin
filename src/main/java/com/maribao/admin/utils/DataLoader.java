package com.maribao.admin.utils;

import com.maribao.admin.models.Discount;
import com.maribao.admin.models.Reservation;
import com.maribao.admin.services.DiscountService;
import com.maribao.admin.services.MediaService;
import com.maribao.admin.services.ReservationService;
import com.maribao.admin.services.StatsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// loads sample data when the app starts so the demo has something to show right away.
// this runs automatically before anything else thanks to CommandLineRunner.
@Component
public class DataLoader implements CommandLineRunner {

    private final ReservationService reservationService;
    private final MediaService mediaService;
    private final DiscountService discountService;
    private final StatsService statsService;

    public DataLoader(ReservationService reservationService,
                      MediaService mediaService,
                      DiscountService discountService,
                      StatsService statsService) {
        this.reservationService = reservationService;
        this.mediaService = mediaService;
        this.discountService = discountService;
        this.statsService = statsService;
    }

    @Override
    public void run(String... args) throws Exception {

        // sample reservations — covers all possible statuses for the demo
        reservationService.create(new Reservation(
                "RES-001", "Daniel Llumiquinga", "daniel@test.com", "593986888256",
                "Sol", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5),
                320.00, "pending"
        ));

        reservationService.create(new Reservation(
                "RES-002", "Maria Garcia", "maria@test.com", "593991234567",
                "Luna", LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 15),
                450.00, "active"
        ));

        reservationService.create(new Reservation(
                "RES-003", "Carlos Lopez", "carlos@test.com", "593987654321",
                "Estrella", LocalDate.of(2026, 5, 20), LocalDate.of(2026, 5, 25),
                380.00, "paid"
        ));

        reservationService.create(new Reservation(
                "RES-004", "Ana Torres", "ana@test.com", "593998765432",
                "Surf", LocalDate.of(2026, 6, 20), LocalDate.of(2026, 6, 23),
                270.00, "cancelled"
        ));

        // sample photos for the gallery
        mediaService.addPhoto("PHOTO-001",
                "https://res.cloudinary.com/demo/image/upload/sample.jpg",
                "Vista exterior del hotel", "#Exteriores");

        mediaService.addPhoto("PHOTO-002",
                "https://res.cloudinary.com/demo/image/upload/sample2.jpg",
                "Habitación Sol con vista al mar", "#Sol");

        mediaService.addPhoto("PHOTO-003",
                "https://res.cloudinary.com/demo/image/upload/sample3.jpg",
                "Playa frente al hotel", "#Playa Paraíso");

        // sample videos for the homepage slider
        mediaService.addVideo("VIDEO-001",
                "https://res.cloudinary.com/demo/video/upload/sample.mp4",
                "Video de la playa", "Playa Paraíso");

        mediaService.addVideo("VIDEO-002",
                "https://res.cloudinary.com/demo/video/upload/sample2.mp4",
                "Video del atardecer", "Atardecer en Maribao");

        // sample discounts — Monday to Thursday promo the hotel already uses
        discountService.create(new Discount(
                "DISC-001", LocalDate.of(2026, 6, 1), 15.0, "Lunes a Jueves", true
        ));

        discountService.create(new Discount(
                "DISC-002", LocalDate.of(2026, 6, 2), 15.0, "Lunes a Jueves", true
        ));

        discountService.create(new Discount(
                "DISC-003", LocalDate.of(2026, 6, 3), 15.0, "Lunes a Jueves", false
        ));

        // simulate some website visits for the stats dashboard
        for (int i = 0; i < 10; i++) {
            statsService.logVisit();
        }
    }
}
