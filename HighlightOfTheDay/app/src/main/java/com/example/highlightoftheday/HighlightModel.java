package com.example.highlightoftheday;

import java.util.Date;

public class HighlightModel {
    private Date date;
    private int image;
    private String description;

    public HighlightModel(Date date, int image, String description){
        this.date = date;
        this.image = image;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
