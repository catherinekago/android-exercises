package com.example.myshoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<ShoppingListModel> shoppingListModels = new ArrayList<>();

    int[]ShoppingListImages = {R.drawable.avocado, R.drawable.bread,R.drawable.oil, R.drawable.tomato};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.shoppingListRecyclerView);

        setUpShoppingListModels();

        // Setup adapter AFTER models because models have to be passed TO the adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, shoppingListModels, this);

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
}