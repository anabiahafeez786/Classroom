package com.example.classroom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUser, etPass;
    DatabaseReference dbRef;

    private static final String CHANNEL_ID = "login_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 SESSION CHECK
        SharedPreferences prefs =
                getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(this, ProductActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etLoginUser);
        etPass = findViewById(R.id.etLoginPass);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        TextView tvSignupLink = findViewById(R.id.tvSignupLink);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        dbRef = FirebaseDatabase.getInstance().getReference("users");

        createNotificationChannel();

        btnLogin.setOnClickListener(v -> login());

        tvSignupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(
                    LoginActivity.this,
                    ForgotPasswordActivity.class
            ));
        });
    }

    private void login() {

        String email = text(etUser);
        String pass  = text(etPass);

        if (email.isEmpty()) {
            etUser.setError("Email required");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            etUser.setError("Invalid email");
            return;
        }

        if (pass.isEmpty()) {
            etPass.setError("Password required");
            return;
        }

        dbRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            toast("User not found");
                            return;
                        }

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            User user = snap.getValue(User.class);

                            if (user != null && user.password.equals(pass)) {

                                // ✅ SAVE SESSION
                                SharedPreferences.Editor editor =
                                        getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("userEmail", email);
                                editor.apply();

                                toast("Login successful");

                                // 🔔 PUSH NOTIFICATION
                                showLoginNotification();

                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        ProductActivity.class
                                );
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                toast("Incorrect password");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        toast("Database error");
                    }
                });
    }

    // ================= NOTIFICATION =================
    private void showLoginNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Login Successful")
                        .setContentText("You have logged in successfully.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(1001, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Login Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
