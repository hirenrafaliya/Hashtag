package com.akx.hashtag.Model;
import androidx.annotation.Keep;

@Keep
public class Photo {
    private int id;

    private String pageURL;

    private String type;

    private String tags;

    private String previewURL;

    private int previewWidth;

    private int previewHeight;

    private String webformatURL;

    private int webformatWidth;

    private int webformatHeight;

    private String largeImageURL;

    private int imageWidth;

    private int imageHeight;

    private int imageSize;

    private int views;

    private int downloads;

    private int favorites;

    private int likes;

    private int comments;

    private int user_id;

    private String user;

    private String userImageURL;

    public Photo(){}

    public Photo(int id, String pageURL, String type, String tags, String previewURL, int previewWidth, int previewHeight, String webformatURL, int webformatWidth, int webformatHeight, String largeImageURL, int imageWidth, int imageHeight, int imageSize, int views, int downloads, int favorites, int likes, int comments, int user_id, String user, String userImageURL) {
        this.id = id;
        this.pageURL = pageURL;
        this.type = type;
        this.tags = tags;
        this.previewURL = previewURL;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.webformatURL = webformatURL;
        this.webformatWidth = webformatWidth;
        this.webformatHeight = webformatHeight;
        this.largeImageURL = largeImageURL;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageSize = imageSize;
        this.views = views;
        this.downloads = downloads;
        this.favorites = favorites;
        this.likes = likes;
        this.comments = comments;
        this.user_id = user_id;
        this.user = user;
        this.userImageURL = userImageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public int getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(int webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public int getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(int webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }
}
