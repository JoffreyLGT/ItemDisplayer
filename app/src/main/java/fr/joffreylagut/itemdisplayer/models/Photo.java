package fr.joffreylagut.itemdisplayer.models;

import java.net.URL;

/**
 * Photo.java
 * Purpose: Blueprint for a Photo object.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-24
 */

public class Photo {

    // Properties
    private int id;
    private int albumId;
    private String title;
    private URL url;
    private URL thumbnailUrl;

    // Constructors
    public Photo() {
    }

    public Photo(int id, int albumId, String title, URL url, URL thumbnailUrl) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(URL thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
