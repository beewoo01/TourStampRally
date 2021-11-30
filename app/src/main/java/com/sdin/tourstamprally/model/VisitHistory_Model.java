package com.sdin.tourstamprally.model;


public class VisitHistory_Model {
    private int spot_idx;
    private String spot_name;
    private String spot_explan;
    private String date;


    private boolean isExpanded = false;

    private String reviewText;
    private String reviewName;
    private String reviewProfile;
    private String reviewContents;

    private int reviewScore;

    public VisitHistory_Model() {
    }

    public VisitHistory_Model(int spot_idx, String spot_name, String spot_explan, String date,
                              boolean isExpanded, String reviewText, String reviewName, String reviewProfile,
                              String reviewContents, int reviewScore) {
        this.spot_idx = spot_idx;
        this.spot_name = spot_name;
        this.spot_explan = spot_explan;
        this.date = date;
        this.isExpanded = isExpanded;
        this.reviewText = reviewText;
        this.reviewName = reviewName;
        this.reviewProfile = reviewProfile;
        this.reviewContents = reviewContents;
        this.reviewScore = reviewScore;
    }

    public int getSpot_idx() {
        return spot_idx;
    }

    public void setSpot_idx(int spot_idx) {
        this.spot_idx = spot_idx;
    }

    public String getSpot_name() {
        return spot_name;
    }

    public void setSpot_name(String spot_name) {
        this.spot_name = spot_name;
    }

    public String getSpot_explan() {
        return spot_explan;
    }

    public void setSpot_explan(String spot_explan) {
        this.spot_explan = spot_explan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getReviewProfile() {
        return reviewProfile;
    }

    public void setReviewProfile(String reviewProfile) {
        this.reviewProfile = reviewProfile;
    }

    public String getReviewContents() {
        return reviewContents;
    }

    public void setReviewContents(String reviewContents) {
        this.reviewContents = reviewContents;
    }

    public int getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(int reviewScore) {
        this.reviewScore = reviewScore;
    }
}
