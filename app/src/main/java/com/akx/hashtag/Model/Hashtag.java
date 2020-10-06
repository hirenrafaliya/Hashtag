package com.akx.hashtag.Model;


import androidx.annotation.Keep;

@Keep
public class Hashtag {

    String id;
    String title;
    String tags;

    public Hashtag(){}

    public Hashtag(String id, String title, String tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
