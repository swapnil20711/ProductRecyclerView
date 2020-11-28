package com.example.livecoding;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.adapters.ProductsAdapter;
import com.example.livecoding.databinding.ActivityCatalogBinding;
import com.example.livecoding.dialogs.DialogPicker;
import com.example.livecoding.models.Inventory;
import com.example.livecoding.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    ActivityCatalogBinding b;
    private ArrayList<Product> products;
    ProductsAdapter adapter;
    private SearchView searchView;
    private ItemTouchHelper itemTouchHelper;
    public boolean isDragModeOn;
    private MyApp app;
    private ArrayList<Product> restoreRemoveProducts=new ArrayList<>();

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Do you want to save?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                finish();
            }
        }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        b = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        app = (MyApp) getApplicationContext();
        loadPreviousData();
    }

    private void saveData() {
        if (app.isOffline()) {
            Toast.makeText(this, "Cannot fetch data You are offline!!", Toast.LENGTH_SHORT).show();
        }
        app.showLoadingDialog(CatalogActivity.this);
        Inventory inventory = new Inventory(products);
        app.db.collection("inventory")
                .document("products")
                .set(inventory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        app.showToast(CatalogActivity.this, "Data saved at db!!");
                        app.hideLoadingDialog();
                        saveDataLocally();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        app.showToast(CatalogActivity.this, "Failed to save data");
                        app.hideLoadingDialog();
                    }
                });
    }

    private void saveDataLocally() {
        SharedPreferences preferences = getSharedPreferences("products_data", MODE_PRIVATE);
        preferences.edit().putString("data", new Gson().toJson(products)).apply();
    }

    private void loadPreviousData() {
        SharedPreferences preferences = getSharedPreferences("products_data", MODE_PRIVATE);
        String jsonData = preferences.getString("data", null);
//        Log.e("main",""+jsonData.isEmpty());
//        Toast.makeText(this, ""+jsonData.isEmpty(), Toast.LENGTH_SHORT).show();
        if (jsonData != null) {
            products = new Gson().fromJson(jsonData, new TypeToken<List<Product>>() {
            }.getType());
            setUpProductList();
        } else {
            fetchFromCloud();
        }
    }

    private void fetchFromCloud() {
        if (app.isOffline()) {
            Toast.makeText(this, "Cannot fetch data You are offline!!", Toast.LENGTH_SHORT).show();
            return;
        }
        app.showLoadingDialog(this);
        app.db.collection("inventory")
                .document("products")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists() && snapshot != null) {
                    Inventory inventory = snapshot.toObject(Inventory.class);
                    products = inventory.products;
                } else {
                    products = new ArrayList<>();
                }
                saveDataLocally();
                setUpProductList();
                app.hideLoadingDialog();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        app.showToast(CatalogActivity.this, "Failed to save data");
                        app.hideLoadingDialog();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDataLocally();
    }

    private void setUpDragDrop() {
        Log.e("setUpDrag", "aa gaya");
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(adapter.visibleProducts, fromPosition, toPosition);
                b.recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_product:
                editProduct();
                return true;
            case R.id.delete_product:
                deleteProduct();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteProduct() {
        new AlertDialog.Builder(this)
                .setTitle("Do you really want to delete this?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Product lastRemove = adapter.visibleProducts.get(adapter.LastItemSelectedPosition);
                        adapter.visibleProducts.remove(lastRemove);
                        adapter.allProducts.remove(lastRemove);
                        adapter.notifyItemRemoved(adapter.LastItemSelectedPosition);

                        Toast.makeText(CatalogActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void editProduct() {
        Product LastProduct = adapter.visibleProducts.get(adapter.LastItemSelectedPosition);
        new DialogPicker().showP(this, LastProduct, new DialogPicker.onProductEditedListener() {
            @Override
            public void onProductEdited(Product product) {
                products.set(adapter.LastItemSelectedPosition, product);
                adapter.notifyItemChanged(adapter.LastItemSelectedPosition);
            }

            @Override
            public void onCancelled() {
                Toast.makeText(CatalogActivity.this, "Cancelled!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setUpProductList() {
        //Create adapter object
        adapter = new ProductsAdapter(this, products);
        //Set the adapter and layout manager
        b.recyclerView.setAdapter(adapter);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        setUpDragDrop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_catalog_options, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //Meta data
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Mylog", "onQueryTextSubmit" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.e("Text changed", "onQueryTextChange: " + query);
                adapter.filterQuery(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                addItem();
                return true;
            case R.id.drag_mode:
                toggleDragMode(item);
                return true;
            case R.id.sort:
                sortByName();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleDragMode(MenuItem item) {
        changeIconBackground(item);
        if (isDragModeOn) {
            itemTouchHelper.attachToRecyclerView(null);
        } else {
            itemTouchHelper.attachToRecyclerView(b.recyclerView);
        }
        isDragModeOn = !isDragModeOn;
    }

    private void changeIconBackground(MenuItem item) {
        Drawable icon = item.getIcon();
        if (isDragModeOn) {
            icon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        } else {
            icon.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        }
        item.setIcon(icon);

    }

    private void sortByName() {
        Collections.sort(adapter.visibleProducts, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.name.compareToIgnoreCase(o2.name);
            }
        });
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "List sorted", Toast.LENGTH_SHORT).show();
    }


    private boolean addItem() {
        new DialogPicker().showP(this, new Product(), new DialogPicker.onProductEditedListener() {
            @Override
            public void onProductEdited(Product product) {

                adapter.allProducts.add(product);
                if (isNameInQuery(product.name)) {
                    adapter.visibleProducts.add(product);
                }
                adapter.notifyItemInserted(products.size() - 1);
                Toast.makeText(CatalogActivity.this, "" + product.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {
                Toast.makeText(CatalogActivity.this, "Cancelled!!", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean isNameInQuery(String name) {
        String query = searchView.getQuery().toString().toLowerCase();
        return name.toLowerCase().contains(query);
    }
}