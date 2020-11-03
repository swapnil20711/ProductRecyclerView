package com.example.livecoding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.livecoding.databinding.WeightPickerDialogBinding;

class WeightPicker {

    /**
     * There are 9 TODOs in this file, locate them using the window for it given at the bottom.
     * Also, complete them in order.
     * <p>
     * TODO 1 : Design layout weight_picker_dialog.xml for this WeightPicker Dialog
     * with 2 NumberPickers (for kg & g)
     */

    public void show(Context context, final OnWeightPickedListener listener) {
        WeightPickerDialogBinding b = WeightPickerDialogBinding.inflate(
                LayoutInflater.from(context)
        );

        new AlertDialog.Builder(context)
                .setTitle("Pick Weight")
                .setView(b.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO 3 : Replace 0s & assign kg & g values from respective NumberPickers
                        int kg = b.numberPickerKg.getValue();
                        int g = b.numberPickerg.getValue() * 50;
                        Toast.makeText(context, "" + g, Toast.LENGTH_SHORT).show();
                        //TODO 4 : Add GuardCode to prevent user from selecting 0kg 0g. If so, then return
                        if (kg == 0 && g == 0) {
                            return;
                        }
                        listener.onWeightPicked(kg, g);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onWeightPickerCancelled();
                    }
                })
                .show();

        setupNumberPickers(b.numberPickerKg, b.numberPickerg);

        //TODO 5 : Call new WeightPicker().show() in MainActivity and pass (this, new OnWeight...)

        //TODO 6 : Show toast of selected weight in onWeightPicked() method

        //TODO 7 : Find appropriate solution for : NumberPicker not formatting the first item

        //TODO 8 : Test your code :)

        //TODO 9 : Try to understand the flow as to how our Listener interface is working
    }

    private void setupNumberPickers(NumberPicker numberPickerKg, NumberPicker numberPickerg) {
        //TODO 2 : Define this method to setup kg & g NumberPickers as per the given ranges
        //kg Range - 0kg to 10kg
        //g Range - 0g to 950g
        numberPickerg.setMinValue(0);
        numberPickerg.setMaxValue(19);
        numberPickerKg.setMinValue(0);
        numberPickerKg.setMaxValue(10);
        numberPickerg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value * 50 + " " + "g";
            }
        });
        numberPickerKg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " " + "kg";
            }
        });

        View firstItem = numberPickerg.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
        View firstItemKg = numberPickerKg.getChildAt(0);
        if (firstItemKg != null) {
            firstItemKg.setVisibility(View.INVISIBLE);
        }
        View firstItemg = numberPickerg.getChildAt(0);
        if (firstItemg != null) {
            firstItemg.setVisibility(View.INVISIBLE);
        }
    }

    interface OnWeightPickedListener {
        void onWeightPicked(int kg, int g);

        void onWeightPickerCancelled();
    }

}
