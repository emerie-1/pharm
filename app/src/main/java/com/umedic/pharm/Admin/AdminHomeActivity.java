package com.umedic.pharm.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.umedic.pharm.Buyers.HomeActivity;
import com.umedic.pharm.Buyers.MainActivity;
import com.umedic.pharm.R;

public class AdminHomeActivity extends AppCompatActivity
{
    private Button adminLogoutBtn, checkOrdersBtn, productMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminLogoutBtn = findViewById(R.id.admin_logout_btn);
        checkOrdersBtn = findViewById(R.id.orders_check_btn);
        productMaintenance = findViewById(R.id.product_maintenance);

        productMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admins", "Admins");
                startActivity(intent);
            }
        });

        adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}