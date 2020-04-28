package com.example.ml_travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ferortega.cf4j.data.Item;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private ArrayList<Item> items;
    private HashMap<Integer, Trip> items_idx;
    private HashMap<Integer, Integer> ratings;
    private LayoutInflater inflater;

    RatingAdapter(Context context, ArrayList<Item> data, HashMap<Integer, Trip> it_idx) {
        setHasStableIds(true);
        this.inflater = LayoutInflater.from(context);
        this.items = data;
        this.items_idx = it_idx;
        ratings = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_rating_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.myTextView.setText(items_idx.get(Integer.parseInt(item.getId())).name);
        holder.bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratings.put(Integer.parseInt(item.getId()),(int)v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        RatingBar bar;


        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.rating_title);
            bar = itemView.findViewById(R.id.rating_bar);
            //            itemView.setOnClickListener(this);
        }



    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public HashMap<Integer, Integer> getRatings()
    {
        return ratings;
    }

}