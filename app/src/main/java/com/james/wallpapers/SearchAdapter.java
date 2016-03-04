package com.james.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Activity activity;
    Context context;
    ArrayList<String> fTitles, fSubtitles;
    ArrayList<Integer> wInt, wPerson, pInt;
    String[] peopleNames, peopleUrls;
    TypedArray tab_names, tab_urls, resourceNums;

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
        context = activity.getApplicationContext();

        fTitles = new ArrayList<>();
        fSubtitles = new ArrayList<>();
        wInt = new ArrayList<>();
        wPerson = new ArrayList<>();
        pInt = new ArrayList<>();

        peopleNames = context.getResources().getStringArray(R.array.people_names);
        peopleUrls = context.getResources().getStringArray(R.array.people_urls);

        tab_names = context.getResources().obtainTypedArray(R.array.wp_names);
        tab_urls = context.getResources().obtainTypedArray(R.array.wp_urls);
        resourceNums = context.getResources().obtainTypedArray(R.array.wp_names);
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(v, v.findViewById(R.id.title), v.findViewById(R.id.subtitle), v.findViewById(R.id.root), v.findViewById(R.id.card));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ((TextView)holder.title).setText(fTitles.get(position).replace("*", ""));
        ((TextView)holder.subtitle).setText(fSubtitles.get(position));

        if (fSubtitles.get(position).matches("Designer")) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.orange)) ;
            ((TextView)holder.title).setTextColor(Color.parseColor("#fafafa"));
            ((TextView)holder.subtitle).setTextColor(Color.parseColor("#e0e0e0"));
        } else {
            ((TextView)holder.title).setTextColor(Color.parseColor("#212121"));
            ((TextView)holder.subtitle).setTextColor(Color.parseColor("#616161"));
        }

        holder.clicker.setTag(position);
        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (fSubtitles.get((int) v.getTag()).matches("Designer")) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(peopleUrls[pInt.get((int) v.getTag())]));
                } else {
                    intent = new Intent(activity, WallpaperView.class);
                    intent.putExtra("num", wInt.get((int) v.getTag()));
                    intent.putExtra("array", tab_names.getResourceId(wPerson.get((int) v.getTag()), -1));
                    intent.putExtra("arrays", tab_urls.getResourceId(wPerson.get((int) v.getTag()), -1));
                    intent.putExtra("auth", fSubtitles.get((int) v.getTag()));
                    intent.putExtra("up", "Search");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fTitles.size();
    }

    public void filter(String filter) {
        fTitles = new ArrayList<>();
        fSubtitles = new ArrayList<>();
        wInt = new ArrayList<>();
        wPerson = new ArrayList<>();
        pInt = new ArrayList<>();

        for (int e = 0; e < resourceNums.length(); e++) {
            String[] walls = context.getResources().getStringArray(resourceNums.getResourceId(e, -1));
            for (int i = 0; i < walls.length; i++) {
                String[] filters = filter.split(" ");
                for (String filt : filters) {
                    filt = filt.replace(" ", "");
                    if (walls[i].toLowerCase().contains(filt.toLowerCase()) || filt.toLowerCase().contains(walls[i].toLowerCase())) {
                        fTitles.add(walls[i]);
                        fSubtitles.add(peopleNames[e]);
                        wInt.add(i);
                        wPerson.add(e);
                    }
                }
            }
        }
        for (int i = 0; i < peopleNames.length; i++) {
            if (peopleNames[i].toLowerCase().contains(filter.toLowerCase()) || filter.toLowerCase().contains(peopleNames[i].toLowerCase())) {
                fTitles.add(peopleNames[i]);
                fSubtitles.add("Designer");
                pInt.add(i);
            }
        }
        notifyDataSetChanged();
    }
}
