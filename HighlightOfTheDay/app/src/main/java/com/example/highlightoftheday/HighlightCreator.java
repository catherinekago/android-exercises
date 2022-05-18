package com.example.highlightoftheday;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.DocumentTransform;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HighlightCreator extends AppCompatActivity {
    FirebaseFirestore firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_creator);
        firebase = FirebaseFirestore.getInstance();
        CollectionReference highlights = firebase.collection("highlights");

        //Get number of documents saved

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
                            addNewHighlight(documentCount+1);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                    private void addNewHighlight(int documentCount) {

                        // Create new object
                        Map<String, Object> highlight = new HashMap<>();
                        highlight.put("date", new Date());
                        // https://www.youtube.com/watch?v=g2Iibnnqga0&ab_channel=Foxandroid
                        // https://www.youtube.com/watch?v=lFPmgtD4lJg&ab_channel=BrandanJones
                        highlight.put("image", 1111222);
                        highlight.put("description", "testtest");

                        // Add to collection
                        highlights.document("highlight_" + documentCount)
                                .set(highlight)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) { }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) { }
                                });
                    }

                });


    }
}
