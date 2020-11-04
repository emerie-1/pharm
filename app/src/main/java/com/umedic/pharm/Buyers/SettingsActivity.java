package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umedic.pharm.Model.Users;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity
{

    private TextView closeSettings, updateInfo;
    private EditText phoneNoSettings, emailSettings, nameSettings, addressSettings, passwordSettings;
    private Button securityQuestions;

    private Users userPhone;
    DatabaseReference userRef;

    private String myUri = "", checker = "";
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        closeSettings = findViewById(R.id.close_settings_btn);
        updateInfo = findViewById(R.id.update_info_settings_btn);
        phoneNoSettings = findViewById(R.id.phone_no_settings);
        emailSettings = findViewById(R.id.email_address_settings);
        nameSettings = findViewById(R.id.full_name_settings);
        addressSettings = findViewById(R.id.address_settings);
        passwordSettings = findViewById(R.id.password_settings);
        securityQuestions = findViewById(R.id.security_question_setting);


        userInfoSettings(closeSettings, updateInfo, phoneNoSettings, passwordSettings, emailSettings, nameSettings, addressSettings);

        closeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                {
                    userInfoStored();
                }

            }
        });

        securityQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PasswordResetActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });
    }

    private void userInfoStored()
    {
        if (TextUtils.isEmpty(nameSettings.getText().toString()))
        {
            Toast.makeText(this, "enter name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressSettings.getText().toString()))
        {
            Toast.makeText(this, "enter address...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneNoSettings.getText().toString()))
        {
            Toast.makeText(this, "enter phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(passwordSettings.getText().toString()))
        {
            Toast.makeText(this, "enter a password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
//            loadingBar.setTitle("updating profile");
//            loadingBar.setMessage("Wait a few seccond...");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();

            saveInfo();
        }

    }

    private void saveInfo()
    {
        final DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("Users");
        updateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", nameSettings.getText().toString());
                    userDataMap.put("address", addressSettings.getText().toString());
                    userDataMap.put("email", emailSettings.getText().toString());
                    userDataMap.put("phone", phoneNoSettings.getText().toString());

                    updateRef.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userDataMap);


                    Toast.makeText(SettingsActivity.this, "profile updated...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void userInfoSettings(final TextView closeSettings, final TextView updateInfo, final EditText phoneNoSettings, final EditText emailSettings, final EditText nameSettings, final EditText addressSettings, final EditText passwordSettings)
    {
        userRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String name = snapshot.child("name").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String password = snapshot.child("password").getValue().toString();
                    String email =snapshot.child("email").getValue().toString();


                    phoneNoSettings.setText(phone);
                    passwordSettings.setText(password);
                    emailSettings.setText(email);
                    nameSettings.setText(name);
                    addressSettings.setText(address);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}