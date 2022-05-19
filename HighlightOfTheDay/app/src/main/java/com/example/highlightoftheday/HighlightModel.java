package com.example.highlightoftheday;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.util.Date;

public class HighlightModel {
    private String date;
    private Drawable image;
    private String description;

    public HighlightModel(String date, Drawable image, String description) {
        this.date = date;
        this.image = image;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
