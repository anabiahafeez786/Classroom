package com.example.classroom;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ColorFragmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_fragments);

        Button btnRed   = findViewById(R.id.btnRed);
        Button btnGreen = findViewById(R.id.btnGreen);
        Button btnBlue  = findViewById(R.id.btnBlue);

        // Default fragment when screen opens (Red: Upcoming assignments)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new RedFragment())
                .commit();

        btnRed.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new RedFragment())
                        .commit()
        );

        btnGreen.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new GreenFragment())
                        .commit()
        );

        btnBlue.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BlueFragment())
                        .commit()
        );
    }
}
