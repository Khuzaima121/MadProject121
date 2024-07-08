package com.example.madproject;

public class model_cart {
    String DishName,Price,Quantity;

    public model_cart() {
    }

    public model_cart(String dishName, String price, String quantity) {
        DishName = dishName;
        Price = price;
        Quantity = quantity;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
