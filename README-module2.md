# Overview

As a software engineer, I wanted to stop losing data every time the server restarted. In Module 1 the whole backend was working, but all the data lived in memory — reservations, photos, discounts, everything was gone on every restart. I wanted to fix that by connecting the app to a real cloud database, and I wanted to do it with AWS because that is what most backend jobs expect you to know.

This module builds directly on the Java Spring Boot REST API I built in Module 1. The backend already had all the endpoints working, but every time the server restarted, all the data was gone because it was stored in memory. In this module I migrated all that data — reservations, photos, videos, discounts, and visit stats — to AWS DynamoDB so it persists permanently across restarts. The API still works exactly the same from the outside. The only thing that changed is where the data lives.

I built this on top of the Maribao Admin project, which is a real backend I am building for a hotel business. The frontend already exists and is live at maribao.com. Every module I complete in school gets applied directly to this project, which keeps me motivated because the work actually matters to someone.

[Software Demo Video](http://youtube.link.goes.here)

# Cloud Database

I used AWS DynamoDB, a fully managed NoSQL key-value database. I chose it specifically because it has an always-free tier — 25 GB of storage and 25 read/write capacity units forever, with no expiration. For a small project like this I do not want to worry about the database pausing or billing me unexpectedly.

DynamoDB is schemaless, meaning each item in a table can have different fields. The only requirement is a partition key that uniquely identifies each item. I structured the data across five tables:

**maribao-reservations**
- `id` (String, partition key) — unique reservation ID
- `guestName`, `email`, `phone`, `roomName` (String)
- `checkIn`, `checkOut` (String — stored as ISO date, parsed back to LocalDate in Java)
- `totalPrice` (Number)
- `status` (String — pending, confirmed, cancelled)
- `comment` (String — optional)

**maribao-photos**
- `id` (String, partition key)
- `url`, `altText`, `room` (String)
- `uploadedAt` (String — stored as ISO datetime, parsed back to LocalDateTime in Java)

**maribao-videos**
- `id` (String, partition key)
- `url`, `altText`, `title` (String)
- `uploadedAt` (String)

**maribao-discounts**
- `id` (String, partition key)
- `date` (String — ISO date)
- `percentage` (Number)
- `description` (String)
- `active` (Boolean)

**maribao-visits**
- `id` (String, partition key — UUID generated on every visit)
- `date` (String — ISO date of the visit)

# Development Environment

I used IntelliJ IDEA as the IDE. I used the AWS Management Console to create the tables and verify the data was saving correctly. Bruno was used to test all endpoints after the migration. Git and GitHub were used for source control.

The project is written in Java 21 using Spring Boot 3.5.14. Maven manages all dependencies. The following libraries were added for this module on top of what was already in Module 1:

- AWS SDK for Java v2 BOM (`software.amazon.awssdk:bom:2.25.60`) — manages all AWS SDK dependency versions centrally so I do not have to specify versions on individual packages
- `software.amazon.awssdk:dynamodb` — provides the DynamoDbClient and all request/response types for interacting with DynamoDB
- `software.amazon.awssdk:auth` — handles AWS credential loading, including ProfileCredentialsProvider for named profiles
- `software.amazon.awssdk:regions` — provides the Region enum used when building the DynamoDB client

I also used a named AWS profile (`[maribao]` in `~/.aws/credentials`) instead of the default credentials provider because I have work AWS credentials on the same machine and did not want to mix them up.

# Files Changed in This Module

**New files:**
- `src/main/java/com/maribao/admin/config/DynamoDbConfig.java` — creates the DynamoDbClient Spring bean used across all repositories
- `src/main/java/com/maribao/admin/repositories/DynamoDbReservationRepository.java` — all raw DynamoDB operations for the reservations table
- `src/main/java/com/maribao/admin/repositories/DynamoDbMediaRepository.java` — all raw DynamoDB operations for the photos and videos tables
- `src/main/java/com/maribao/admin/repositories/DynamoDbDiscountRepository.java` — all raw DynamoDB operations for the discounts table
- `src/main/java/com/maribao/admin/repositories/DynamoDbStatsRepository.java` — all raw DynamoDB operations for the visits table

**Modified files:**
- `pom.xml` — added the AWS SDK BOM and three AWS dependencies
- `src/main/java/com/maribao/admin/services/ReservationService.java` — removed in-memory ArrayList, now uses DynamoDbReservationRepository
- `src/main/java/com/maribao/admin/services/MediaService.java` — removed in-memory lists, now uses DynamoDbMediaRepository
- `src/main/java/com/maribao/admin/services/DiscountService.java` — removed in-memory ArrayList, now uses DynamoDbDiscountRepository
- `src/main/java/com/maribao/admin/services/StatsService.java` — removed in-memory list, now uses DynamoDbStatsRepository

**Deleted files:**
- `src/main/java/com/maribao/admin/DataLoader.java` — this file pre-loaded sample data on every startup so the app had something to show. With DynamoDB persisting data permanently, it would create duplicate records on every restart so I removed it.

# Useful Websites

- [AWS DynamoDB Developer Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html)
- [AWS SDK for Java v2 Developer Guide](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
- [AWS SDK for Java v2 DynamoDB Examples](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_dynamodb_code_examples.html)
- [Baeldung — DynamoDB with Spring](https://www.baeldung.com/spring-data-dynamodb)
- [AWS Free Tier Details](https://aws.amazon.com/free/)

# Future Work

- Integrate Cloudinary so photo and video uploads go through the backend instead of requiring a manual URL
- Add admin authentication to protect all endpoints — right now anyone who knows the URL can call them
- Implement DynamoDB pagination so large tables do not return everything in a single scan
- Add a GSI (Global Secondary Index) on the reservations table to query by status or date without scanning the full table
- Deploy the backend to AWS Lambda and API Gateway so it runs on always-free serverless infrastructure
