package com.example.livecoding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       WeightPicker weightPicker= new WeightPicker();
       weightPicker.show(this, new WeightPicker.OnWeightPickedListener() {
           @Override
           public void onWeightPicked(int kg, int g) {

               Toast.makeText(MainActivity.this, "Item quantity in kg is "+kg, Toast.LENGTH_SHORT).show();
               Toast.makeText(MainActivity.this, "Item quantity in g is "+g, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onWeightPickerCancelled() {

               Toast.makeText(MainActivity.this, "Order cancelled", Toast.LENGTH_SHORT).show();
           }
       });
    }
}