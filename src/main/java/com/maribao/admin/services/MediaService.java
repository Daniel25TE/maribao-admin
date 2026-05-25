package com.maribao.admin.services;

import com.maribao.admin.interfaces.MediaRepository;
import com.maribao.admin.models.MediaItem;
import com.maribao.admin.models.Photo;
import com.maribao.admin.models.Video;
import com.maribao.admin.repositories.DynamoDbMediaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// handles all the business logic for photos and videos uploaded through the admin panel.
// used to store everything in two lists in memory — now it goes through DynamoDB so the gallery persists.
@Service
public class MediaService implements MediaRepository {

    private final DynamoDbMediaRepository repository;

    public MediaService(DynamoDbMediaRepository repository) {
        this.repository = repository;
    }

    // --- PHOTOS ---

    // returns all photos — used in the gallery page
    public List<Photo> getAllPhotos() {
        return repository.findAllPhotos();
    }

    // creates a new photo record — the url comes from Cloudinary after the owner uploads the image
    public Photo addPhoto(String id, String url, String altText, String room) {
        Photo photo = new Photo(id, url, altText, LocalDateTime.now(), room);
        repository.savePhoto(photo);
        return photo;
    }

    // removes a photo record — does not delete it from Cloudinary
    public boolean deletePhoto(String id) {
        repository.deletePhoto(id);
        return true;
    }

    // filters photos by room — used when the gallery page shows photos for a specific room
    public List<Photo> getPhotosByRoom(String room) {
        List<Photo> result = new ArrayList<>();
        for (Photo p : repository.findAllPhotos()) {
            if (p.getRoom().equals(room)) {
                result.add(p);
            }
        }
        return result;
    }

    // --- VIDEOS ---

    // returns all videos — used in the gallery page
    public List<Video> getAllVideos() {
        return repository.findAllVideos();
    }

    // creates a new video record — the url comes from Cloudinary after the owner uploads the video
    public Video addVideo(String id, String url, String altText, String title) {
        Video video = new Video(id, url, altText, LocalDateTime.now(), title);
        repository.saveVideo(video);
        return video;
    }

    // removes a video record — does not delete it from Cloudinary
    public boolean deleteVideo(String id) {
        repository.deleteVideo(id);
        return true;
    }

    // --- SHARED ---

    // combines photos and videos into one list — used when the admin panel shows all media together
    public List<MediaItem> getAllMedia() {
        List<MediaItem> all = new ArrayList<>();
        all.addAll(repository.findAllPhotos());
        all.addAll(repository.findAllVideos());
        return all;
    }
}
