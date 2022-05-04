package com.example.myshoppinglist;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String name = getIntent().getStringExtra("NAME");
        int image = getIntent().getIntExtra("IMAGE", 0);
        String description = getIntent().getStringExtra("DESCRIPTION");

        TextView nameTextView = findViewById(R.id.name);
        ImageView imageView = findViewById(R.id.img);
        TextView descriptionTextView = findViewById(R.id.description);

        nameTextView.setText(name);
        imageView.setImageResource(image);
        descriptionTextView.setText(description);

    }
}
