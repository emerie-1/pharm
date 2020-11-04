package com.umedic.pharm.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.umedic.pharm.R;

public class SellerLoginActivity extends AppCompatActivity
{
    private Button sellerLoginBtn;
    private EditText pharmEmail, pharmPassword;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        pharmEmail = findViewById(R.id.seller_login_email);
        pharmPassword = findViewById(R.id.seller_login_password);

        sellerLoginBtn = findViewById(R.id.seller_login_btn);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sellerLogin();
            }
        });
    }

    private void sellerLogin()
    {
        final String email = pharmEmail.getText().toString();
        final String password = pharmPassword.getText().toString();

        if (!email.equals("") && !password.equals(""))
                    {
                        loadingBar.setTitle("Logging in");
                        loadingBar.setMessage("Wait a few seccond...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(SellerLoginActivity.this, "Login credentials are incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}