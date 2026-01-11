package com.example.classroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RewardedAd rewardedAd;

    // ================= REWARDED AD LOADER =================
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(
                this,
                "ca-app-pub-3940256099942544/5224354917", // TEST VIDEO AD
                adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        rewardedAd = null;
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ================= SESSION CHECK =================
        SharedPreferences prefs =
                getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_product);

        // ================= ADMOB INIT =================
        MobileAds.initialize(this, initializationStatus -> {});
        loadRewardedAd();

        // ================= BANNER AD =================
        AdView adView = findViewById(R.id.adView);
        View adContainer = findViewById(R.id.adContainer);
        ImageView btnCloseAd = findViewById(R.id.btnCloseAd);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnCloseAd.setOnClickListener(v ->
                adContainer.setVisibility(View.GONE)
        );

        // ================= VIEWS =================
        drawerLayout = findViewById(R.id.drawerLayout);

        ImageView ivMenu = findViewById(R.id.ivMenu);

        TextView tvClasses     = findViewById(R.id.tvMenuClasses);
        TextView tvCalendar    = findViewById(R.id.tvMenuCalendar);
        TextView tvFilters     = findViewById(R.id.tvMenuFilters);
        TextView tvStudentInfo = findViewById(R.id.tvMenuStudentInfo);
        TextView tvCreateClass = findViewById(R.id.tvMenuCreateClass);
        TextView tvJoinClass   = findViewById(R.id.tvMenuJoinClass);
        TextView tvMenuChat    = findViewById(R.id.tvMenuChat);

        Button btnLogout = findViewById(R.id.btnLogout);
        FloatingActionButton fabQr = findViewById(R.id.fabQr);

        // ================= DRAWER OPEN =================
        ivMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // ================= DRAWER CLICKS =================
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

        // ================= AI TUTOR / CHAT =================
        tvMenuChat.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            if (rewardedAd != null) {
                rewardedAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                rewardedAd = null;
                                loadRewardedAd();
                                startActivity(
                                        new Intent(
                                                ProductActivity.this,
                                                ChatActivity.class
                                        )
                                );
                            }
                        }
                );

                rewardedAd.show(ProductActivity.this, rewardItem -> {});
            } else {
                startActivity(
                        new Intent(
                                ProductActivity.this,
                                ChatActivity.class
                        )
                );
            }
        });

        // ================= QR SCAN =================
        fabQr.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                ProductActivity.this,
                                QrScanActivity.class
                        )
                )
        );

        // ================= LOGOUT (UPDATED) =================
        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor =
                    getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            // ✅ LOGOUT TOAST
            Toast.makeText(
                    ProductActivity.this,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    ProductActivity.this,
                    LoginActivity.class
            );
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
            finish();
        });

        // ================= BACK PRESS =================
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
