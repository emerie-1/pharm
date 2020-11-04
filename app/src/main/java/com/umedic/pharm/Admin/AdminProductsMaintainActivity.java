package com.umedic.pharm.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.umedic.pharm.R;
import com.umedic.pharm.Sellers.SellerProductCategoryActivity;

import java.util.HashMap;

public class AdminProductsMaintainActivity extends AppCompatActivity
{
    private ImageView changeImage;
    private EditText changeProductName, changeProductPrice, changeProductDesc;
    private Button applyChangesBtn, deleteBtn;

    private String productId;

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products_maintain);

        productId = getIntent().getStringExtra("product_ID");
        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(productId);

        changeImage = findViewById(R.id.edit_product_image);
        changeProductName = findViewById(R.id.edit_product_name);
        changeProductPrice = findViewById(R.id.edit_product_price);
        changeProductDesc = findViewById(R.id.edit_product_description);

        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        deleteBtn = findViewById(R.id.product_delete_btn);

        productSpecificationDisplay();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteProduct();
            }
        });
    }

    private void deleteProduct()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(AdminProductsMaintainActivity.this, "product have been deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminProductsMaintainActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applyChanges()
    {
        String pName = changeProductName.getText().toString();
        String pPrice = changeProductPrice.getText().toString();
        String pDesc = changeProductDesc.getText().toString();

        if (pName.equals(""))
        {
            Toast.makeText(this, "write product name", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals(""))
        {
            Toast.makeText(this, "enter your product price", Toast.LENGTH_SHORT).show();
        }
        else if (pDesc.equals(""))
        {
            Toast.makeText(this, "describe your product", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("product_ID", productId);
            productMap.put("description", pDesc);
            productMap.put("product_name", pName);
            productMap.put("price", pPrice);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminProductsMaintainActivity.this, "Changes successfully applied", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminProductsMaintainActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void productSpecificationDisplay()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String name = snapshot.child("product_name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();

                    changeProductName.setText(name);
                    changeProductPrice.setText(price);
                    changeProductDesc.setText(description);

                    Picasso.get().load(image).into(changeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}