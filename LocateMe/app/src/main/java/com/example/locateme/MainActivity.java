package com.example.locateme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private EditText latitude;
    private EditText longitude;
    private AppCompatButton open;

    private Location location;

    private final Double LONGITUDE_MAX = 180.00;
    private final Double LATITUDE_MAX = 90.00;
    private final Double LONGITUDE_MIN = -180.00;
    private final Double LATITUDE_MIN = -90.00;

    private FusedLocationProviderClient mFusedLocationClient;
    private SensorManager sensorManager;
    private long lastUpdate;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize views
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        open = findViewById(R.id.open);
        open.setAlpha(.5f);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        // Text Watcher input fields
        TextWatcher inputValidation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (longitude.getText().toString().length() > 0 && latitude.getText().toString().length() > 0) {
                    open.setAlpha(1f);
                    open.setEnabled(true);
                } else {
                    if (open.isEnabled()) {
                        open.setAlpha(.5f);
                        open.setEnabled(false);
                    }
                }
            }
        };

        // Add text watcher change listener to input fields to validate if they are filled
        latitude.addTextChangedListener(inputValidation);
        longitude.addTextChangedListener(inputValidation);
    }

    // Handle OnClick event on button
    public void searchLocation(View view) {
        Boolean isLongitudeValid = Math.abs(Double.parseDouble(longitude.getText().toString())) <= LONGITUDE_MAX;
        Boolean isLatitudeValid = Math.abs(Double.parseDouble(latitude.getText().toString())) <= LATITUDE_MAX;

        // Perform localization if values are valid
        if (isLongitudeValid && isLatitudeValid){
            openGoogleMaps();

            // Show toast if any given value is out of range
        } else if (!isLongitudeValid && !isLatitudeValid){
            showToast("Error! Required LONG between -180 and 180, and LAT between: -90 and 90");
        } else if (!isLongitudeValid){
            showToast("Please enter a LONG value between -180 and 180");
        } else {
            showToast("Please enter a LAT value between -90 and 90");
        }

    }

    // Open google maps with the provided coordinates
    private void openGoogleMaps() {
        Uri intentUri = Uri.parse("geo:" + latitude.getText() + "," + longitude.getText());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void showToast(String text) {
        Toast errorToast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
        errorToast.setGravity(Gravity.TOP,0,0);
        errorToast.show();
    }

    public void locateMe(View view) {
        // Get current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != -1 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != -1) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location newLocation) {
                        // Got last known location. In some rare situations this can be null.
                        if (newLocation != null) {
                            // Logic to handle location object
                            location = newLocation;

                        }
                    }
                });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        // calculate acceleration vector
        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        // Check if movement is above threshold
        if (accelationSquareRoot >= 2)
        {
            // Check if enough time has passed between two events
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            randomInput();
        }
    }

    // Create random values for longitude and latitude and display them in the input fields
    private void randomInput() {
        Random r = new Random();
        double randomLongitude = LONGITUDE_MAX + (LONGITUDE_MAX - LONGITUDE_MIN) * r.nextDouble();
        double randomLatitude = LATITUDE_MAX + (LATITUDE_MAX - LATITUDE_MIN) * r.nextDouble();
        longitude.setText(randomLongitude + "");
        latitude.setText(randomLatitude + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
