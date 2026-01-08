package com.example.classroom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinClassActivity extends AppCompatActivity {

    private EditText etClassCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        ImageView ivBack = findViewById(R.id.ivBackJoin);
        Button btnJoin   = findViewById(R.id.btnJoinTop);
        etClassCode      = findViewById(R.id.etClassCode);

        ivBack.setOnClickListener(v -> finish());

        btnJoin.setOnClickListener(v -> {
            String code = etClassCode.getText().toString().trim();

            if (code.isEmpty()) {
                etClassCode.setError("Class code is required");
                etClassCode.requestFocus();
                return;
            }

            String msg = "Joined class with code: " + code;
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });
    }
}
