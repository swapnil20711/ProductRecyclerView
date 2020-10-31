package com.example.livecoding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.databinding.ProductListBinding;
import com.example.livecoding.models.Product;

import java.util.List;

//Adapter for list of products
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout for product item
        //Inflating layout by using LayoutInflater from product_list.xml
        ProductListBinding binding = ProductListBinding.inflate(
                LayoutInflater.from(context)
                , parent
                , false
        );

        //Create ViewHolder object and return
        return new ViewHolder(binding);
    }

    //Binds the data to view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at position
        Product product = productList.get(position);

        //Bind the data
        holder.b.name.setText(String.format("%s %d", product.name, product.price));

        //Quantity
        holder.b.qty.setText(product.qty + "");

        //DecrementButton & Quantity TV visibility
        holder.b.decrementBtn.setVisibility(product.qty > 0 ? View.VISIBLE : View.GONE);
        holder.b.qty.setVisibility(product.qty > 0 ? View.VISIBLE : View.GONE);


        //Configure + btn

        holder.b.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.qty++;
                notifyItemChanged(position);

            }
        });
        holder.b.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.qty--;
                notifyItemChanged(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    //Holds the view for each item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public ProductListBinding b;

        public ViewHolder(@NonNull ProductListBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
