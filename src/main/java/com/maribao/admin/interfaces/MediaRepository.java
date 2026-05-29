package com.maribao.admin.interfaces;

import com.maribao.admin.models.Photo;
import com.maribao.admin.models.Video;

import java.util.List;

// any class that manages photos and videos in this app must follow this contract.
// MediaService implements this using DynamoDB since module 2.
public interface MediaRepository {

    // photos
    List<Photo> getAllPhotos();
    Photo addPhoto(String id, String url, String altText, String room);
    boolean deletePhoto(String id);
    List<Photo> getPhotosByRoom(String room);

    // videos
    List<Video> getAllVideos();
    Video addVideo(String id, String url, String altText, String title, String room);
    boolean deleteVideo(String id);
}
