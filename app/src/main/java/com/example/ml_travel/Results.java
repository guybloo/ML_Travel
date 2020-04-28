package com.example.ml_travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ferortega.cf4j.data.Item;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Results extends AppCompatActivity implements ResultAdapter.ItemClickListener{

    private HashMap<Integer, Trip> items;
    private ResultAdapter adapter;
    private ArrayList<Trip> all;
    private ArrayList<Trip> forNow;
    private Trip search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        items = (HashMap<Integer, Trip>) getIntent().getSerializableExtra("items");

        ArrayList<Trip> results = new ArrayList<>();
        for(Rate re : (ArrayList<Rate>) getIntent().getSerializableExtra("all"))
        {
            results.add(items.get(re.item));
        }
        all = results;

        results = new ArrayList<>();
        for(Rate re : (ArrayList<Rate>) getIntent().getSerializableExtra("for_now"))
        {
            results.add(items.get(re.item));
        }
        forNow = results;
        search = new Trip();

        recyclerViewConfig();
        findViewById(R.id.result_now).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setBtnColor(R.id.result_now);
                adapter.setItems(forNow);
            }
        });

        findViewById(R.id.result_all).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                setBtnColor(R.id.result_all);
                adapter.setItems(all);
            }
        });

        findViewById(R.id.result_search).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setBtnColor(R.id.result_search);
                View search_box =  findViewById(R.id.check_search);
                if(search_box.getVisibility() == View.GONE)
                    search_box.setVisibility(View.VISIBLE);
                else
                    search_box.setVisibility(View.GONE);
            }
        });



        findViewById(R.id.btn_search_do).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSearch();
                findViewById(R.id.check_search).setVisibility(View.GONE);
            }
        });

        setIconsClicks();
    }

    private void setBtnColor(int id)
    {
        findViewById(R.id.check_search).setVisibility(View.GONE);
        ((Button)findViewById(R.id.result_search)).setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
        ((Button)findViewById(R.id.result_all)).setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
        ((Button)findViewById(R.id.result_now)).setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));

        ((Button)findViewById(id)).setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorAccent));

    }
    private void recyclerViewConfig() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ResultAdapter(this, all);
        adapter.setClickListener(this);

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        int v = view.findViewById(R.id.result_info).getVisibility();
        if(v == View.VISIBLE)
            view.findViewById(R.id.result_info).setVisibility(View.GONE);
        else
            view.findViewById(R.id.result_info).setVisibility(View.VISIBLE);
    }


    private void startSearch() {
        if (all == null)
            return;

        ArrayList<Trip> results = new ArrayList<>();
        for (Trip trip : all) {
            boolean add = true;
            if (search.bicycle) {
                if (!trip.bicycle) add = false;
            }
            if (search.accessible) {
                if (!trip.accessible) add = false;
            }
            if (search.bigBags) {
                if (!trip.bigBags) add = false;
            }
            if (search.circle) {
                if (!trip.circle) add = false;
            }
            if (search.city) {
                if (!trip.city) add = false;
            }
            if (search.dogs) {
                if (!trip.dogs) add = false;
            }
            if (search.families) {
                if (!trip.families) add = false;
            }
            if (search.romantic) {
                if (!trip.romantic) add = false;
            }
            if (search.nature) {
                if (!trip.nature) add = false;
            }
            if (search.publicTransport) {
                if (!trip.publicTransport) add = false;
            }
            if (search.goodWalkers) {
                if (!trip.goodWalkers) add = false;
            }
            if (search.water) {
                if (!trip.water) add = false;
            }
            if (search.flowers) {
                if (!trip.flowers) add = false;
            }
            if (search.summer) {
                if (!trip.summer) add = false;
            }
            if (search.winter) {
                if (!trip.winter) add = false;
            }
            if (search.fall) {
                if (!trip.fall) add = false;
            }
            if (search.spring) {
                if (!trip.spring) add = false;
            }

            if (add)
                results.add(trip);
        }
        adapter.setItems(results);
    }


    private void setIconsClicks() {
        findViewById(R.id.r_water).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.water = !search.water;
                if (search.water)
                    ((MaterialIconView) findViewById(R.id.r_water)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_water)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_walk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.goodWalkers = !search.goodWalkers;
                if (search.goodWalkers)
                    ((MaterialIconView) findViewById(R.id.r_walk)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_walk)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_public_transport).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.publicTransport = !search.publicTransport;
                if (search.publicTransport)
                    ((MaterialIconView) findViewById(R.id.r_public_transport)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_public_transport)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });

        findViewById(R.id.r_nature).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.nature = !search.nature;
                if (search.nature)
                    ((MaterialIconView) findViewById(R.id.r_nature)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_nature)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_heart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.romantic = !search.romantic;
                if (search.romantic)
                    ((MaterialIconView) findViewById(R.id.r_heart)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_heart)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_flower).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.flowers = !search.flowers;
                if (search.flowers)
                    ((MaterialIconView) findViewById(R.id.r_flower)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_flower)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_families).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.families = !search.families;
                if (search.families)
                    ((MaterialIconView) findViewById(R.id.r_families)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_families)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_dogs).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.dogs = !search.dogs;
                if (search.dogs)
                    ((MaterialIconView) findViewById(R.id.r_dogs)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_dogs)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_city).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.city = !search.city;
                if (search.city)
                    ((MaterialIconView) findViewById(R.id.r_city)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_city)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_circle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.circle = !search.circle;
                if (search.circle)
                    ((MaterialIconView) findViewById(R.id.r_circle)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_circle)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_bigbag).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.bigBags = !search.bigBags;
                if (search.bigBags)
                    ((MaterialIconView) findViewById(R.id.r_bigbag)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_bigbag)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_bicycle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.bicycle = !search.bicycle;
                if (search.bicycle)
                    ((MaterialIconView) findViewById(R.id.r_bicycle)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_bicycle)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_accessible).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.accessible = !search.accessible;
                if (search.accessible)
                    ((MaterialIconView) findViewById(R.id.r_accessible)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_accessible)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });

        findViewById(R.id.r_summer).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.summer = !search.summer;
                if (search.summer)
                    ((MaterialIconView) findViewById(R.id.r_summer)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_summer)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_winter).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.winter = !search.winter;
                if (search.winter)
                    ((MaterialIconView) findViewById(R.id.r_winter)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_winter)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_fall).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.fall = !search.fall;
                if (search.fall)
                    ((MaterialIconView) findViewById(R.id.r_fall)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_fall)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
        findViewById(R.id.r_spring).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.spring = !search.spring;
                if (search.spring)
                    ((MaterialIconView) findViewById(R.id.r_spring)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
                else
                    ((MaterialIconView) findViewById(R.id.r_spring)).setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            }
        });
    }

}
