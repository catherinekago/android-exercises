package com.example.highlightoftheday;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        saveHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewHighlight();
            }
        });

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


