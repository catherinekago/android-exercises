package com.example.highlightoftheday;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HighlightCreator extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    FirebaseFirestore firebase;
    CollectionReference highlights;

    EditText highlightDate;
    AppCompatButton chooseImage;
    EditText highlightDescription;
    AppCompatButton saveHighlight;
    ImageView highlightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_creator);
        firebase = FirebaseFirestore.getInstance();
        highlights = firebase.collection("highlights");

        chooseImage = findViewById(R.id.choose_image);
        highlightDescription = findViewById(R.id.edit_description);
        saveHighlight = findViewById(R.id.save_highlight);
        highlightDate = findViewById(R.id.date_text);
        highlightImage = findViewById(R.id.imageView);

        highlightDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String formattedDate = formatDate(year, monthOfYear, dayOfMonth);
                highlightDate.setText(formattedDate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        highlightDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                boolean pick= true;
                if (pick){
                  if(!checkCameraPermission()){
                    requestCameraPermission();
                  } else {
                      pickImage();
                  }
                } else {
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    } else {
                        pickImage();
                    }
                }
            }
        });

        saveHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewHighlight();
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void pickImage() {
        CropImage.activity()
                .setFixAspectRatio(true)
                .setAspectRatio(1,1).start(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private boolean checkStoragePermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try{
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    highlightImage.setImageBitmap(bitmap);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        String yy = String.valueOf(year);
        String mm = String.valueOf(monthOfYear+1);
        String dd = String.valueOf(dayOfMonth);
        if (monthOfYear < 10) {
            mm = "0"+monthOfYear;
        }
        if (dayOfMonth <10){
            dd = "0"+dayOfMonth;
        }

        return (dd + "/" + mm + "/" + yy);
    }

    private void addNewHighlight () {
            highlights
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int documentCount = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    documentCount++;
                                }
                                try {
                                    createHighlightObject(documentCount+1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }

                        private void createHighlightObject(int documentCount) throws ParseException {
                            // Save photo to storage

                            // Create new highlight object
                            Map<String, Object> highlight = new HashMap<>();
                            if(highlightDate.getText().toString().length() >0){
                                highlight.put("date", new SimpleDateFormat("dd/MM/yyyy").parse(highlightDate.getText().toString()));
                            } else {
                                highlight.put("date", new Date());
                            }

                            // https://www.youtube.com/watch?v=g2Iibnnqga0&ab_channel=Foxandroid
                            // https://www.youtube.com/watch?v=lFPmgtD4lJg&ab_channel=BrandanJones
                            highlight.put("description", highlightDescription.getText().toString());

                            // Add to collection
                            highlights.document("highlight_" + documentCount)
                                    .set(highlight)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        }


                    });
        }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}


