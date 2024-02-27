package com.app.astrotalk.model;

public class PoojaBookModel {

    int poojaImg;
    int poojaId;

    public int getPoojaId() {
        return poojaId;
    }

    public void setPoojaId(int poojaId) {
        this.poojaId = poojaId;
    }

    public PoojaBookModel(int poojaImg, int poojaId, String poojaName, String poojaDesc, String poojaTitle, String poojaBenefits, String poojaGodDetails) {
        this.poojaImg = poojaImg;
        this.poojaId = poojaId;
        this.poojaName = poojaName;
        this.poojaDesc = poojaDesc;
        this.poojaTitle = poojaTitle;
        this.poojaBenefits = poojaBenefits;
        this.poojaGodDetails = poojaGodDetails;
    }

    String poojaName;

    String poojaDesc;

    String poojaTitle;

    String poojaBenefits;

    String poojaGodDetails;



    public int getPoojaImg() {
        return poojaImg;
    }

    public void setPoojaImg(int poojaImg) {
        this.poojaImg = poojaImg;
    }

    public String getPoojaName() {
        return poojaName;
    }

    public void setPoojaName(String poojaName) {
        this.poojaName = poojaName;
    }

    public String getPoojaDesc() {
        return poojaDesc;
    }

    public void setPoojaDesc(String poojaDesc) {
        this.poojaDesc = poojaDesc;
    }

    public String getPoojaTitle() {
        return poojaTitle;
    }

    public void setPoojaTitle(String poojaTitle) {
        this.poojaTitle = poojaTitle;
    }

    public String getPoojaBenefits() {
        return poojaBenefits;
    }

    public void setPoojaBenefits(String poojaBenefits) {
        this.poojaBenefits = poojaBenefits;
    }

    public String getPoojaGodDetails() {
        return poojaGodDetails;
    }

    public void setPoojaGodDetails(String poojaGodDetails) {
        this.poojaGodDetails = poojaGodDetails;
    }
}
