package com.example.highlightoftheday;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.HighlightViewHolder> {

    Context context;
    CollectionReference firebaseHighlights;
    ArrayList<HighlightViewHolder> highlightViewHolders = new ArrayList<HighlightViewHolder>();

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        this.firebaseHighlights = FirebaseFirestore.getInstance().collection("highlights");
    }

    @NonNull
    @Override
    // Gets the layout of each of the highlights
    public RecyclerViewAdapter.HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.highlight_view, parent, false);
        HighlightViewHolder viewHolder = new HighlightViewHolder(view, context);
        highlightViewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    // Changes data based on the position of the items
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        holder.dateTextView.setText("test");
        //holder.imageView.setImageResource(firebaseHighlights.get(position).getImage());
        holder.descriptionTextView.setText("test");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder {
        CollectionReference collectionRef;
        View rootView;
        Context context;
        ImageView imageView;
        TextView dateTextView;
        TextView descriptionTextView;


        public HighlightViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            rootView = itemView;
            imageView = itemView.findViewById(R.id.highlight_image);
            dateTextView = itemView.findViewById(R.id.highlight_date);
            descriptionTextView = itemView.findViewById(R.id.highlight_description);

        }
    }
}


