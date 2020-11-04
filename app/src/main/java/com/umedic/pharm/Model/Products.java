package com.umedic.pharm.Model;

public class Products
{
    private String product_name, description, price, image, category, product_ID, date, time;

    public Products()
    {

    }

    public Products(String product_name, String description, String price, String image, String category, String product_ID, String date, String time) {
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.product_ID = product_ID;
        this.date = date;
        this.time = time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
