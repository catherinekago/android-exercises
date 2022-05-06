package com.example.myshoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<ShoppingListModel> shoppingListModels = new ArrayList<>();

    int[]ShoppingListImages = {R.drawable.avocado, R.drawable.bread,R.drawable.oil, R.drawable.tomato};

    boolean isControlTypeButtons = true;
    RadioButton buttonControl;
    RadioButton sliderControl;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonControl = findViewById(R.id.switch_button);
        sliderControl = findViewById(R.id.switch_slider);
        RecyclerView recyclerView = findViewById(R.id.shoppingListRecyclerView);

        setUpShoppingListModels();

        // Setup adapter AFTER models because models have to be passed TO the adapter
        adapter = new RecyclerViewAdapter(this, shoppingListModels, this);

        // Attach adapter to view
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpShoppingListModels(){
        String[] itemNames = getResources().getStringArray(R.array.shopping_item_names);
        String[] itemDescriptions = getResources().getStringArray(R.array.shopping_item_descriptions);
        for (int i = 0; i < itemNames.length; i++){
            shoppingListModels.add(new ShoppingListModel(itemNames[i], ShoppingListImages[i], 0, itemDescriptions[i]));
        }
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(MainActivity.this, ActivityDetail.class);

        intent.putExtra("NAME", shoppingListModels.get(position).getName());
        intent.putExtra("IMAGE", shoppingListModels.get(position).getImage());
        intent.putExtra("DESCRIPTION", shoppingListModels.get(position).getDescription());

        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        isControlTypeButtons = view.getId() == R.id.switch_button;
        adapter.switchControls(isControlTypeButtons);
    }
}