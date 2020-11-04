package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity
{
    private EditText shippingNameEdit, shippingPhoneEdit, shippingAddressEdit, shippingCityEdit;
    private Button confirmOrderBtn;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total price: " + totalAmount, Toast.LENGTH_LONG).show();

        shippingNameEdit = findViewById(R.id.shipping_name);
        shippingPhoneEdit = findViewById(R.id.shipping_phone_no);
        shippingAddressEdit = findViewById(R.id.shipping_address);
        shippingCityEdit = findViewById(R.id.shipping_city);

        confirmOrderBtn = findViewById(R.id.confirm_order_button);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Check();
            }
        });

    }

    private void Check()
    {
        if (TextUtils.isEmpty(shippingNameEdit.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shippingPhoneEdit.getText().toString()))
        {
            Toast.makeText(this, "Please provide a functional phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shippingAddressEdit.getText().toString()))
        {
            Toast.makeText(this, "Recipient address is needed", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shippingCityEdit.getText().toString()))
        {
            Toast.makeText(this, "Enter a city...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmOrder();
        }
    }

    private void confirmOrder()
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar retrieveDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(retrieveDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime  = currentTime.format(retrieveDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("orders").child(Prevalent.currentOnlineUser.getPhone());

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", shippingNameEdit.getText().toString());
        orderMap.put("phone", shippingPhoneEdit.getText().toString());
        orderMap.put("address", shippingAddressEdit.getText().toString());
        orderMap.put("city", shippingCityEdit.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("orderState", "not shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("User cart").child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmOrderActivity.this, "Cart item removed", Toast.LENGTH_LONG).show();

                                        Intent intent =  new Intent(ConfirmOrderActivity. this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}