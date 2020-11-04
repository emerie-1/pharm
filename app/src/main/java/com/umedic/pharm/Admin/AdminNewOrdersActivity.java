package com.umedic.pharm.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umedic.pharm.Model.AdminOrders;
import com.umedic.pharm.R;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView ordersList;

    private DatabaseReference ordersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

        ordersList = findViewById(R.id.order_items_for_admin);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersView> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersView>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersView holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.adminOrdersUserName.setText("Name: " + model.getName());
                        holder.adminOrdersPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.adminTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                        holder.cityAddress.setText("Shipping Address: " + model.getAddress());
                        holder.dateTime.setText("date: " + model.getDate() + ",58 " +  model.getTime());

                        holder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String user_id = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserCartActivity.class);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[] =  new CharSequence[]
                                        {
                                                "Yes", "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you shipped this order products");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which == 0)
                                        {
                                            String userId = getRef(position).getKey();
                                            RemoveOrder(userId);
                                        }
                                        else
                                        {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersView(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String userId)
    {
        ordersRef.child(userId).removeValue();
    }

    public static class AdminOrdersView extends RecyclerView.ViewHolder
    {
        public TextView adminOrdersUserName, adminOrdersPhoneNumber, adminTotalPrice, cityAddress, dateTime;
        public Button showOrderBtn;

        public AdminOrdersView(@NonNull View itemView) {
            super(itemView);

            adminOrdersUserName = itemView.findViewById(R.id.user_name);
            adminOrdersPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            adminTotalPrice = itemView.findViewById(R.id.order_total_price);
            cityAddress = itemView.findViewById(R.id.order_city_address);
            dateTime = itemView.findViewById(R.id.order_date_time);
            showOrderBtn = itemView.findViewById(R.id.show_product);
        }

    }
}