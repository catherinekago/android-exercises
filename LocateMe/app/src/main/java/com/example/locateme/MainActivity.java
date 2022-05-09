package com.example.locateme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText latitude;
    private EditText longitude;
    private AppCompatButton open;

    private final Double LONGITUDE_RANGE = 180.00;
    private final Double LATITUDE_RANGE = 90.00;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        open = findViewById(R.id.open);
        open.setAlpha(.5f);


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
                    if (open.isEnabled()){
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
        Boolean isLongitudeValid = Math.abs(Double.parseDouble(longitude.getText().toString())) <= LONGITUDE_RANGE;
        Boolean isLatitudeValid = Math.abs(Double.parseDouble(latitude.getText().toString())) <= LATITUDE_RANGE;

        // Perform localization if values are valid
        if (isLongitudeValid && isLatitudeValid){


            // Show toast if any given value is out of range
        } else if (!isLongitudeValid && !isLatitudeValid){
            showToast("Please make sure to use valid coordinates: -180 <= longitude <= 180, -90 <= latitude <= 90");
        } else if (!isLongitudeValid){
            showToast("Please make sure to use valid coordinates: -180 <= longitude <= 180");
        } else {
            showToast("Please make sure to use valid coordinates: -90 <= latitude <= 90");
        }

    }

    private void showToast(String text) {
        Toast errorToast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
        errorToast.setGravity(Gravity.TOP,0,0);
        errorToast.show();
    }
}
