package com.umedic.pharm;

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

public class CourierLoginActivity extends AppCompatActivity
{
    private EditText courierEmail, courierPassword;
    private Button courierLoginBtn;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);


        courierEmail = findViewById(R.id.courier_login_email);
        courierPassword = findViewById(R.id.courier_login_password);
        courierLoginBtn = findViewById(R.id.courier_login_btn);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        courierLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                courierLogin();
            }
        });
    }

    private void courierLogin()
    {
        final String email = courierEmail.getText().toString();
        final String password = courierPassword.getText().toString();

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
                        Intent intent = new Intent(CourierLoginActivity.this, CourierHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(CourierLoginActivity.this, "Login credentials are incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}