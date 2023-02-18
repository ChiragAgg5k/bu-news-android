package com.chiragagg5k.bu_news_android;

public class UploadObject {

    private String newsHeading;
    private String newsDescription;
    private String mImageUrl;

    public UploadObject() {
        //empty constructor needed
    }

    public UploadObject(String newsHeading, String newsDescription, String imageUrl) {
        this.newsHeading = newsHeading;
        this.newsDescription = newsDescription;
        this.mImageUrl = imageUrl;
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
}
