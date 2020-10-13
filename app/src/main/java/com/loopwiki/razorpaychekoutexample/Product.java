package com.loopwiki.razorpaychekoutexample;

public class Product {
    private String name;
    private int price;
    private int ImageResourceId;
    private boolean isNew;
    private boolean isAddedToCart;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getPrice() {
        return price;
    }

     void setPrice(int price) {
        this.price = price;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

     boolean isAddedToCart() {
        return isAddedToCart;
    }

     void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    int getImageResourceId() {
        return ImageResourceId;
    }

     void setImageResourceId(int imageResourceId) {
        ImageResourceId = imageResourceId;
    }
}
