package com.akx.hashtag.Model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Keep;

@Keep
public class Quote {

    @SerializedName("content")
    String quote;

    @SerializedName("language_code")
    String language;

    public Quote(String quote, String language) {
        this.quote = quote;
        this.language = language;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
