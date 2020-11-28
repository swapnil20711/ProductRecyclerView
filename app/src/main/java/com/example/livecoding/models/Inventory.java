package com.example.livecoding.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable {
    public ArrayList<Product>products;

    public Inventory(ArrayList<Product> products) {
        this.products = products;
    }

    public Inventory() {
    }
}
