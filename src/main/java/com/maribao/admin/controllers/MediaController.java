package com.maribao.admin.controllers;

import com.maribao.admin.models.MediaItem;
import com.maribao.admin.models.Photo;
import com.maribao.admin.models.Video;
import com.maribao.admin.services.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// exposes all media operations as REST endpoints.
// this is what the React admin panel uses to upload and manage photos and videos.
@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // GET /api/media — returns all photos and videos together
    @GetMapping
    public List<MediaItem> getAllMedia() {
        return mediaService.getAllMedia();
    }

    // --- PHOTOS ---

    // GET /api/media/photos — returns all photos
    @GetMapping("/photos")
    public List<Photo> getAllPhotos() {
        return mediaService.getAllPhotos();
    }

    // GET /api/media/photos/room/{room} — returns photos filtered by room
    @GetMapping("/photos/room/{room}")
    public List<Photo> getPhotosByRoom(@PathVariable String room) {
        return mediaService.getPhotosByRoom(room);
    }

    // POST /api/media/photos — adds a new photo
    @PostMapping("/photos")
    public ResponseEntity<Photo> addPhoto(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        String url = body.get("url");
        String altText = body.get("altText");
        String room = body.get("room");
        Photo photo = mediaService.addPhoto(id, url, altText, room);
        return ResponseEntity.ok(photo);
    }

    // DELETE /api/media/photos/{id} — deletes a photo
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable String id) {
        boolean deleted = mediaService.deletePhoto(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // --- VIDEOS ---

    // GET /api/media/videos — returns all videos
    @GetMapping("/videos")
    public List<Video> getAllVideos() {
        return mediaService.getAllVideos();
    }

    // POST /api/media/videos — adds a new video
    @PostMapping("/videos")
    public ResponseEntity<Video> addVideo(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        String url = body.get("url");
        String altText = body.get("altText");
        String title = body.get("title");
        Video video = mediaService.addVideo(id, url, altText, title);
        return ResponseEntity.ok(video);
    }

    // DELETE /api/media/videos/{id} — deletes a video
    @DeleteMapping("/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        boolean deleted = mediaService.deleteVideo(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
