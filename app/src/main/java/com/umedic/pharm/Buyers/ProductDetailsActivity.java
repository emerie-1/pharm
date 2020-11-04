package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.umedic.pharm.Model.Products;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{

    private Button cartAddBtn;
    private ImageView productImgBtn;
    private ElegantNumberButton numberButton;
    TextView productNamedet, productDescdet, productPricedet;

    private String productId = "", state = "normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        cartAddBtn = findViewById(R.id.cart_add_btn);
        productImgBtn = findViewById(R.id.product_img_details);
        numberButton = findViewById(R.id.cart_quantity);
        productNamedet = findViewById(R.id.product_name_detail);
        productDescdet = findViewById(R.id.product_desc_detail);
        productPricedet = findViewById(R.id.product_price_detail);

        productId = getIntent().getStringExtra("product_ID");


        getProductDetails(productId);

        cartAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (state.equals("order placed")  || state.equals("order shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "if your order is shipped then you can purchase another batch", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addedToCart();
                }
            }

        });
        
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }

    private void getProductDetails(final String productId)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productNamedet.setText(products.getProduct_name());
                    productDescdet.setText(products.getDescription());
                    productPricedet.setText(products.getPrice());

                    Picasso.get().load(products.getImage()).placeholder(R.drawable.pharmacy).into(productImgBtn);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addedToCart()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar retrieveDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(retrieveDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime  = currentTime.format(retrieveDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("User cart");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("product_ID", productId);
        cartMap.put("price", productPricedet.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("product_name", productNamedet.getText().toString());
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                       if (task.isSuccessful())
                       {
                           cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("products").child(productId)
                                   .updateChildren(cartMap)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task)
                                       {
                                           if (task.isSuccessful())
                                           {
                                               cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("products").child(productId)
                                                       .updateChildren(cartMap)
                                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task)
                                                           {
                                                               if (task.isSuccessful())
                                                               {
                                                                   Toast.makeText(ProductDetailsActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                                                                   Intent intent =  new Intent(ProductDetailsActivity. this, HomeActivity.class);
                                                                   startActivity(intent);
                                                               }
                                                           }
                                                       });


                                           }
                                       }
                                   });
                       }

                    }
                });
    }


    private  void checkOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String orderState = snapshot.child("orderState").getValue().toString();

                    if (orderState.equals("shipped"))
                    {
                        state = "order shipped";
                    }
                    else if (orderState.equals("not shipped"))
                    {
                        state = "order placed";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}