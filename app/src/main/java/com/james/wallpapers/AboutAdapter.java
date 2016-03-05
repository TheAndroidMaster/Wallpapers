package com.james.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

    Activity activity;
    ArrayList<Parcelable> itemList;

    public AboutAdapter(Activity activity, ArrayList<Parcelable> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof HeaderListData) return 0;
        else if (itemList.get(position) instanceof TextListData) return 1;
        else return 2;
    }

    @Override
    public AboutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case 0:
                return new ViewHolder(inflater.inflate(R.layout.layout_header, null)) ;
            case 1:
                return new ViewHolder(inflater.inflate(R.layout.layout_text, null));
            case 2:
                return new ViewHolder(inflater.inflate(R.layout.layout_person, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(AboutAdapter.ViewHolder holder, int position) {
        holder.v.setAlpha(0.0f);

        if (getItemViewType(position) == 0) {
            HeaderListData data = (HeaderListData) itemList.get(position);

            if (data.name != null) {
                TextView header = (TextView) holder.v.findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
                header.setText(data.name);
                if (data.centered) header.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            else holder.v.findViewById(R.id.header).setVisibility(View.GONE);

            if (data.content != null) {
                TextView content = (TextView) holder.v.findViewById(R.id.content);
                content.setVisibility(View.VISIBLE);
                content.setText(data.content);
                if (data.centered) content.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            else holder.v.findViewById(R.id.content).setVisibility(View.GONE);

            if (data.primary != null) {
                holder.v.setTag(position);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(((HeaderListData) itemList.get((int) v.getTag())).primary);
                    }
                });
            }
        } else if (getItemViewType(position) == 1) {
            TextListData data = (TextListData) itemList.get(position);

            ((TextView) holder.v.findViewById(R.id.header)).setText(data.name);
            ((TextView) holder.v.findViewById(R.id.content)).setText(data.content);

            holder.v.findViewById(R.id.card).setTag(position);
            holder.v.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(((TextListData) itemList.get((int) v.getTag())).primary);
                }
            });
        } else {
            PersonListData data = (PersonListData) itemList.get(position);

            ((ImageView) holder.v.findViewById(R.id.profile)).setImageResource(data.drawableRes);
            ((TextView) holder.v.findViewById(R.id.header)).setText(data.name);
            ((TextView) holder.v.findViewById(R.id.content)).setText(data.content);

            holder.v.findViewById(R.id.card).setTag(position);
            holder.v.findViewById(R.id.card).findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(((PersonListData) itemList.get((int) v.getTag())).primary);
                }
            });
        }

        holder.v.animate().alpha(1.0f).setDuration(250).start();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
