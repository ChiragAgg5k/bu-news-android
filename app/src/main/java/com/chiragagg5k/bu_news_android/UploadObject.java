package com.chiragagg5k.bu_news_android;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UploadObject {

    private String newsHeading;
    private String newsDescription;
    private String mImageUrl;
    private String category;
    private boolean authorized;

    private String username;

    public UploadObject(String newsHeading, String newsDescription, String category, String imageUrl) {
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


    public boolean isAuthorized() {
        return authorized;
    }


    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

}
