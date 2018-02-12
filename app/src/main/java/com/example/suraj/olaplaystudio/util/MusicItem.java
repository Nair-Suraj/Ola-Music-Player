package com.example.suraj.olaplaystudio.util;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by suraj on 16/12/17.
 */
public class MusicItem implements Serializable{

    private String cover_image;

    private  String song;
    private boolean favorite=false;

    private String artists;
    private  String url;


    public MusicItem(String song, String url, String artists, String cover_image) {
        this.cover_image = cover_image;
        this.song = song;
        this.artists = artists;
        this.url = url;
    }

    public String getCover() {
        return cover_image;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artists;
    }

    public String getSongUrl() {
        return url;
    }
    public void setCover_image(String cover_image){
            this.cover_image=cover_image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


}
