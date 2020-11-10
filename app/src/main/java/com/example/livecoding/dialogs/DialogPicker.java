package com.example.livecoding.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.livecoding.R;
import com.example.livecoding.databinding.DialogProductListBinding;
import com.example.livecoding.models.Product;

public class DialogPicker {
    private Product product;
    private DialogProductListBinding b;


    public void showP(Context context,
                      final Product product, final onProductEditedListener listener) {
        this.product = product;

        b = DialogProductListBinding.inflate(LayoutInflater.from(context));
        new AlertDialog.Builder(context)
                .setTitle("Edit Product")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancelled ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (areProductsDetailsValid()) {
                            product.name = b.productName.getText().toString().trim();
                            listener.onProductEdited(DialogPicker.this.product);
                        } else {
                            Toast.makeText(context, "Invalid details!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setView(b.getRoot())
                .show();

        setUpRadio();
        prefilldetails();
    }

    //Set Up radio buttons
    private void setUpRadio() {
        b.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.weight_based_radiobtn) {
                    b.lvVariant.setVisibility(View.GONE);
                    b.lhEdittext.setVisibility(View.VISIBLE);
                } else {
                    b.lhEdittext.setVisibility(View.GONE);
                    b.lvVariant.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void prefilldetails() {
        b.productName.setText(product.name);

        b.radioGroup.check(product.type == product.WEIGHT_BASED ? R.id.weight_based_radiobtn : R.id.variants_based_radiobtn);

        if (product.type == Product.WEIGHT_BASED) {
            b.productName.setText(product.name);
            b.productMinQty.setText(product.minQtyToString());
            b.pricePerKg.setText(product.price + "");
        } else {
            b.variantProduct.setText(product.variantsString() + "");
        }

    }

    //Check whether product details are valid or not
    private boolean areProductsDetailsValid() {
        String pName = b.productName.getText().toString().trim();
        if (pName.isEmpty()) {
            return false;
        }
        switch (b.radioGroup.getCheckedRadioButtonId()) {
            case R.id.weight_based_radiobtn:
                String price = b.pricePerKg.getText().toString().trim();
                String minQty = b.productMinQty.getText().toString().trim();
                if (price.isEmpty() || minQty.isEmpty() || pName.isEmpty() || !minQty.matches("\\d+(kg|g)")) {
                    return false;
                }
                product = new Product(pName, Integer.parseInt(price), extractQty(minQty));
                return true;
            case R.id.variants_based_radiobtn:
                Log.e("main", "fucked up");
                String variants = b.variantProduct.getText().toString().trim();

                product = new Product(pName);

                return areVariantsValid(variants);
        }
        return false;
    }

    private boolean areVariantsValid(String variants) {
        if (variants.length() == 0) {
            return false;
        }
        String[] vs = variants.split("\n");

        for (String s : vs) {
            if (!(s.matches("^\\w+(\\s|\\w)+,\\d+$"))) {
                Log.e("main", "fucked");
                return false;
            }
        }
        product.fromVariantStrings(vs);
        return true;
    }


    //To extract qty from string
    private float extractQty(String minQty) {
        if (minQty.contains("kg")) {
            return Integer.parseInt(minQty.replace("kg", ""));
        } else {
            return Integer.parseInt(minQty.replace("g", "")) / 1000f;
        }
    }

    //Listener to call when product is edited
    public interface onProductEditedListener {
        void onProductEdited(Product product);

        void onCancelled();
    }


}
