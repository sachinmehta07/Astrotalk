package com.app.astrotalk.model;

import java.util.List;

public class AstrolgerModel {
    private Integer id;
    private String name;
    private int imageD;
    private String astroType;
    private String astroExp;
    private String astroLang;
    private String astroAbout;
    private String address;  // New field for address
    private String phoneNumber;  // New field for phone number
    private List<UserReviewModel> userReviews;

    public AstrolgerModel(Integer id, String name, int imageD, String astroType, String astroExp, String astroLang, String astroAbout, String address, String phoneNumber, List<UserReviewModel> userReviews) {
        this.id = id;
        this.name = name;
        this.imageD = imageD;
        this.astroType = astroType;
        this.astroExp = astroExp;
        this.astroLang = astroLang;
        this.astroAbout = astroAbout;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userReviews = userReviews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageD() {
        return imageD;
    }

    public void setImageD(int imageD) {
        this.imageD = imageD;
    }

    public String getAstroType() {
        return astroType;
    }

    public void setAstroType(String astroType) {
        this.astroType = astroType;
    }

    public String getAstroExp() {
        return astroExp;
    }

    public void setAstroExp(String astroExp) {
        this.astroExp = astroExp;
    }

    public String getAstroLang() {
        return astroLang;
    }

    public void setAstroLang(String astroLang) {
        this.astroLang = astroLang;
    }

    public String getAstroAbout() {
        return astroAbout;
    }

    public void setAstroAbout(String astroAbout) {
        this.astroAbout = astroAbout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserReviewModel> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<UserReviewModel> userReviews) {
        this.userReviews = userReviews;
    }
}
