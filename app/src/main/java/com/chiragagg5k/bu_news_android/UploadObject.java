package com.chiragagg5k.bu_news_android;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UploadObject {

    private String newsHeading;
    private String newsDescription;
    private String mImageUrl;
    private String category;
    private boolean authorized;

    private String username;

    public UploadObject() {
        //empty constructor needed
    }

    public UploadObject(String newsHeading, String newsDescription,String category, String imageUrl) {
        this.newsHeading = newsHeading;
        this.newsDescription = newsDescription;
        this.mImageUrl = imageUrl;
        this.category = category;
        this.authorized = false;

        this.username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }
    public String getNewsHeading() {
        return newsHeading;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

}
