package com.chiragagg5k.bu_news_android.objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewsObject {

    private final String newsHeading;
    private final String newsDescription;
    private final String mImageUrl;
    private final String category;
    private boolean authorized;
    private final String username;
    private final boolean promoted;

    public NewsObject(String newsHeading, String newsDescription, String category, String imageUrl) {
        this.authorized = false;
        this.promoted = false;
        this.category = category;
        this.mImageUrl = imageUrl;
        this.newsDescription = newsDescription;
        this.newsHeading = newsHeading;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.username = user.getDisplayName();
        } else {
            this.username = "Unable to fetch username";
        }
    }

    public NewsObject() {
        // !IMPORTANT: This constructor is required for Firebase to work

        this("", "", "", "");
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

    public boolean isPromoted() {
        return promoted;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

}
