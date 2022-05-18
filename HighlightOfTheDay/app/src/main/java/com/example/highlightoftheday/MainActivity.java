package com.example.highlightoftheday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.highlightRecyclerView);

        // Set up button
        add = findViewById(R.id.add);
        add.setOnClickListener(view -> goToHighlightCreator());

    }


    private void goToHighlightCreator() {
        Intent switchActivityIntent = new Intent(this, HighlightCreator.class);
        startActivity(switchActivityIntent);
    };
}