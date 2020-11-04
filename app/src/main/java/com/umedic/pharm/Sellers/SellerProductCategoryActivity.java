package com.umedic.pharm.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umedic.pharm.Admin.AdminNewOrdersActivity;
import com.umedic.pharm.Buyers.HomeActivity;
import com.umedic.pharm.R;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private TextView infantMedicine, drugs, pills, humanBody;
    private TextView prescription, firstAid, hygiene, files;
    private ImageView services, walkIns, disables, compounds, ems;

    private Button adminLogoutBtn, checkOrdersBtn, productMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

        infantMedicine = findViewById(R.id.infant_medicine);
        drugs = findViewById(R.id.drugs);
        pills = findViewById(R.id.pills);
        humanBody = findViewById(R.id.human_body);

        prescription = findViewById(R.id.prescription);
        firstAid = findViewById(R.id.first_aid);
        hygiene = findViewById(R.id.hygiene);
        files = findViewById(R.id.files);

        services = findViewById(R.id.services);
        walkIns = findViewById(R.id.walkIns);
        disables = findViewById(R.id.for_disables);
        compounds = findViewById(R.id.compounds);
        ems = findViewById(R.id.ems);

        checkOrdersBtn = findViewById(R.id.orders_check_btn);
        productMaintenance = findViewById(R.id.product_maintenance);


        infantMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "infant Medicine");
                startActivity((intent));;
            }
        });
        drugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "drugs");
                startActivity((intent));

            }
        });
        pills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "pills");
                startActivity((intent));

            }
        });
        humanBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "Human Body");
                startActivity((intent));

            }
        });

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "prescription");
                startActivity((intent));
            }
        });

        firstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "firstAid");
                startActivity((intent));
            }
        });
        hygiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "hygiene");
                startActivity((intent));
            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "files");
                startActivity((intent));
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "services");
                startActivity((intent));
            }
        });
        walkIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "walk Ins");
                startActivity((intent));
            }
        });
        disables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "disables");
                startActivity((intent));
            }
        });
        compounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "compounds");
                startActivity((intent));
            }
        });
        ems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddProductActivity.class);
                intent.putExtra("category", "ems");
                startActivity((intent));
            }
        });

        productMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admins", "Admins");
                startActivity(intent);
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

    }
}