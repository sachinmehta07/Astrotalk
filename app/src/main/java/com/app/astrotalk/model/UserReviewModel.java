package com.app.astrotalk.model;

public class UserReviewModel {
    private int userProfilePic;  // New field for user profile picture
    private String reviewerName;
    private float rating;
    private String reviewText;

    public UserReviewModel(int userProfilePic, String reviewerName, float rating, String reviewText) {
        this.userProfilePic = userProfilePic;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(int userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
