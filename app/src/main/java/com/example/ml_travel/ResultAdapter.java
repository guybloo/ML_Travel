package com.example.ml_travel;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ferortega.cf4j.data.Item;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<Trip> items;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    ResultAdapter(Context context, ArrayList<Trip> it) {
        setHasStableIds(true);
        this.inflater = LayoutInflater.from(context);
        this.items = it;
    }

    public void setItems(ArrayList<Trip> it)
    {
        items = it;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Trip trip = items.get(position);
        ((TextView) holder.itemView.findViewById(R.id.result_name)).setText(trip.name);
        ((TextView) holder.itemView.findViewById(R.id.result_area)).setText(trip.area);
        ((TextView) holder.itemView.findViewById(R.id.result_place)).setText(trip.place);
        String more = "";
        switch (trip.level) {
            case 0:
                more += "קל | ";
                break;
            case 1:
                more += "בינוני | ";
                break;
            case 2:
                more += "מאתגר | ";
                break;
        }

        switch (trip.time) {
            case 0:
                more += "פחות מיום | ";
                break;
            case 1:
                more += "יום | ";
                break;
            case 2:
                more += "יותר מיום | ";
                break;
        }
        more += String.valueOf(trip.length) + " קמ";

        ((TextView) holder.itemView.findViewById(R.id.result_more_info)).setText(more);
        holder.itemView.findViewById(R.id.r_accessible).setVisibility(boolToVisible(trip.accessible));
        holder.itemView.findViewById(R.id.r_bicycle).setVisibility(boolToVisible(trip.bicycle));
        holder.itemView.findViewById(R.id.r_bigbag).setVisibility(boolToVisible(trip.bigBags));
        holder.itemView.findViewById(R.id.r_circle).setVisibility(boolToVisible(trip.circle));
        holder.itemView.findViewById(R.id.r_city).setVisibility(boolToVisible(trip.city));
        holder.itemView.findViewById(R.id.r_dogs).setVisibility(boolToVisible(trip.dogs));
        holder.itemView.findViewById(R.id.r_families).setVisibility(boolToVisible(trip.families));
        holder.itemView.findViewById(R.id.r_flower).setVisibility(boolToVisible(trip.flowers));
        holder.itemView.findViewById(R.id.r_heart).setVisibility(boolToVisible(trip.romantic));
        holder.itemView.findViewById(R.id.r_nature).setVisibility(boolToVisible(trip.nature));
        holder.itemView.findViewById(R.id.r_public_transport).setVisibility(boolToVisible(trip.publicTransport));
        holder.itemView.findViewById(R.id.r_walk).setVisibility(boolToVisible(trip.goodWalkers));
        holder.itemView.findViewById(R.id.r_water).setVisibility(boolToVisible(trip.water));
        holder.itemView.findViewById(R.id.result_info).setVisibility(View.GONE);


    }

    private int boolToVisible(boolean b) {
        if (b) {
            return View.VISIBLE;
        }
        return View.GONE;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        TextView myTextView;
        public View view;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
//            myTextView = itemView.findViewById(R.id.result_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
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

}