package com.example.model;

/**
 * Created by لا اله الا الله on 23/01/2016.
 */
public class Review_Data {
    private String Author;
    private String Review;
    private String url;

    public void setAuthor(String author) {
        Author = author;
    }

    public void setReview(String review) {
        Review = review;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return Author;
    }

    public String getReview() {
        return Review;
    }

    public String getUrl() {
        return url;
    }
}
