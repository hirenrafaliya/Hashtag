package com.akx.hashtag.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Keep;

@Keep
public class Caption {

    String source;

    @SerializedName("quotes")
    List<String> captionList;

    public Caption(){}

    public Caption(String source, List<String> captionList) {
        this.source = source;
        this.captionList = captionList;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getCaptionList() {
        return captionList;
    }

    public void setCaptionList(List<String> captionList) {
        this.captionList = captionList;
    }
}
