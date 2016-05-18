package com.james.wallpapers.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.james.wallpapers.R;
import com.james.wallpapers.data.HeaderListData;
import com.james.wallpapers.data.PersonListData;
import com.james.wallpapers.data.TextListData;

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
        switch(getItemViewType(position)) {
            case 0:
                HeaderListData headerData = (HeaderListData) itemList.get(position);

                if (headerData.name != null) {
                    TextView header = (TextView) holder.v.findViewById(R.id.header);
                    header.setVisibility(View.VISIBLE);
                    header.setText(headerData.name);
                    if (headerData.centered) header.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                else holder.v.findViewById(R.id.header).setVisibility(View.GONE);

                if (headerData.content != null) {
                    TextView content = (TextView) holder.v.findViewById(R.id.content);
                    content.setVisibility(View.VISIBLE);
                    content.setText(headerData.content);
                    if (headerData.centered) content.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                else holder.v.findViewById(R.id.content).setVisibility(View.GONE);

                if (headerData.url != null) {
                    holder.v.setTag(headerData);
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(((HeaderListData) v.getTag()).url)));
                        }
                    });
                }
                break;
            case 1:
                TextListData textData = (TextListData) itemList.get(position);

                ((TextView) holder.v.findViewById(R.id.header)).setText(textData.name);
                ((TextView) holder.v.findViewById(R.id.content)).setText(textData.content);

                View card = holder.v.findViewById(R.id.card);
                card.setTag(textData);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(((TextListData) v.getTag()).primary);
                    }
                });
                break;
            case 2:
                PersonListData personData = (PersonListData) itemList.get(position);

                Glide.with(activity).load(personData.icon).into((ImageView) holder.v.findViewById(R.id.profile));
                ((TextView) holder.v.findViewById(R.id.header)).setText(personData.name);
                ((TextView) holder.v.findViewById(R.id.content)).setText(personData.content);

                View button = holder.v.findViewById(R.id.button);
                button.setTag(personData);
                button.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(((PersonListData) v.getTag()).url)));
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
