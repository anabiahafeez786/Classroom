package com.example.classroom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DataSendActivity extends AppCompatActivity {

    private EditText etName, etAge, etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_send);

        etName = findViewById(R.id.etName);
        etAge  = findViewById(R.id.etAge);
        etDate = findViewById(R.id.etDate);
        Button btnSend = findViewById(R.id.btnSend);

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        btnSend.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String age  = etAge.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Required");
                etName.requestFocus();
                return;
            }
            if (age.isEmpty()) {
                etAge.setError("Required");
                etAge.requestFocus();
                return;
            }
            if (date.isEmpty()) {
                Toast.makeText(this, "Please select joining date", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent(DataSendActivity.this, DataReceiveActivity.class);
            i.putExtra("student_name", name);
            i.putExtra("student_age", age);
            i.putExtra("student_date", date);
            startActivity(i);
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) -> {
                    // m is 0-based month, so add 1
                    String selected = d + "/" + (m + 1) + "/" + y;
                    etDate.setText(selected);
                },
                year, month, day
        );
        dialog.show();
    }
}
