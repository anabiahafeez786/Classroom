package com.example.classroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 SESSION CHECK
        SharedPreferences prefs =
                getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_product);

        // ================== VIEWS ==================
        drawerLayout = findViewById(R.id.drawerLayout);

        ImageView ivMenu = findViewById(R.id.ivMenu);

        TextView tvClasses     = findViewById(R.id.tvMenuClasses);
        TextView tvCalendar    = findViewById(R.id.tvMenuCalendar);
        TextView tvFilters     = findViewById(R.id.tvMenuFilters);
        TextView tvStudentInfo = findViewById(R.id.tvMenuStudentInfo);
        TextView tvCreateClass = findViewById(R.id.tvMenuCreateClass);
        TextView tvJoinClass   = findViewById(R.id.tvMenuJoinClass);

        Button btnLogout = findViewById(R.id.btnLogout);
        FloatingActionButton fabQr = findViewById(R.id.fabQr);

        // ================== DRAWER OPEN ==================
        ivMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // ================== DRAWER CLICKS ==================
        tvClasses.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Classes clicked", Toast.LENGTH_SHORT).show();
        });

        tvCalendar.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Calendar clicked", Toast.LENGTH_SHORT).show();
        });

        tvFilters.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ColorFragmentsActivity.class));
        });

        tvStudentInfo.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, DataSendActivity.class));
        });

        tvCreateClass.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, CreateClassActivity.class));
        });

        tvJoinClass.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, JoinClassActivity.class));
        });

        // ================== QR SCAN ==================
        fabQr.setOnClickListener(v ->
                startActivity(new Intent(
                        ProductActivity.this,
                        QrScanActivity.class
                ))
        );

        // ================== LOGOUT ==================
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor =
                    getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(
                    ProductActivity.this,
                    LoginActivity.class
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // ================== BACK PRESS ==================
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            setEnabled(false);
                            getOnBackPressedDispatcher().onBackPressed();
                        }
                    }
                });
    }
}
