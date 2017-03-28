package fr.joffreylagut.itemdisplayer.models;

import java.util.List;

/**
 * Album.java
 * Purpose: Blueprint for an Album object.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-28
 */

public class Album {

    // Properties
    private int id;
    private List<Photo> photos;

    // Constructors
    public Album() {
    }

    public Album(int id, List<Photo> photos) {
        this.id = id;
        this.photos = photos;
    }

    // Getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
