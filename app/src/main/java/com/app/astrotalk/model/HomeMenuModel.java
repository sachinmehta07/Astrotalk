package com.app.astrotalk.model;

public class HomeMenuModel {
    int optionImg;
    String optionName;
    String optionDesc;
    public HomeMenuModel(int optionImg, String optionName, String optionDesc) {
        this.optionImg = optionImg;
        this.optionName = optionName;
        this.optionDesc = optionDesc;
    }

    public int getOptionImg() {
        return optionImg;
    }

    public void setOptionImg(int optionImg) {
        this.optionImg = optionImg;
    }
    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

    public void setOptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }
}
