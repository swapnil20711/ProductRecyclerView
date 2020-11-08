package com.example.livecoding;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.livecoding.adapters.ProductsAdapter;
import com.example.livecoding.databinding.ActivityCatalogBinding;
import com.example.livecoding.dialogs.DialogPicker;
import com.example.livecoding.models.Product;

import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity{
    ActivityCatalogBinding b;
    private ArrayList<Product> products;
    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        b = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setUpProductList();
    }

    private void setUpProductList() {
        products=new ArrayList<>();
        //Create adapter object
        adapter = new ProductsAdapter(this, products);
        //Set the adapter and layout manager
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_catalog_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_item) {
            Toast.makeText(this, "Add Item clicked ", Toast.LENGTH_SHORT).show();
            DialogPicker dialogPicker=new DialogPicker();
            dialogPicker.showP(this, new Product(), new DialogPicker.onProductEditedListener() {
                @Override
                public void onProductEdited(Product product) {
                    products.add(product);
                    adapter.notifyItemInserted(products.size()-1);
                    Toast.makeText(CatalogActivity.this, ""+product.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled() {
                    Toast.makeText(CatalogActivity.this, "Cancelled!!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}