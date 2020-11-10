package com.example.livecoding.models;

public class Variant {

    public String name;
    public int price;

    @Override
    public String toString() {
        return  name +"-Rs "+ price;
    }

    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
