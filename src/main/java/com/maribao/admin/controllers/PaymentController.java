package com.maribao.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maribao.admin.models.Reservation;
import com.maribao.admin.services.ReservationService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;

    private String stripeSecretKey;
    private String stripeWebhookSecret;
    private String frontendUrl;

    public PaymentController(ReservationService reservationService, ObjectMapper objectMapper) {
        this.reservationService = reservationService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        this.stripeSecretKey = System.getenv("STRIPE_SECRET_KEY");
        this.stripeWebhookSecret = System.getenv("STRIPE_WEBHOOK_SECRET");
        this.frontendUrl = System.getenv("FRONTEND_URL") != null
                ? System.getenv("FRONTEND_URL")
                : "https://maribao.com";

        if (stripeSecretKey != null) {
            Stripe.apiKey = stripeSecretKey;
        }
    }

    // POST /api/payment/create-checkout-session
    // Called by the frontend when the guest picks card payment.
    // Returns a Stripe-hosted checkout URL that the frontend redirects to.
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CheckoutRequest req) {
        try {
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(req.getCurrency() != null ? req.getCurrency() : "usd")
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(req.getDescription() != null ? req.getDescription() : "Reserva Hotel Maribao")
                                            .build())
                                    // Stripe expects amount in cents
                                    .setUnitAmount(Math.round(req.getAmount() * 100))
                                    .build())
                            .setQuantity(1L)
                            .build())
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/maribao/thanks.html?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendUrl + "/maribao/reservar.html");

            // Pass all metadata (includes datosReserva JSON string from the frontend)
            if (req.getMetadata() != null) {
                req.getMetadata().forEach(builder::putMetadata);
            }

            Session session = Session.create(builder.build());
            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error creando la sesión de pago"));
        }
    }

    // GET /api/payment/stripe-session?session_id=cs_...
    // Called by thanks.html after Stripe redirects back.
    // Returns the reservation data embedded in the session metadata so the page can show a confirmation.
    @GetMapping("/stripe-session")
    public ResponseEntity<?> getStripeSession(@RequestParam String session_id) {
        try {
            Session session = Session.retrieve(session_id);
            Map<String, String> metadata = session.getMetadata();

            if (metadata != null && metadata.containsKey("datosReserva")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> datosReserva = objectMapper.readValue(
                        metadata.get("datosReserva"), Map.class);
                return ResponseEntity.ok(Map.of("reserva", datosReserva));
            }
            return ResponseEntity.status(404).body(Map.of("error", "No se encontraron datos de la reserva"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al recuperar datos de Stripe"));
        }
    }

    // POST /api/payment/webhook
    // Stripe calls this when a card payment completes.
    // We verify the signature, then create the reservation in DynamoDB.
    // Must return 200 quickly — Stripe will retry if it times out.
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Webhook Error: " + e.getMessage());
        }

        if ("checkout.session.completed".equals(event.getType())) {
            event.getDataObjectDeserializer().getObject().ifPresent(stripeObject -> {
                Session session = (Session) stripeObject;
                Map<String, String> metadata = session.getMetadata();

                if (metadata != null && metadata.containsKey("datosReserva")) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> datos = objectMapper.readValue(
                                metadata.get("datosReserva"), Map.class);

                        String guestName = datos.getOrDefault("firstName", "") + " "
                                + datos.getOrDefault("lastName", "");
                        String email = (String) datos.getOrDefault("email", "");
                        String phone = (String) datos.getOrDefault("phone", "");
                        String room = (String) datos.getOrDefault("cuarto", "");
                        LocalDate checkIn = LocalDate.parse((String) datos.get("checkin"));
                        LocalDate checkOut = LocalDate.parse((String) datos.get("checkout"));
                        double totalPrice = Double.parseDouble(datos.getOrDefault("totalPrice", "0").toString());

                        Reservation reservation = new Reservation(
                                UUID.randomUUID().toString(),
                                guestName.trim(),
                                email,
                                phone,
                                room,
                                checkIn,
                                checkOut,
                                totalPrice,
                                "active"
                        );
                        reservationService.create(reservation);

                    } catch (Exception e) {
                        // Log but still return 200 — Stripe must not retry for business logic errors
                        System.err.println("Error creating reservation from webhook: " + e.getMessage());
                    }
                }
            });
        }

        return ResponseEntity.ok("{\"received\": true}");
    }

    // Simple DTO for the checkout session request body
    public static class CheckoutRequest {
        private double amount;
        private String currency;
        private String description;
        private Map<String, String> metadata;

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Map<String, String> getMetadata() { return metadata; }
        public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    }
}
