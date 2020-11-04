package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umedic.pharm.Model.Cart;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;
import com.umedic.pharm.ViewHolder.CartView;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button proceedBtn, checkBtn;
    private TextView textTotalPrice, orderSuccessMsg, totalPrice2;


    private int overallTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textTotalPrice = findViewById(R.id.total_price);
        proceedBtn = findViewById(R.id.proceed_btn);
        checkBtn = findViewById(R.id.check);
        orderSuccessMsg = findViewById(R.id.order_success_txt);
        totalPrice2 = findViewById(R.id.total_price_2);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                textTotalPrice.setText("Total Price: NGN " + overallTotalPrice);
                totalPrice2.setText("Total Price: NGN " + overallTotalPrice);
                totalPrice2.setVisibility(View.VISIBLE);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                textTotalPrice.setText("Total Price: NGN " + overallTotalPrice);

                Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("User cart");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartView> adapter =  new FirebaseRecyclerAdapter<Cart, CartView>(options){
            @Override
            protected void onBindViewHolder(@NonNull CartView holder, int position, @NonNull final Cart model)
            {
                holder.textItemName.setText(model.getProduct_name());
                holder.textItemPrice.setText("Price: NGN " + model.getPrice());
                holder.textItemQuantity.setText("Quantity: " + model.getQuantity());


                try {
                    int particularProductPrice =
                            ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                    overallTotalPrice +=  particularProductPrice;
                } catch (NumberFormatException e) {
                    // Log error, change value of temperature, or do nothing
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit", "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options: ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("product_ID", model.getProduct_ID());
                                    startActivity(intent);
                                }
                                if (which == 1)
                                {
                                    cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("products")
                                            .child(model.getProduct_ID()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item has been remove from your cart",Toast.LENGTH_SHORT).show();

                                                        //Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        //startActivity(intent);

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String userName = snapshot.child("name").getValue().toString();

                    if (orderState.equals("shipped"))
                    {
                        textTotalPrice.setText("Dear" + userName +", Your order is on route \n you paid " + overallTotalPrice);
                        recyclerView.setVisibility(View.GONE);

                        orderSuccessMsg.setVisibility(View.VISIBLE);
                        orderSuccessMsg.setText("Order has been placed successfully. You will recieve as call soon. Thank you for ordering");

                        proceedBtn.setVisibility(View.GONE);
                    }
                    else if (orderState.equals("not shipped"))
                    {
                        textTotalPrice.setText("has not been shipped");
                        recyclerView.setVisibility(View.GONE);
                        orderSuccessMsg.setVisibility(View.VISIBLE);
                        proceedBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase later", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}