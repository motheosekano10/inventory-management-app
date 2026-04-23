package com.example.inventoryapp;
public class Product {
    public int id;
    public String name;
    public double price;
    public int stock;
    public String category;
    public Product(int id, String name, double price, int stock, String category) {
        this.id = id; this.name = name; this.price = price; this.stock = stock; this.category = category;
    }
}
