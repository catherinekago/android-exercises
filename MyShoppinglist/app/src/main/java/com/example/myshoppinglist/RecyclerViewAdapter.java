package com.example.myshoppinglist;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ShoppingListViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ShoppingListModel> shoppingListModels;

    public RecyclerViewAdapter(Context context, ArrayList<ShoppingListModel> shoppingListModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.shoppingListModels = shoppingListModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    // Gets the layout of each of the rows
    public RecyclerViewAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerviewrow, parent, false);
        return new RecyclerViewAdapter.ShoppingListViewHolder(view, recyclerViewInterface, shoppingListModels, context);
    }

    @Override
    // Changes data based on the position of the items
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ShoppingListViewHolder holder, int position) {
        holder.nameTextView.setText(shoppingListModels.get(position).getName());
        holder.countTextView.setText(String.valueOf(shoppingListModels.get(position).getCount()));
        holder.imageView.setImageResource(shoppingListModels.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return shoppingListModels.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        ArrayList<ShoppingListModel> models;
        View rootView;
        Context context;
        ImageView imageView;
        TextView nameTextView;
        // Counter
        TextView countTextView;
        AppCompatButton add;
        AppCompatButton remove;
        int counter;
        //Vibration: requires context to work
        Vibrator vibrator;
        final long[] patternAdd = {0, 500, 250, 500};
        final long[] patternRemove = {0, 1250};

        public ShoppingListViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, ArrayList<ShoppingListModel> models, Context context) {
            super(itemView);
            this.models = models;
            this.context = context;
            rootView = itemView;
            imageView = itemView.findViewById(R.id.img_avocado);
            nameTextView = itemView.findViewById(R.id.text_avocado);
            vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

            add = itemView.findViewById(R.id.add);
            add.setText("+");
            add.setOnClickListener(view -> incrementCounter());
                    remove = itemView.findViewById(R.id.remove);
            remove.setText("-");
            remove.setOnClickListener(view -> decrementCounter());
            countTextView = itemView.findViewById(R.id.count);
            initCounter();

            // attach onclick listener to item view
            itemView.setOnClickListener(view -> {
                            if (recyclerViewInterface != null){
                                int position = getAdapterPosition();
                                recyclerViewInterface.onItemClick(position);
                            }
            });
        }

        private void initCounter() {
           counter = 0;
           countTextView.setText("0");
        }

        @SuppressLint("ResourceAsColor")
        private void incrementCounter(){
            if (counter == 0){
                remove.setVisibility(View.VISIBLE);
            }
           counter ++;
            for (int i = 0; i < models.size(); i++) {
                if (models.get(i).getName() == (String) nameTextView.getText()) {
                    models.get(i).setCount(counter);
                    countTextView.setText(models.get(i).getCount() + "");
                    // Vibrate
                    vibrator.vibrate(patternAdd, -1);
                }
            }

        }

        @SuppressLint("ResourceAsColor")
        private void decrementCounter(){
            if (counter -1 >= 0){
                counter --;
                for (int i = 0; i < models.size(); i++) {
                    if (models.get(i).getName() == (String) nameTextView.getText()) {
                        models.get(i).setCount(counter);
                        countTextView.setText(models.get(i).getCount() + "");
                        // Vibrate
                        vibrator.vibrate(patternRemove, -1);
                    }
                }
            }
            if (counter == 0){
                remove.setVisibility(View.INVISIBLE);
            }
        }
    }
}
