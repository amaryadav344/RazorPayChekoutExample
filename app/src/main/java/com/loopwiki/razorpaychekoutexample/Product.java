package com.loopwiki.razorpaychekoutexample;

public class Product {
    private String name;
    private int price;
    private String ImageURL;
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

     String getImageURL() {
        return ImageURL;
    }

     void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
