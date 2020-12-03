package com.example.livecoding.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.databinding.ItemViewOrderBinding;
import com.example.livecoding.models.Order;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DocumentSnapshot> documentSnapshots;

    public OrdersAdapter(Context context, List<DocumentSnapshot> documentSnapshots) {
        this.context = context;
        this.documentSnapshots = documentSnapshots;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewOrderBinding binding = ItemViewOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OrdersVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueryDocumentSnapshot queryDocumentSnapshot = (QueryDocumentSnapshot) documentSnapshots.get(position);
        ItemViewOrderBinding binding=((OrdersVH) holder).binding;
        binding.userName.setText(queryDocumentSnapshot.getId());
        binding.orderDetails.setVisibility(View.VISIBLE);

        StringBuilder builder=new StringBuilder();
        builder.append(queryDocumentSnapshot.get("userName")).append("\n").append(queryDocumentSnapshot.get("userAddress")).append("\n").append(queryDocumentSnapshot.get("subTotal")).append("\n").append(queryDocumentSnapshot.get("status"));
        binding.orderDetails.setText(builder);



    }

    @Override
    public int getItemCount() {
        return documentSnapshots.size();
    }

    public static class OrdersVH extends RecyclerView.ViewHolder {

        ItemViewOrderBinding binding;

        public OrdersVH(@NonNull ItemViewOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
