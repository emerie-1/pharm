package com.umedic.pharm.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.umedic.pharm.Admin.AdminProductsMaintainActivity;
import com.umedic.pharm.MapsActivity;
import com.umedic.pharm.Model.Products;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;
import com.umedic.pharm.ViewHolder.ProductView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference ProductsRef;

    private RecyclerView recyclerView;
    private NavigationView navigationView;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private String type = "";

    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("Admins").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("products");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!type.equals("Admins"))
                {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // navigation
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //profile name view
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.profile_user_name);

        if (!type.equals("Admins"))
        {
            usernameTextView.setText(Prevalent.currentOnlineUser.getName());
        }

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef.orderByChild("product_state").equalTo("approved"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductView> adapter =
                new FirebaseRecyclerAdapter<Products, ProductView>(options)
                {

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
                                if (type.equals("Admins"))
                                {
                                    Intent intent = new Intent(HomeActivity.this, AdminProductsMaintainActivity.class);
                                    intent.putExtra("product_ID", model.getProduct_ID());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("product_ID", model.getProduct_ID());
                                    startActivity(intent);
                                }

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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.nav_view)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_categories)
        {

        }
        else if (id == R.id.nav_search)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HomeActivity.this, ProductSearchActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_map)
        {
            if (!type.equals("Admins"))
            {
                Intent mapIntent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }

        }
        else if (id == R.id.nav_settings)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_logout)
        {
            if (!type.equals("Admins"))
            {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }

}