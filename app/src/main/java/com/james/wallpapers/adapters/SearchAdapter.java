package com.james.wallpapers.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.wallpapers.activities.MainActivity;
import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.activities.WallActivity;
import com.james.wallpapers.data.AuthorData;
import com.james.wallpapers.data.WallData;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Activity activity;
    ArrayList<WallData> totalWalls, walls;
    ArrayList<AuthorData> totalAuthors, authors;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v, title, subtitle, clicker, card;
        public ViewHolder(View v, View title, View subtitle, View clicker, View card) {
            super(v);
            this.v = v;
            this.title = title;
            this.subtitle = subtitle;
            this.clicker = clicker;
            this.card = card;
        }
    }

    public SearchAdapter(Activity activity) {
        this.activity = activity;
        totalWalls = Supplier.getWallpapers(activity);
        totalAuthors = Supplier.getAuthors(activity);
        walls = new ArrayList<>();
        authors = new ArrayList<>();
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(v, v.findViewById(R.id.title), v.findViewById(R.id.subtitle), v.findViewById(R.id.root), v.findViewById(R.id.card));
    }

    @Override
    public int getItemViewType(int position) {
        if (position < walls.size()) return 0;
        else return 1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TextView title = (TextView) holder.title, subtitle = (TextView) holder.subtitle;

        if (getItemViewType(position) == 0) {
            holder.card.setBackgroundColor(Color.TRANSPARENT);
            title.setText(walls.get(position).name);
            subtitle.setText(walls.get(position).authorName);
            title.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimaryInverse));
            subtitle.setTextColor(ContextCompat.getColor(activity, R.color.textColorSecondaryInverse));
        } else {
            holder.card.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent));
            title.setText(authors.get(position - walls.size()).name);
            subtitle.setText("Designer");
            title.setTextColor(ContextCompat.getColor(activity, R.color.textColorPrimary));
            subtitle.setTextColor(ContextCompat.getColor(activity, R.color.textColorSecondary));
        }

        holder.clicker.setTag(position);
        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemViewType(holder.getAdapterPosition()) == 0) {
                    Intent i = new Intent(activity, WallActivity.class);
                    i.putExtra("wall", walls.get(holder.getAdapterPosition()));
                    activity.startActivity(i);
                } else {
                    Intent i = new Intent(activity, MainActivity.class);
                    i.putExtra("person", authors.get(holder.getAdapterPosition() - walls.size()));
                    activity.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return walls.size() + authors.size();
    }

    public void filter(String filter) {
        walls.clear();
        authors.clear();

        for (WallData data : totalWalls) {
            if (data.name.contains(filter) || filter.contains(data.name)) walls.add(data);
        }

        for (AuthorData data : totalAuthors) {
            if (data.name.contains(filter) || filter.contains(data.name)) authors.add(data);
        }

        notifyDataSetChanged();
    }
}
