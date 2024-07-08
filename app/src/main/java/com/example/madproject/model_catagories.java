package com.example.madproject;

import java.util.ArrayList;

public class model_catagories {
    private String imageUrl;
    private String category;
    ArrayList<model_dishes> Dishes=new ArrayList<model_dishes>();
    public model_catagories() {

    }

    public model_catagories(String imageUrl, String category) {
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
