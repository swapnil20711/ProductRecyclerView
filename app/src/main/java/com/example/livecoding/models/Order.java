package com.example.livecoding.models;

import com.google.firebase.Timestamp;

public class Order {
    public String userName, userAddress;
    public int subTotal;
    public int status;
    public Timestamp timestamp;

    public Order(String userName, String userAddress,int subTotal, int status) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.subTotal = subTotal;
        this.status = status;
        this.timestamp = timestamp.now();
    }

    public static class OrderStatus {

        public static final int PLACED = 1 // Initially (U)
                , DELIVERED = 0, DECLINED = -1;     //(A)

    }

}
