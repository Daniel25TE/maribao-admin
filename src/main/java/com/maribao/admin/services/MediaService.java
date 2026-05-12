package com.maribao.admin.services;

import com.maribao.admin.interfaces.MediaRepository;
import com.maribao.admin.models.MediaItem;
import com.maribao.admin.models.Photo;
import com.maribao.admin.models.Video;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// handles all the business logic for photos and videos uploaded through the admin panel.
// for now data lives in memory — in module 2 this gets replaced with DynamoDB + Cloudinary.
@Service
public class MediaService implements MediaRepository {

    // separate lists for photos and videos
    private final List<Photo> photos = new ArrayList<>();
    private final List<Video> videos = new ArrayList<>();

    // --- PHOTOS ---

    // returns all photos uploaded to the gallery
    public List<Photo> getAllPhotos() {
        return photos;
    }

    // adds a new photo — url comes from Cloudinary in module 2, for now it's passed manually
    public Photo addPhoto(String id, String url, String altText, String room) {
        Photo photo = new Photo(id, url, altText, LocalDateTime.now(), room);
        photos.add(photo);
        return photo;
    }

    // removes a photo by id — also needs to delete from Cloudinary in module 2
    public boolean deletePhoto(String id) {
        for (Photo p : photos) {
            if (p.getId().equals(id)) {
                photos.remove(p);
                return true;
            }
        }
        return false;
    }

    // filters photos by room — e.g. only show #Sol photos in the gallery
    public List<Photo> getPhotosByRoom(String room) {
        List<Photo> result = new ArrayList<>();
        for (Photo p : photos) {
            if (p.getRoom().equals(room)) {
                result.add(p);
            }
        }
        return result;
    }

    // --- VIDEOS ---

    // returns all videos uploaded for the homepage slider
    public List<Video> getAllVideos() {
        return videos;
    }

    // adds a new video — url comes from Cloudinary in module 2, for now it's passed manually
    public Video addVideo(String id, String url, String altText, String title) {
        Video video = new Video(id, url, altText, LocalDateTime.now(), title);
        videos.add(video);
        return video;
    }

    // removes a video by id
    public boolean deleteVideo(String id) {
        for (Video v : videos) {
            if (v.getId().equals(id)) {
                videos.remove(v);
                return true;
            }
        }
        return false;
    }

    // --- SHARED ---

    // returns all media items together — photos and videos in one list
    public List<MediaItem> getAllMedia() {
        List<MediaItem> all = new ArrayList<>();
        all.addAll(photos);
        all.addAll(videos);
        return all;
    }
}
