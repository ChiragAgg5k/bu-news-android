package com.chiragagg5k.bu_news_android.objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Object to store news data
 *
 * @author Chirag Aggarwal
 */
public class NewsObject {

    private final String newsHeading;
    private final String newsDescription;
    private final String mImageUrl;
    private final String category;
    private final String username;
    private final long dateInMilliseconds;
    private final boolean promoted;
    private final boolean authorized;

    public NewsObject(String newsHeading, String newsDescription, String category, String imageUrl, long date) {
        this.authorized = true;
        this.promoted = false;
        this.category = category;
        this.mImageUrl = imageUrl;
        this.newsDescription = newsDescription;
        this.newsHeading = newsHeading;
        this.dateInMilliseconds = date;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.username = user.getDisplayName();
        } else {
            this.username = "Anonymous";
        }
    }

    @SuppressWarnings("unused")
    public NewsObject() {
        // !IMPORTANT: This constructor is required for Firebase to work

        this("", "", "", "", 0);
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

    public long getDateInMilliseconds() {
        return dateInMilliseconds;
    }

}
