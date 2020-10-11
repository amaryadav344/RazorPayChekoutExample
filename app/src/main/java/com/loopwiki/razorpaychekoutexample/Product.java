package com.loopwiki.razorpaychekoutexample;

public class Product {
    private String name;
    private int price;
    private String ImageURL;
    private boolean isNew;
    private boolean isAddedToCart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
