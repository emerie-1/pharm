package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.umedic.pharm.Model.Products;
import com.umedic.pharm.R;
import com.umedic.pharm.ViewHolder.ProductView;

public class ProductSearchActivity extends AppCompatActivity
{
    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        searchBtn = findViewById(R.id.search_btn);
        inputText = findViewById(R.id.search_product);
        searchList = findViewById(R.id.search_list);

        searchList.setLayoutManager(new LinearLayoutManager(ProductSearchActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference searchRef = FirebaseDatabase.getInstance().getReference().child("products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(searchRef.orderByChild("product_name").startAt(searchInput), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductView> adapter = new FirebaseRecyclerAdapter<Products, ProductView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductView productView, int position, @NonNull final Products model)
            {
                productView.textProductName.setText(model.getProduct_name());
                productView.textProductDesc.setText(model.getDescription());
                productView.textProductPrice.setText("Price: Naira " + model.getPrice());

                Picasso.get().load(model.getImage())
                        .placeholder(R.drawable.pharmacy)
                        .into(productView.productImg);

                productView.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ProductSearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("product_ID", model.getProduct_ID());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent,false);
                ProductView productView = new ProductView(view);
                return productView;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}