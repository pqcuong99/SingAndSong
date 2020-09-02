package com.example.yourmusic;

import java.io.Serializable;

public class Song implements Serializable {

    private long id;
    private String title;
    private String artist;
    private String uri;
    private String image;

    public Song() {
    }

    public Song(long id, String title, String artist, String uri, String image) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.uri = uri;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
