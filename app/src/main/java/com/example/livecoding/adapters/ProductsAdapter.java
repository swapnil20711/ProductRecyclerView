package com.example.livecoding.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.CatalogActivity;
import com.example.livecoding.R;
import com.example.livecoding.databinding.VariantsItemPreviewBinding;
import com.example.livecoding.databinding.WeightItemPreviewBinding;
import com.example.livecoding.models.Product;

import java.util.ArrayList;
import java.util.List;

//Adapter for list of products
public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //Needed for inflating layout
    private Context context;

    //List of data
    public List<Product> allProducts, visibleProducts;
    public int LastItemSelectedPosition;


    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        allProducts = productList;
        this.visibleProducts = new ArrayList<>(productList);
    }

    //Inflate the view for item and create a ViewHolder object and return
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Product.WEIGHT_BASED) {
            WeightItemPreviewBinding binding = WeightItemPreviewBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );
            //Create ViewHolder object and return
            return new WeightBasedProductVH(binding);
        } else {
            VariantsItemPreviewBinding binding = VariantsItemPreviewBinding.inflate(LayoutInflater.from(context), parent, false);
            return new VariantsBasedVH(binding);
        }

    }

    //Binds the data to view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Product product = visibleProducts.get(position);
        if (product.type == Product.WEIGHT_BASED) {
            WeightItemPreviewBinding previewBinding = ((WeightBasedProductVH) holder).binding;
            previewBinding.itemName.setText(product.name);
            previewBinding.itemMinqty.setText("MinQty " + product.minQty + " kg ");
            previewBinding.itemPrice.setText("Rs. " + product.price + "");

            setUpContextMenu(previewBinding.getRoot());
        } else {
            VariantsItemPreviewBinding binding = ((VariantsBasedVH) holder).binding;
            binding.name.setText(product.name);
            binding.variants.setText(product.variantsString());
            setUpContextMenu(binding.getRoot());
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LastItemSelectedPosition = holder.getAdapterPosition();
                return false;
            }
        });
    }

    private void setUpContextMenu(ConstraintLayout root) {
        root.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (!(context instanceof CatalogActivity)) {
                    return;
                }

                ((CatalogActivity) context).getMenuInflater().inflate(R.menu.contextual_menu, menu);
            }
        });
    }


    @Override
    public int getItemCount() {
        return visibleProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return visibleProducts.get(position).type;
    }

    //Holds the view for each item
    public static class WeightBasedProductVH extends RecyclerView.ViewHolder {
        WeightItemPreviewBinding binding;

        public WeightBasedProductVH(@NonNull WeightItemPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class VariantsBasedVH extends RecyclerView.ViewHolder {

        VariantsItemPreviewBinding binding;

        public VariantsBasedVH(@NonNull VariantsItemPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void filterQuery(String query) {
        query = query.toLowerCase();
        visibleProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.name.toLowerCase().contains(query)) {
                visibleProducts.add(product);

            }
        }
        notifyDataSetChanged();
    }

}
