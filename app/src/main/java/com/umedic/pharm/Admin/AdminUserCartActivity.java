package com.umedic.pharm.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umedic.pharm.Model.Cart;
import com.umedic.pharm.R;
import com.umedic.pharm.ViewHolder.CartView;

public class AdminUserCartActivity extends AppCompatActivity
{

    private RecyclerView userProductsListsForAdmin;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference userCartRef;

    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_user_admin);

        userID = getIntent().getStringExtra("user_id");

        //layout manager
         userProductsListsForAdmin = findViewById(R.id.product_lists);
         userProductsListsForAdmin.setHasFixedSize(true);
         layoutManager = new LinearLayoutManager(this);
         userProductsListsForAdmin.setLayoutManager(layoutManager);

        userCartRef = FirebaseDatabase.getInstance().getReference().child("User cart")
                .child("Admin View").child(userID).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(userCartRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartView> adapter = new FirebaseRecyclerAdapter<Cart, CartView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartView holder, int position, @NonNull Cart model)
            {
                holder.textItemName.setText(model.getProduct_name());
                holder.textItemPrice.setText("Price: NGN " + model.getPrice());
                holder.textItemQuantity.setText("Quantity: " + model.getQuantity());
            }

            @NonNull
            @Override
            public CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartView holder = new CartView(view);
                return holder;
            }
        };
        userProductsListsForAdmin.setAdapter(adapter);
        adapter.startListening();

    }
}