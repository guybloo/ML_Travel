package com.example.ml_travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.ferortega.cf4j.data.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstRating extends AppCompatActivity {

    private ArrayList<Item> list;
    HashMap<Integer, Trip> items_idx;
    HashMap<Integer, Integer> ratings;
    private RatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_rating);
        list = (ArrayList<Item>) getIntent().getSerializableExtra("list");
        items_idx = (HashMap<Integer, Trip>) getIntent().getSerializableExtra("items_idx");
        recyclerViewConfig();

        Button submit = findViewById(R.id.first_rating_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ratings = adapter.getRatings();
                Intent intent = new Intent();
                intent.putExtra("ratings", ratings);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void recyclerViewConfig() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RatingAdapter(this, list, items_idx);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

    }
}
