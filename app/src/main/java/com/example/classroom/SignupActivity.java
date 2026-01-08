package com.example.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etFirst, etLast, etUser, etPass, etConfirm;
    private TextInputLayout tilFirst, tilLast, tilUser, tilPass, tilConfirm;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // EditTexts
        etFirst   = findViewById(R.id.etFirst);
        etLast    = findViewById(R.id.etLast);
        etUser    = findViewById(R.id.etUser);   // USERNAME ONLY
        etPass    = findViewById(R.id.etPass);
        etConfirm = findViewById(R.id.etConfirm);

        // Layouts
        tilFirst   = findViewById(R.id.tilFirst);
        tilLast    = findViewById(R.id.tilLast);
        tilUser    = findViewById(R.id.tilUser);
        tilPass    = findViewById(R.id.tilPass);
        tilConfirm = findViewById(R.id.tilConfirm);

        // Remove error icons
        tilFirst.setErrorIconDrawable(null);
        tilLast.setErrorIconDrawable(null);
        tilUser.setErrorIconDrawable(null);
        tilPass.setErrorIconDrawable(null);
        tilConfirm.setErrorIconDrawable(null);

        // Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        // 🔹 SIGNUP BUTTON
        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> handleSignup());

        // 🔹 LOGIN LINK (NEW CHANGE)
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

    private void handleSignup() {

        clearErrors();

        String first = text(etFirst);
        String last  = text(etLast);
        String username = text(etUser);          // user types ONLY username
        String email = username + "@gmail.com";  // auto append
        String pass  = text(etPass);
        String conf  = text(etConfirm);

        // FIRST NAME VALIDATION
        if (!first.matches("^[A-Za-z]{2,}$")) {
            tilFirst.setError("Enter a valid first name");
            return;
        }

        // LAST NAME VALIDATION
        if (!last.matches("^[A-Za-z]{2,}$")) {
            tilLast.setError("Enter a valid last name");
            return;
        }

        // USERNAME VALIDATION
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9._]{2,}$")) {
            tilUser.setError("Invalid username");
            return;
        }

        // PASSWORD VALIDATION
        if (pass.length() < 6) {
            tilPass.setError("Password must be at least 6 characters");
            return;
        }

        // CONFIRM PASSWORD
        if (!pass.equals(conf)) {
            tilConfirm.setError("Passwords do not match");
            return;
        }

        // SAVE TO FIREBASE
        String userId = dbRef.push().getKey();
        if (userId == null) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(userId).child("firstName").setValue(first);
        dbRef.child(userId).child("lastName").setValue(last);
        dbRef.child(userId).child("username").setValue(username);
        dbRef.child(userId).child("email").setValue(email);
        dbRef.child(userId).child("password").setValue(pass);

        Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();

        // MOVE TO LOGIN AFTER SIGNUP
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        }, 500);
    }

    private void clearErrors() {
        tilFirst.setError(null);
        tilLast.setError(null);
        tilUser.setError(null);
        tilPass.setError(null);
        tilConfirm.setError(null);
    }

    private String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
