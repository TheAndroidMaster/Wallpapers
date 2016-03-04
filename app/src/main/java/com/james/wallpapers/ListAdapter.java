package com.james.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private String[] names, urls;
    int array, arrays, orientation;
    String tabname;
    Context context;
    Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public View imagel;
        public Thread thread;
        public int loaded;
        public ViewHolder(View v, View imagel) {
            super(v);
            this.v = v;
            this.imagel = imagel;
        }
    }
    public ListAdapter(int names, int urls, Activity activity, final String tabname, int orientation) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.names = context.getResources().getStringArray(names);
        this.urls = context.getResources().getStringArray(urls);
        this.tabname = tabname;
        array = names;
        arrays = urls;

        this.orientation = orientation;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v, v.findViewById(R.id.image));
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder viewHolder, int position) {
        if (viewHolder.thread != null && !viewHolder.thread.isInterrupted()) {
            viewHolder.thread.interrupt();
        }

        final SquareImageView image = (SquareImageView) viewHolder.imagel;
        image.setOrientation(orientation);

        TextView title = (TextView) viewHolder.v.findViewById(R.id.title);

        image.setImageBitmap(null);

        String titlestring = names[position];
        titlestring = titlestring.replace("*", "");
        title.setText(titlestring);

        viewHolder.v.findViewById(R.id.card).setTag(position);
        viewHolder.v.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WallpaperView.class);
                intent.putExtra("num", (int) v.getTag());
                intent.putExtra("array", array);
                intent.putExtra("arrays", arrays);
                intent.putExtra("auth", tabname);
                intent.putExtra("up", "Flat");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (image.getDrawable() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ((BitmapDrawable) image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    intent.putExtra("preload", b);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, ((BitmapDrawable) image.getDrawable()).getBitmap(), 5, 5);
                    ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.thread = new Thread() {
            public void run() {
                final Drawable d = Cache.getCompressedDrawable(tabname.toLowerCase().replace(" ", "_"), ListAdapter.this.urls[viewHolder.getAdapterPosition()].replace("/", "").replace(":", "").replace(".", ""), ListAdapter.this.urls[viewHolder.getAdapterPosition()], context);
                ListAdapter.this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(context.getResources().getColor(R.color.orange)), d});
                        image.setImageDrawable(td);
                        viewHolder.loaded = 0;
                        td.startTransition(200);
                    }
                });
            }
        };
        viewHolder.thread.start();
    }

    @Override
    public int getItemCount() {
        return names.length;
    }
}
