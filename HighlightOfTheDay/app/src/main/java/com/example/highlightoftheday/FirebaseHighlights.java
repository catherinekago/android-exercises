package com.example.highlightoftheday;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHighlights {
    private FirebaseFirestore db;

    public FirebaseHighlights() {
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
