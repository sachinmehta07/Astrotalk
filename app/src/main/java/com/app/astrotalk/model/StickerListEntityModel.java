package com.app.astrotalk.model;

import java.util.ArrayList;

public class StickerListEntityModel {

    private int Id;

    private String packName;


    private String creatorName;


    private String packIcon;


    private ArrayList<String> stickerLists;


    public StickerListEntityModel(int id, String packName, String creatorName, String packIcon, ArrayList<String> stickerLists) {
        Id = id;
        this.packName = packName;
        this.creatorName = creatorName;
        this.packIcon = packIcon;
        this.stickerLists = stickerLists;
    }

    public StickerListEntityModel() {

    }


    public StickerListEntityModel(String packName, String creatorName, String packIcon, ArrayList<String> stickerLists) {
        this.packName = packName;
        this.creatorName = creatorName;
        this.packIcon = packIcon;
        this.stickerLists = stickerLists;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getPackIcon() {
        return packIcon;
    }

    public void setPackIcon(String packIcon) {
        this.packIcon = packIcon;
    }

    public ArrayList<String> getStickerLists() {
        return stickerLists;
    }

    public void setStickerLists(ArrayList<String> stickerLists) {
        this.stickerLists = stickerLists;
    }

}
