package com.example.myshoppinglist;

public class ShoppingListModel {
    String name;
    int image;
    int count;
    String description;


    public ShoppingListModel(String name, int image, int count, String description) {
        this.name = name;
        this.image = image;
        this.count = count;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getCount() {
        return count;
    }

    public String getDescription(){
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
