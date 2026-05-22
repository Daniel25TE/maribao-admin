package com.maribao.admin.repositories;

import com.maribao.admin.models.Reservation;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this is where all the DynamoDB calls for reservations happen.
// the service layer never touches DynamoDB directly, it always goes through here.
@Repository
public class DynamoDbReservationRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "maribao-reservations";

    public DynamoDbReservationRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // stores a new reservation — called when a guest books a room
    public void save(Reservation reservation) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(reservation.getId()));
        item.put("guestName", AttributeValue.fromS(reservation.getGuestName()));
        item.put("email", AttributeValue.fromS(reservation.getEmail()));
        item.put("phone", AttributeValue.fromS(reservation.getPhone()));
        item.put("roomName", AttributeValue.fromS(reservation.getRoomName()));
        item.put("checkIn", AttributeValue.fromS(reservation.getCheckIn().toString()));
        item.put("checkOut", AttributeValue.fromS(reservation.getCheckOut().toString()));
        item.put("totalPrice", AttributeValue.fromN(String.valueOf(reservation.getTotalPrice())));
        item.put("status", AttributeValue.fromS(reservation.getStatus()));
        if (reservation.getComment() != null) {
            item.put("comment", AttributeValue.fromS(reservation.getComment()));
        }

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build());
    }

    // pulls every reservation from the table — the service filters them down from there
    public List<Reservation> findAll() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build());

        List<Reservation> reservations = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            reservations.add(mapToReservation(item));
        }
        return reservations;
    }

    // looks up a single reservation by id — returns null if it doesn't exist
    public Reservation findById(String id) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());

        if (!response.hasItem()) return null;
        return mapToReservation(response.item());
    }

    // updates just the status field — "status" is a reserved word in DynamoDB so I use the #s alias to get around that
    public void updateStatus(String id, String newStatus) {
        dynamoDbClient.updateItem(UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .updateExpression("SET #s = :status")
                .expressionAttributeNames(Map.of("#s", "status"))
                .expressionAttributeValues(Map.of(":status", AttributeValue.fromS(newStatus)))
                .build());
    }

    // saves the guest's comment on an existing reservation — same alias trick as status
    public void updateComment(String id, String comment) {
        dynamoDbClient.updateItem(UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .updateExpression("SET #c = :comment")
                .expressionAttributeNames(Map.of("#c", "comment"))
                .expressionAttributeValues(Map.of(":comment", AttributeValue.fromS(comment)))
                .build());
    }

    // removes a reservation for good — no soft delete, it's gone
    public void delete(String id) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());
    }

    // DynamoDB gives back a raw map of AttributeValues, this turns it into a real Reservation object
    private Reservation mapToReservation(Map<String, AttributeValue> item) {
        return new Reservation(
                item.get("id").s(),
                item.get("guestName").s(),
                item.get("email").s(),
                item.get("phone").s(),
                item.get("roomName").s(),
                java.time.LocalDate.parse(item.get("checkIn").s()),
                java.time.LocalDate.parse(item.get("checkOut").s()),
                Double.parseDouble(item.get("totalPrice").n()),
                item.get("status").s()
        );
    }
}
