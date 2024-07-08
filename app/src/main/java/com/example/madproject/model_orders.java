package com.example.madproject;

public class model_orders {
String UserName,UserPhone,TotalBill,DishName,Address;

    public model_orders() {
    }

    public model_orders(String userName, String userPhone, String totalBill, String dishName, String address) {
        UserName = userName;
        UserPhone = userPhone;
        TotalBill = totalBill;
        DishName = dishName;
        Address = address;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getTotalBill() {
        return TotalBill;
    }

    public void setTotalBill(String totalBill) {
        TotalBill = totalBill;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
