package com.example.highlightoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton add;
    RecyclerViewAdapter adapter;
    ArrayList<HighlightModel> highlightModels = new ArrayList<>();
    ArrayList<Map> documents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.highlightRecyclerView);

        // Set up button
        add = findViewById(R.id.add);
        add.setOnClickListener(view -> goToHighlightCreator());

        adapter = new RecyclerViewAdapter(this);

        // Attach adapter to view
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrieveDbDocuments();

    }

    private void retrieveDbDocuments(){
                FirebaseFirestore.getInstance().collection("highlights").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map data = document.getData();
                                    documents.add(data);
                            }
                            try {
                                setUpHighlightModels();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void setUpHighlightModels() throws ParseException {
        for (Map document: documents) {
            Timestamp timestamp = (Timestamp) document.get("date");
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String date = formatter.format(timestamp.toDate());
            String description = document.get("description").toString();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageRef.child((String) document.get("imageURL"));

            final long ONE_MEGABYTE = 1024 * 1024;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Drawable d = Drawable.createFromStream(new ByteArrayInputStream(bytes), null);
                    highlightModels.add(new HighlightModel(date, d, description));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }

    }


    private void goToHighlightCreator() {
        Intent switchActivityIntent = new Intent(this, HighlightCreator.class);
        startActivity(switchActivityIntent);
    };
}