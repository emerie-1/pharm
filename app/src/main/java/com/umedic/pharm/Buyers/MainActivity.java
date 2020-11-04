package com.umedic.pharm.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.umedic.pharm.CourierLoginActivity;
import com.umedic.pharm.R;
import com.umedic.pharm.Sellers.SellerHomeActivity;
import com.umedic.pharm.Sellers.SellerLoginActivity;
import com.umedic.pharm.Sellers.SellerRegistrationActivity;

public class MainActivity extends AppCompatActivity {
    private Button signUpButton, loginButton, sellerLoginBtn, courierLoginBtn;
    private ProgressDialog loadingBar;

    private TextView becomeSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpButton = findViewById(R.id.sign_up_btn);
        loginButton = findViewById(R.id.main_login_btn);
        sellerLoginBtn = findViewById(R.id.main_seller_login);
        courierLoginBtn = findViewById(R.id.main_courier_login_btn);

        becomeSeller = findViewById(R.id.become_a_seller);

        loadingBar = new ProgressDialog(this);

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        courierLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CourierLoginActivity.class);
                startActivity(intent);
            }
        });

        becomeSeller.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}