package com.akx.hashtag.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import androidx.annotation.Keep;

@Keep
public class Pixabay {

    int total;
    int totalHits;
    @SerializedName("hits")
    List<Photo> photos;

    public Pixabay(){}

    public Pixabay(int total, int totalHits, List<Photo> photos) {
        this.total = total;
        this.totalHits = totalHits;
        this.photos = photos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
