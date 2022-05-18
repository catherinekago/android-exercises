package com.example.highlightoftheday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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