package com.example.livecoding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.livecoding.adapters.ProductsAdapter;
import com.example.livecoding.databinding.ActivityCatalogBinding;
import com.example.livecoding.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    ActivityCatalogBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        b = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setUpProductList();
    }

    private void setUpProductList() {
        //Create dataset
        List<Product> products = new ArrayList<>(
                Arrays.asList(
                        new Product("Apple", 20),
                        new Product("Banana", 30),
                        new Product("Apricots", 40),
                        new Product("Avocado", 50),
                        new Product("Blackberries", 60),
                        new Product("Apple", 20),
                        new Product("Banana", 30),
                        new Product("Apricots", 40),
                        new Product("Avocado", 50),
                        new Product("Blackberries", 60),
                        new Product("Apple", 20),
                        new Product("Banana", 30),
                        new Product("Apricots", 40),
                        new Product("Avocado", 50),
                        new Product("Blackberries", 60)
                )
        );

        //Create adapter object
        ProductsAdapter adapter = new ProductsAdapter(this, products);

        //Set the adapter and layout manager
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}