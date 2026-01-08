package com.example.classroom;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DataReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_receive);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvAge  = findViewById(R.id.tvAge);
        TextView tvDate = findViewById(R.id.tvDate);

        // Get data from Intent
        String name = getIntent().getStringExtra("student_name");
        String age  = getIntent().getStringExtra("student_age");
        String date = getIntent().getStringExtra("student_date");

        // Show it
        tvName.setText("Name: " + name);
        tvAge.setText("Age: " + age);
        tvDate.setText("Joining Date: " + date);
    }
}
