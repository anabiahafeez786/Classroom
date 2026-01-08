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

public class ProductActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        drawerLayout = findViewById(R.id.drawerLayout);

        ImageView ivMenu       = findViewById(R.id.ivMenu);
        TextView tvClasses     = findViewById(R.id.tvMenuClasses);
        TextView tvCalendar    = findViewById(R.id.tvMenuCalendar);
        TextView tvFilters     = findViewById(R.id.tvMenuFilters);
        TextView tvStudentInfo = findViewById(R.id.tvMenuStudentInfo);
        TextView tvCreateClass = findViewById(R.id.tvMenuCreateClass);
        TextView tvJoinClass   = findViewById(R.id.tvMenuJoinClass);
        Button btnLogout       = findViewById(R.id.btnLogout);

        // ☰ Open drawer
        ivMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // Classes
        tvClasses.setOnClickListener(v -> {
            Toast.makeText(this, "Classes clicked", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Calendar
        tvCalendar.setOnClickListener(v -> {
            Toast.makeText(this, "Calendar clicked", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Class Info Filters
        tvFilters.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ColorFragmentsActivity.class));
        });

        // Student Info
        tvStudentInfo.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, DataSendActivity.class));
        });

        // Create class
        tvCreateClass.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, CreateClassActivity.class));
        });

        // Join class
        tvJoinClass.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, JoinClassActivity.class));
        });

        // 🔴 LOGOUT FUNCTIONALITY (IMPORTANT)
        btnLogout.setOnClickListener(v -> {

            // Clear login session
            SharedPreferences prefs =
                    getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Go to LoginActivity
            Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Handle back button for drawer
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
