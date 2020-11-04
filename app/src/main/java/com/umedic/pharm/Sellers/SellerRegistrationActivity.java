package com.umedic.pharm.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umedic.pharm.Buyers.MainActivity;
import com.umedic.pharm.R;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity
{
    private Button sellerRegBtn;
    private EditText pharmName, pharmPhone, pharmEmail, pharmPassword, pharmAddress;
    private TextView startSellerLogin;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        sellerRegBtn = findViewById(R.id.seller_registration_btn);
        startSellerLogin = findViewById(R.id.seller_already_exist_link);

        pharmName = findViewById(R.id.seller_reg_name);
        pharmPhone = findViewById(R.id.seller_reg_phone);
        pharmEmail = findViewById(R.id.seller_reg_email);
        pharmPassword = findViewById(R.id.seller_reg_password);
        pharmAddress = findViewById(R.id.seller_business_address);

        startSellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sellerReg();
            }
        });
    }

    private void sellerReg()
    {
        final String sName = pharmName.getText().toString();
        final String sPhone = pharmPhone.getText().toString();
        final String sEmail = pharmEmail.getText().toString();
        String sPassword = pharmPassword.getText().toString();
        final String sAddress = pharmAddress.getText().toString();

        if (!sName.equals("") && !sPhone.equals("") && !sEmail.equals("") && !sPassword.equals("") && !sAddress.equals(""))
        {
            loadingBar.setTitle("Creating pharmacy account");
            loadingBar.setMessage("Wait a few seccond...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                       final DatabaseReference rootRef;
                       rootRef = FirebaseDatabase.getInstance().getReference();

                       String sid = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("sid", sid);
                        sellerMap.put("phone", sPhone);
                        sellerMap.put("email", sEmail);
                        sellerMap.put("name", sName);
                        sellerMap.put("address", sAddress);
                        //sellerMap.put("password", sPassword);


                        rootRef.child("sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                loadingBar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, "Your pharmacy account has been successfully registered ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "A row is empty. Please fill out the form", Toast.LENGTH_SHORT).show();
        }
    }

}