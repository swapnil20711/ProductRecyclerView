package com.example.livecoding.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.databinding.ProductListBinding;
import com.example.livecoding.databinding.VariantsItemPreviewBinding;
import com.example.livecoding.databinding.WeightItemPreviewBinding;
import com.example.livecoding.models.Product;
import com.example.livecoding.models.Variant;

import java.util.List;

//Adapter for list of products
public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Needed for inflating layout
    private Context context;

    //List of data
    private List<Product> productList;

    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
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
        final Product product = productList.get(position);
        if (product.type == Product.WEIGHT_BASED) {
            WeightItemPreviewBinding previewBinding = ((WeightBasedProductVH) holder).binding;
            previewBinding.itemName.setText(product.name);
            previewBinding.itemMinqty.setText("MinQty " + product.minQty + " kg ");
            previewBinding.itemPrice.setText("Rs. " + product.price + "");
        } else {
            VariantsItemPreviewBinding binding = ((VariantsBasedVH) holder).binding;
            binding.name.setText(product.name);
            binding.variants.setText(product.variantsString());
            Log.e("main", product.variantsString());
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return productList.get(position).type;
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
}
