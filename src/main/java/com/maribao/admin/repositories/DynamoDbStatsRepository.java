package com.maribao.admin.repositories;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// this is where all the DynamoDB calls for visit tracking happen.
// every time someone visits the site, a new row gets added here.
@Repository
public class DynamoDbStatsRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "maribao-visits";

    public DynamoDbStatsRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // logs a new visit — I use a random UUID for the id since visits don't need to be looked up individually
    public void saveVisit() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        item.put("date", AttributeValue.fromS(LocalDate.now().toString()));

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build());
    }

    // scans the whole table and uses the built-in count — no need to loop through items
    public int getTotalVisits() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build());
        return response.count();
    }

    // groups visits by date and counts how many happened each day — the service uses this for both the daily and monthly charts
    public Map<String, Long> getVisitsByDate() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build());

        Map<String, Long> result = new HashMap<>();
        for (Map<String, AttributeValue> item : response.items()) {
            String date = item.get("date").s();
            result.put(date, result.getOrDefault(date, 0L) + 1);
        }
        return result;
    }
}
