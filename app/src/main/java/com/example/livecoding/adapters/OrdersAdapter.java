package com.example.livecoding.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livecoding.MyApp;
import com.example.livecoding.databinding.ItemViewOrderBinding;
import com.example.livecoding.fcmsender.FCMSender;
import com.example.livecoding.fcmsender.MessageFormatter;
import com.example.livecoding.models.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DocumentSnapshot> documentSnapshots;
    private MyApp app;

    public OrdersAdapter(Context context, List<DocumentSnapshot> documentSnapshots, MyApp app) {
        this.context = context;
        this.documentSnapshots = documentSnapshots;
        this.app = app;
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
        ItemViewOrderBinding binding = ((OrdersVH) holder).binding;
        binding.userName.setText(queryDocumentSnapshot.getId());
        binding.orderDetails.setVisibility(View.VISIBLE);

        StringBuilder builder = new StringBuilder();
        builder.append("UserName ").append(queryDocumentSnapshot.get("userName")).append("\n").append("Useraddress ").append(queryDocumentSnapshot.get("userAddress")).append("\n").append("Subtotal ").append(queryDocumentSnapshot.get("subTotal")).append("\n").append("Status ").append(queryDocumentSnapshot.get("status"));
        builder.append("\n").append(queryDocumentSnapshot.get("map"));
        binding.orderDetails.setText(builder);
        binding.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick","Onclivk");
                sendNotification();
                updateStatusToDelivered(queryDocumentSnapshot);
            }
        });
    }

    private void updateStatusToDelivered(QueryDocumentSnapshot queryDocumentSnapshot) {
        app.db.collection("orders").document(queryDocumentSnapshot.getId())
                .update("status", Order.OrderStatus.DELIVERED);
    }

    private void sendNotification() {
        Log.e("sendNotification","aa gaya");
        String message = MessageFormatter.getSampleMessage("users","Your order status:","Order delivered");
        new FCMSender().send(message, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
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
