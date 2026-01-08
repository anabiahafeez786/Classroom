package com.example.classroom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateClassActivity extends AppCompatActivity {

    private EditText etClassName, etSection, etRoom, etSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        ImageView ivBack = findViewById(R.id.ivBackCreate);
        Button btnCreate = findViewById(R.id.btnCreateTop);

        etClassName = findViewById(R.id.etClassName);
        etSection   = findViewById(R.id.etSection);
        etRoom      = findViewById(R.id.etRoom);
        etSubject   = findViewById(R.id.etSubject);

        // Back arrow -> close this screen
        ivBack.setOnClickListener(v -> finish());

        // Create button -> simple validation + Toast (for viva demo)
        btnCreate.setOnClickListener(v -> {
            String name = etClassName.getText().toString().trim();

            if (name.isEmpty()) {
                etClassName.setError("Class name is required");
                etClassName.requestFocus();
                return;
            }

            String section = etSection.getText().toString().trim();
            String room    = etRoom.getText().toString().trim();
            String subject = etSubject.getText().toString().trim();

            String msg = "Class created:\n"
                    + "Name: " + name + "\n"
                    + "Section: " + section + "\n"
                    + "Room: " + room + "\n"
                    + "Subject: " + subject;

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });
    }
}
