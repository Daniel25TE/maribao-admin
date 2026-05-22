package com.maribao.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

// creates the DynamoDB client that the whole app uses to talk to AWS.
// I use the "maribao" profile from ~/.aws/credentials because I also have work credentials on this machine and I don't want to mix them up.
@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("maribao"))
                .build();
    }
}
