package com.umedic.pharm.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.umedic.pharm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddProductActivity extends AppCompatActivity {
    
    private String categoryName, name, desc, price, saveDate, saveTime, productRandomKey, downloadImgUrl;

    private ImageView chooseProductImage;
    private EditText productName, productDesc, productPrice;
    private Button submitProduct;
    private static final int galleryPick = 1;

    private StorageReference productImgRef;
    private DatabaseReference productRef, sellerRef;

    private String sellerName, sellerAddress, sellerPhone, sellerEmail, sellerID;

    private Uri imageUri;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);
        
        categoryName = getIntent().getExtras().get("category").toString();
        productImgRef = FirebaseStorage.getInstance().getReference().child("product images");

        productRef = FirebaseDatabase.getInstance().getReference().child("products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("sellers");

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        chooseProductImage = findViewById(R.id.choose_product_image);
        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        submitProduct =findViewById(R.id.product_submit_btn);

        loadingBar = new ProgressDialog(this);

        chooseProductImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        submitProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                validateProductDataAndAdd();
            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    sellerName = snapshot.child("name").getValue().toString();
                    sellerAddress = snapshot.child("address").getValue().toString();
                    sellerPhone = snapshot.child("phone").getValue().toString();
                    sellerEmail = snapshot.child("email").getValue().toString();
                    sellerID = snapshot.child("sid").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            chooseProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductDataAndAdd()
    {
        name = productName.getText().toString();
        desc = productDesc.getText().toString();
        price = productPrice.getText().toString();

        if (imageUri == null)
        {
            Toast.makeText(this, "Kindly select an Image...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(desc))
        {
            Toast.makeText(this, "Kindly write a product description...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Kindly give your customers price on the goods...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "kindly enter a name of the goods for sale...", Toast.LENGTH_LONG).show();
        }
        else
        {
            storeProductInfo();
        }
    }

    private void storeProductInfo()
    {
        loadingBar.setTitle("Adding product");
        loadingBar.setMessage("Wait a few second...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calendar.getTime());

        productRandomKey = saveDate + saveTime;

        final StorageReference filePath = productImgRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(SellerAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(SellerAddProductActivity.this, "Upload successful...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImgUrl = task.getResult().toString();

                            loadingBar.dismiss();
                            Toast.makeText(SellerAddProductActivity.this, "Image URL successful retrieved ...", Toast.LENGTH_SHORT).show();
                            saveProductInfoInDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoInDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("product_ID", productRandomKey);
        productMap.put("date", saveDate);
        productMap.put("time", saveTime);
        productMap.put("description", desc);
        productMap.put("image", downloadImgUrl);
        productMap.put("category", categoryName);
        productMap.put("product_name", name);
        productMap.put("price", price);

        productMap.put("sellerName", sellerName);
        productMap.put("sellerAddress", sellerAddress);
        productMap.put("sellerPhone", sellerPhone);
        productMap.put("sellerEmail", sellerEmail);
        productMap.put("sid", sellerID);
        productMap.put("product_state", "approved");



        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(SellerAddProductActivity.this, SellerHomeActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(SellerAddProductActivity.this, "product added", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(SellerAddProductActivity.this, "error " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}