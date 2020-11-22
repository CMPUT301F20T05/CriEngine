package com.example.criengine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.criengine.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * The main activity for all things scan related.
 *
 * code referenced from https://medium.com/analytics-vidhya/creating-a-barcode-scanner-using-android-studio-71cff11800a2
 * Accessed Nov 5 2020
 */
public class ScanActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CAMERA = 77;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private TextView barcodeText;
    private String barcodeData;
    private Button confirmScanButton;

    /**
     * Called upon creation of activity; code referenced from https://stackoverflow.com/questions/37251823/camera-not-opening-after-granting-the-permission-in-surface-view
     * Accessed Nov 20, 2020
     * @param savedInstanceState  If the activity is being re-initialized after previously being
     *                            shut down then this Bundle contains the data it most recently
     *                            supplied. Note: Otherwise it is null. This value may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If camera permission is not granted, request permission.
        // Camera permission is required to start the scan
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            return;
        }
        initialize();
    }

    /**
     * Once the user clicks yes or no on granting the app camera access, process the result here.
     * @param requestCode   what kind of request was asked
     * @param permissions   the permission(s)
     * @param grantResults  whether the permission(s) was granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            // If request is cancelled, move user to page before
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialize();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Initialize the activity
     */
    private void initialize() {
        setContentView(R.layout.activity_scan);
        surfaceView = findViewById(R.id.scan_surface_view);
        barcodeText = findViewById(R.id.barcode_text);

        initialiseDetectorsAndSources();
        barcodeText.setText(R.string.scan_prompt);
        confirmScanButton = findViewById(R.id.scan_confirm_button);

        confirmScanButton.setEnabled(false);

        confirmScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("barcode", barcodeData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Initialize the camera source and barcode detector to be used and detect if there are barcodes in the camera source
     */
    private void initialiseDetectorsAndSources() {
        // new barcode detector object
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        // new camera source object
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    // verify permissions, else request permissions
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // required function
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            // required function
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                            }
                            // once barcode data obtained, enable the confirmation button to return to previous activity with barcode data.
                            barcodeText.setText(R.string.scan_complete);
                            confirmScanButton.setEnabled(true);
                        }
                    });
                }
            }
        });
    }
}