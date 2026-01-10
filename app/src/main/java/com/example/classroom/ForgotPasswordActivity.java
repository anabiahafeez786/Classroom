package com.example.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.*;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etNewPass;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail   = findViewById(R.id.etEmail);
        etNewPass = findViewById(R.id.etNewPass);
        MaterialButton btnReset = findViewById(R.id.btnReset);

        dbRef = FirebaseDatabase.getInstance().getReference("users");

        btnReset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {

        String email = text(etEmail);
        String newPass = text(etNewPass);

        if (email.isEmpty()) {
            etEmail.setError("Email required");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            etEmail.setError("Invalid email");
            return;
        }

        if (newPass.length() < 6) {
            etNewPass.setError("Password must be at least 6 characters");
            return;
        }

        // 🔹 FIND USER BY EMAIL
        dbRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            toast("Email not found");
                            return;
                        }

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            snap.getRef().child("password").setValue(newPass);
                        }

                        toast("Password reset successful");

                        // GO BACK TO LOGIN
                        startActivity(new Intent(
                                ForgotPasswordActivity.this,
                                LoginActivity.class
                        ));
                        finish();
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
