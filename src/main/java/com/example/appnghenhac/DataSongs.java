package com.example.appnghenhac;

import java.io.Serializable;
import java.util.List;

public class DataSongs implements Serializable {
    private List<com.example.yourmusic.Song> dsBaiHat;
    private int idBH;

    public DataSongs(List<com.example.yourmusic.Song> dsBaiHat, int idBH) {
        this.dsBaiHat = dsBaiHat;
        this.idBH = idBH;
    }

    public List<com.example.yourmusic.Song> getDsBaiHat() {
        return dsBaiHat;
    }

    public void setDsBaiHat(List<com.example.yourmusic.Song> dsBaiHat) {
        this.dsBaiHat = dsBaiHat;
    }

    public int getIdBH() {
        return idBH;
    }

    public void setIdBH(int idBH) {
        this.idBH = idBH;
    }
}
