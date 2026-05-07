package com.maribao.admin.models;

import java.time.LocalDateTime;

// Base class for anything the hotel owner uploads through the admin panel.
// Can't be used directly — Photo and Video extend this and add their own specific fields.
public abstract class MediaItem {

    private String id;
    private String url;          // the Cloudinary URL where the file lives
    private String altText;      // accessibility description for the image or video
    private LocalDateTime uploadedAt;

    public MediaItem(String id, String url, String altText, LocalDateTime uploadedAt) {
        this.id = id;
        this.url = url;
        this.altText = altText;
        this.uploadedAt = uploadedAt;
    }

    // every class that extends this must say what type of media it is
    public abstract String getMediaType();

    // id is set once on upload and never changes, so no setter
    public String getId() { return id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
