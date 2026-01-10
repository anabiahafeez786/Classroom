package com.example.classroom;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QrScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        MaterialButton btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(v -> startScan());
    }

    private void startScan() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);

        // ✅ CORRECT CLASS (IMPORTANT FIX)
        options.setCaptureActivity(CustomCaptureActivity.class);

        barcodeLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions>
            barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Toast.makeText(
                            this,
                            "Scanned: " + result.getContents(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            this,
                            "Scan cancelled",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
    );
}
