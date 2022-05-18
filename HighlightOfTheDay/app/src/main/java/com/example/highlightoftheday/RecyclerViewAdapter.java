package com.example.highlightoftheday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.HighlightViewHolder> {

    Context context;
    ArrayList<FirebaseHighlights> firebaseHighlights;
    ArrayList<HighlightViewHolder> highlightViewHolders = new ArrayList<HighlightViewHolder>();

    public RecyclerViewAdapter(Context context, ArrayList<FirebaseHighlights> firebaseHighlights) {
        this.context = context;
        this.firebaseHighlights = firebaseHighlights;
    }

    @NonNull
    @Override
    // Gets the layout of each of the highlights
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.highlight_view, parent, false);
        HighlightViewHolder viewHolder = new HighlightViewHolder(view, firebaseHighlights, context);
        highlightViewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    // Changes data based on the position of the items
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        //holder.dateTextView.setText(firebaseHighlights.get(position).getDate().toString());
        //holder.imageView.setImageResource(firebaseHighlights.get(position).getImage());
        //holder.descriptionTextView.setText(String.valueOf(firebaseHighlights.get(position).getDescription()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder {
        ArrayList<FirebaseHighlights> models;
        View rootView;
        Context context;
        ImageView imageView;
        TextView dateTextView;
        TextView descriptionTextView;

        public HighlightViewHolder(@NonNull View itemView, ArrayList<FirebaseHighlights> models, Context context) {
            super(itemView);
            this.models = models;
            this.context = context;
            rootView = itemView;
            imageView = itemView.findViewById(R.id.highlight_image);
            dateTextView = itemView.findViewById(R.id.highlight_date);
            descriptionTextView = itemView.findViewById(R.id.highlight_description);

        }
    }
}


