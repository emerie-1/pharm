package com.umedic.pharm.Model;

public class Cart
{
    private String product_ID, product_name, price, quantity, discount;

    public Cart() {
    }

    public Cart(String product_ID, String product_name, String price, String quantity, String discount) {
        this.product_ID = product_ID;
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
