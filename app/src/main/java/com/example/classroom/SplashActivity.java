package com.example.classroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.gifLogo);

        // Load GIF / Logo
        Glide.with(this).load(R.drawable.classroom_logo).into(logo);

        // Animation (unchanged)
        logo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                logo.getViewTreeObserver().removeOnPreDrawListener(this);

                float startOffset = dp(140);
                logo.setTranslationY(startOffset);
                logo.setAlpha(0f);

                logo.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(900)
                        .setInterpolator(new DecelerateInterpolator(1.8f))
                        .start();
                return true;
            }
        });

        // 🔑 AUTO-LOGIN CHECK AFTER SPLASH
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            SharedPreferences prefs =
                    getSharedPreferences("loginPrefs", MODE_PRIVATE);

            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // User already logged in → Product
                startActivity(new Intent(SplashActivity.this, ProductActivity.class));
            } else {
                // New user → Signup
                startActivity(new Intent(SplashActivity.this, SignupActivity.class));
            }

            finish();
        }, 3000);
    }

    private float dp(float v) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                v,
                getResources().getDisplayMetrics()
        );
    }
}
