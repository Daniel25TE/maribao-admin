package com.maribao.admin.repositories;

import com.maribao.admin.models.Discount;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this is where all the DynamoDB calls for discount dates happen.
// the service layer calls this instead of touching DynamoDB directly.
@Repository
public class DynamoDbDiscountRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "maribao-discounts";

    public DynamoDbDiscountRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // stores a new discount date the owner created from the admin panel
    public void save(Discount discount) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(discount.getId()));
        item.put("date", AttributeValue.fromS(discount.getDate().toString()));
        item.put("percentage", AttributeValue.fromN(String.valueOf(discount.getPercentage())));
        item.put("description", AttributeValue.fromS(discount.getDescription()));
        item.put("active", AttributeValue.fromBool(discount.isActive()));

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build());
    }

    // pulls every discount from the table — the service decides which ones are active
    public List<Discount> findAll() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build());

        List<Discount> discounts = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            discounts.add(mapToDiscount(item));
        }
        return discounts;
    }

    // looks up a single discount by id — returns null if it doesn't exist
    public Discount findById(String id) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());

        if (!response.hasItem()) return null;
        return mapToDiscount(response.item());
    }

    // flips the active field on or off without touching the rest of the discount
    public void updateActive(String id, boolean active) {
        dynamoDbClient.updateItem(UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .updateExpression("SET active = :active")
                .expressionAttributeValues(Map.of(":active", AttributeValue.fromBool(active)))
                .build());
    }

    // removes a discount for good — no soft delete, it's gone
    public void delete(String id) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());
    }

    // DynamoDB gives back a raw map, this turns it into a real Discount object
    private Discount mapToDiscount(Map<String, AttributeValue> item) {
        return new Discount(
                item.get("id").s(),
                LocalDate.parse(item.get("date").s()),
                Double.parseDouble(item.get("percentage").n()),
                item.get("description").s(),
                item.get("active").bool()
        );
    }
}
