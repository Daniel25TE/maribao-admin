package com.maribao.admin.models;

import java.time.LocalDateTime;

// A video uploaded by the hotel owner to show up on the homepage slider.
// Extends MediaItem so it already has id, url, altText and uploadedAt without repeating them here.
public class Video extends MediaItem {

    private String title;   // label shown in the admin panel to identify the video
    private String room;    // which hotel room this video belongs to — same as photos

    public Video(String id, String url, String altText, LocalDateTime uploadedAt, String title, String room) {
        super(id, url, altText, uploadedAt);
        this.title = title;
        this.room = room;
    }

    // tells the rest of the app this media item is a video
    @Override
    public String getMediaType() {
        return "video";
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
