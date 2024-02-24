package com.app.astrotalk.model;

public class AllCategory {
    String astroType;

    String astroExp;

    String astroLang;

    String astroAbout;

    public AllCategory(Integer id, String name, int imageD, String astroType, String astroExp, String astroLang, String astroAbout) {
        this.id = id;
        this.name = name;
        this.imageD = imageD;
        this.astroAbout = astroAbout;
        this.astroLang = astroLang;
        this.astroExp = astroExp;
        this.astroType = astroType;
    }

    private Integer id;

    public int getImageD() {
        return imageD;
    }

    public void setImageD(int imageD) {
        this.imageD = imageD;
    }

    private String name;
    private int imageD;

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


}
