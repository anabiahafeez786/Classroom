package com.example.classroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUser, etPass;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 SESSION CHECK (IMPORTANT)
        SharedPreferences prefs =
                getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(this, ProductActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etLoginUser);   // FULL EMAIL
        etPass = findViewById(R.id.etLoginPass);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        // 🔹 SIGNUP LINK
        TextView tvSignupLink = findViewById(R.id.tvSignupLink);

        // 🔹 FORGOT PASSWORD LINK
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        dbRef = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(v -> login());

        // 🔹 GO TO SIGNUP
        tvSignupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        // 🔹 GO TO FORGOT PASSWORD
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,
                    ForgotPasswordActivity.class));
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

        // 🔹 READ FROM FIREBASE
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
                                SharedPreferences prefs =
                                        getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("userEmail", email);
                                editor.apply();

                                toast("Login successful");

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

    private String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
