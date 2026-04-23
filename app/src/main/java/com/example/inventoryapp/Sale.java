package com.example.inventoryapp;
public class Sale {
    public int id;
    public String productName;
    public int quantity;
    public double total;
    public String date;
    public Sale(int id, String productName, int quantity, double total, String date) {
        this.id = id; this.productName = productName; this.quantity = quantity; this.total = total; this.date = date;
    }
}
