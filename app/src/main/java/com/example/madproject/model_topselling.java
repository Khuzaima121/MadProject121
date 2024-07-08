package com.example.madproject;

public class model_topselling {


    private String name;
    private String description;
    private String price;
    private boolean topSelling;
    private boolean deal;
    private String imageUrl;
    private boolean favourite;

    public model_topselling() {
    }

    public model_topselling(String imageUrl, String name, String description, String price, boolean topSelling, boolean deal, String imageUrl1, boolean favourite) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.topSelling = topSelling;
        this.deal = deal;

        this.imageUrl = imageUrl1;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTopSelling() {
        return topSelling;
    }

    public void setTopSelling(boolean topSelling) {
        this.topSelling = topSelling;
    }

    public boolean isDeal() {
        return deal;
    }

    public void setDeal(boolean deal) {
        this.deal = deal;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
