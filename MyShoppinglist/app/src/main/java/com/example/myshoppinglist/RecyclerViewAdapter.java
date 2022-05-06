package com.example.myshoppinglist;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ShoppingListViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ShoppingListModel> shoppingListModels;
    ArrayList<ShoppingListViewHolder> shoppingListViewHolders = new ArrayList<>();

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
        ShoppingListViewHolder viewHolder = new RecyclerViewAdapter.ShoppingListViewHolder(view, recyclerViewInterface, shoppingListModels, context);
        shoppingListViewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    // Changes data based on the position of the items
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ShoppingListViewHolder holder, int position) {
        holder.nameTextView.setText(shoppingListModels.get(position).getName());
        holder.countButtonTextView.setText(String.valueOf(shoppingListModels.get(position).getCount()));
        holder.imageView.setImageResource(shoppingListModels.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return shoppingListModels.size();
    }

    public void switchControls(boolean isControlTypeButtons) {
        if (isControlTypeButtons) {
            for (int i = 0; i < shoppingListViewHolders.size(); i++) {

                // Hide remove button if current count equals 0
                if (shoppingListViewHolders.get(i).counter > 0) {
                    shoppingListViewHolders.get(i).remove.setVisibility(View.VISIBLE);
            } else {
                    shoppingListViewHolders.get(i).remove.setVisibility(View.INVISIBLE);
                }

                // Hide add button if current count equals 25
                if (shoppingListViewHolders.get(i).counter < 25) {
                    shoppingListViewHolders.get(i).add.setVisibility(View.VISIBLE);
                } else {
                    shoppingListViewHolders.get(i).add.setVisibility(View.INVISIBLE);
                }

                shoppingListViewHolders.get(i).countButtonTextView.setVisibility(View.VISIBLE);
                // Update displayed count
                shoppingListViewHolders.get(i).countButtonTextView.setText(shoppingListViewHolders.get(i).models.get(i).getCount() + "");
                // Remove sliders
                shoppingListViewHolders.get(i).sliderContainer.setVisibility(View.GONE);
            }


            } else {
            for (int i = 0; i < shoppingListViewHolders.size(); i++) {
                // Hide buttons
                shoppingListViewHolders.get(i).remove.setVisibility(View.GONE);
                shoppingListViewHolders.get(i).add.setVisibility(View.GONE);
                shoppingListViewHolders.get(i).countButtonTextView.setVisibility(View.GONE);
                // Show sliders
                shoppingListViewHolders.get(i).sliderContainer.setVisibility(View.VISIBLE);
                shoppingListViewHolders.get(i).slider.setVisibility(View.VISIBLE);
                shoppingListViewHolders.get(i).countSliderTextView.setVisibility(View.VISIBLE);
                // Update displayed count
                shoppingListViewHolders.get(i).countSliderTextView.setText(shoppingListViewHolders.get(i).models.get(i).getCount() + "");
                shoppingListViewHolders.get(i).slider.setProgress(shoppingListViewHolders.get(i).models.get(i).getCount());
            }
        }

        }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        ArrayList<ShoppingListModel> models;
        View rootView;
        Context context;
        ImageView imageView;
        TextView nameTextView;
        // Counter Button
        TextView countButtonTextView;
        AppCompatButton add;
        AppCompatButton remove;
        LinearLayout buttonContainer;
        // Counter Slider
        TextView countSliderTextView;
        SeekBar slider;
        int counter;
        LinearLayout sliderContainer;
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

            // Set up buttons
            add = itemView.findViewById(R.id.add);
            add.setText("+");
            add.setOnClickListener(view -> incrementCounter());
                    remove = itemView.findViewById(R.id.remove);
            remove.setText("-");
            remove.setOnClickListener(view -> decrementCounter());
            countButtonTextView = itemView.findViewById(R.id.count_button);
            buttonContainer = itemView.findViewById(R.id.button_container);

            // Setup slider
            countSliderTextView = itemView.findViewById(R.id.count_slider);
            slider = itemView.findViewById(R.id.slider);
            sliderContainer = itemView.findViewById(R.id.slider_container);
            sliderContainer.setVisibility(View.GONE);

            initCounter();

            // attach onclick listener to item view
            itemView.setOnClickListener(view -> {
                            if (recyclerViewInterface != null){
                                int position = getAdapterPosition();
                                recyclerViewInterface.onItemClick(position);
                            }
            });
            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    counter = progress;
                    for (int i = 0; i < models.size(); i++) {
                        if (models.get(i).getName() == (String) nameTextView.getText()) {
                            models.get(i).setCount(counter);
                            countSliderTextView.setText(models.get(i).getCount() + "");
                        }
                    }

                }
            });

        }

        private void initCounter() {
           counter = 0;
           countButtonTextView.setText("0");
           countSliderTextView.setText("0");
        }

        @SuppressLint("ResourceAsColor")
        private void incrementCounter(){
            if (counter == 0){
                remove.setVisibility(View.VISIBLE);
            }
            if (counter < 25) {
                counter ++;
                for (int i = 0; i < models.size(); i++) {
                    if (models.get(i).getName() == (String) nameTextView.getText()) {
                        models.get(i).setCount(counter);
                        countButtonTextView.setText(models.get(i).getCount() + "");
                        // Vibrate
                        vibrator.vibrate(patternAdd, -1);
                    }
                }
            }
            if (counter == 25) {
                add.setVisibility(View.INVISIBLE);
            }
        }

        @SuppressLint("ResourceAsColor")
        private void decrementCounter(){
            if (counter -1 >= 0){
                counter --;
                for (int i = 0; i < models.size(); i++) {
                    if (models.get(i).getName() == (String) nameTextView.getText()) {
                        models.get(i).setCount(counter);
                        countButtonTextView.setText(models.get(i).getCount() + "");
                        // Vibrate
                        vibrator.vibrate(patternRemove, -1);
                    }
                }
            }
            if (counter == 0){
                remove.setVisibility(View.INVISIBLE);
            }
            if (counter < 25){
                add.setVisibility(View.VISIBLE);
            }
        }
    }
}
