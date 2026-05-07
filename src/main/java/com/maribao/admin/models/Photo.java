package com.maribao.admin.models;

import java.time.LocalDateTime;

// A photo uploaded by the hotel owner to show up in the gallery page.
// Extends MediaItem so it already has id, url, altText and uploadedAt without repeating them here.
public class Photo extends MediaItem {

    private String room;   // which room or section this photo belongs to e.g. #Sol, #Luna, #Exteriores

    public Photo(String id, String url, String altText, LocalDateTime uploadedAt, String room) {
        super(id, url, altText, uploadedAt);
        this.room = room;
    }

    // tells the rest of the app this media item is a photo
    @Override
    public String getMediaType() {
        return "photo";
    }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
