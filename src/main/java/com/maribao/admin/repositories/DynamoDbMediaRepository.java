package com.maribao.admin.repositories;

import com.maribao.admin.models.Photo;
import com.maribao.admin.models.Video;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this is where all the DynamoDB calls for photos and videos happen.
// I kept photos and videos in the same class since they're both media and the operations are almost identical.
@Repository
public class DynamoDbMediaRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String PHOTOS_TABLE = "maribao-photos";
    private static final String VIDEOS_TABLE = "maribao-videos";

    public DynamoDbMediaRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // --- PHOTOS ---

    // stores a new photo — the url comes from Cloudinary, we just save the reference here
    public void savePhoto(Photo photo) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(photo.getId()));
        item.put("url", AttributeValue.fromS(photo.getUrl()));
        item.put("altText", AttributeValue.fromS(photo.getAltText()));
        item.put("room", AttributeValue.fromS(photo.getRoom()));
        item.put("uploadedAt", AttributeValue.fromS(photo.getUploadedAt().toString()));

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(PHOTOS_TABLE)
                .item(item)
                .build());
    }

    // pulls all photos from the table — the service filters by room from there
    public List<Photo> findAllPhotos() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(PHOTOS_TABLE)
                .build());

        List<Photo> photos = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            photos.add(mapToPhoto(item));
        }
        return photos;
    }

    // removes the photo record from DynamoDB — does not delete it from Cloudinary
    public void deletePhoto(String id) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(PHOTOS_TABLE)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());
    }

    // --- VIDEOS ---

    // stores a new video — same idea as photos, the url comes from Cloudinary
    public void saveVideo(Video video) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(video.getId()));
        item.put("url", AttributeValue.fromS(video.getUrl()));
        item.put("altText", AttributeValue.fromS(video.getAltText()));
        item.put("title", AttributeValue.fromS(video.getTitle()));
        item.put("uploadedAt", AttributeValue.fromS(video.getUploadedAt().toString()));

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(VIDEOS_TABLE)
                .item(item)
                .build());
    }

    // pulls all videos from the table
    public List<Video> findAllVideos() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(VIDEOS_TABLE)
                .build());

        List<Video> videos = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            videos.add(mapToVideo(item));
        }
        return videos;
    }

    // removes the video record from DynamoDB — does not delete it from Cloudinary
    public void deleteVideo(String id) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(VIDEOS_TABLE)
                .key(Map.of("id", AttributeValue.fromS(id)))
                .build());
    }

    // --- MAPPERS ---

    // DynamoDB gives back a raw map, this turns it into a real Photo object
    private Photo mapToPhoto(Map<String, AttributeValue> item) {
        return new Photo(
                item.get("id").s(),
                item.get("url").s(),
                item.get("altText").s(),
                LocalDateTime.parse(item.get("uploadedAt").s()),
                item.get("room").s()
        );
    }

    // same as mapToPhoto but for videos
    private Video mapToVideo(Map<String, AttributeValue> item) {
        return new Video(
                item.get("id").s(),
                item.get("url").s(),
                item.get("altText").s(),
                LocalDateTime.parse(item.get("uploadedAt").s()),
                item.get("title").s()
        );
    }
}
