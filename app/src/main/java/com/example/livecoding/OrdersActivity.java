package com.example.livecoding;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.livecoding.adapters.OrdersAdapter;
import com.example.livecoding.databinding.ActivityOrdersBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private ActivityOrdersBinding b;
    private MyApp app;
    private ArrayList<DocumentSnapshot> docs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        app=(MyApp)getApplicationContext();
        getOrderDetails();
    }

    private void getOrderDetails() {
        app.db.collection("orders")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                for (QueryDocumentSnapshot query:snapshot){
                    docs.add(query);
                    Log.e("main",docs.toString());
                }
                setAdapter();
            }
        });
    }


    private void setAdapter() {
        OrdersAdapter adapter=new OrdersAdapter(this, docs,app);
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}