package com.james.wallpapers.adapters;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.james.wallpapers.R;
import com.james.wallpapers.Utils;
import com.james.wallpapers.views.CustomImageView;
import com.james.wallpapers.views.SquareImageView;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.activities.WallActivity;
import com.james.wallpapers.data.WallData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<WallData> walls;
    private Activity activity;
    private int layoutMode = 0;
    public final static int LAYOUT_MODE_HORIZONTAL = 1, LAYOUT_MODE_COMPLEX = 2;

    private Supplier supplier;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public View imagel;
        public ViewHolder(View v, View imagel) {
            super(v);
            this.v = v;
            this.imagel = imagel;
        }
    }

    public ListAdapter(Activity activity, ArrayList<WallData> walls) {
        this.activity = activity;
        this.walls = walls;
        supplier = (Supplier) activity.getApplicationContext();
    }

    public void setLayoutMode(int mode) {
        layoutMode = mode;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutMode == LAYOUT_MODE_COMPLEX ? R.layout.layout_item_complex : R.layout.layout_item, parent, false);
        return new ViewHolder(v, v.findViewById(R.id.image));
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, int position) {
        final CustomImageView image = (CustomImageView) holder.imagel;
        image.setImageBitmap(null);

        ((TextView) holder.v.findViewById(R.id.title)).setText(walls.get(position).name);

        holder.v.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WallActivity.class);
                intent.putExtra("wall", walls.get(holder.getAdapterPosition()));
                intent.putExtra("up", "Flat");

                if (image.getDrawable() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Drawable prev = image.getDrawable();
                    if (prev instanceof TransitionDrawable) prev = ((TransitionDrawable) image.getDrawable()).getDrawable(1);
                    Bitmap bitmap;
                    try {
                        bitmap = Utils.drawableToBitmap(prev);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    } catch (Exception e) {
                        e.printStackTrace();
                        activity.startActivity(intent);
                        return;
                    }
                    byte[] b = baos.toByteArray();
                    intent.putExtra("preload", b);

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, bitmap, 5, 5);
                    ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                    activity.startActivity(intent, options.toBundle());
                } else {
                    activity.startActivity(intent);
                }
            }
        });

        if (layoutMode == LAYOUT_MODE_HORIZONTAL && image instanceof SquareImageView) ((SquareImageView) image).setOrientation(SquareImageView.HORIZONTAL);

        if (layoutMode == LAYOUT_MODE_COMPLEX) {
            ((TextView) holder.v.findViewById(R.id.author)).setText(walls.get(position).authorName);

            holder.v.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (walls.get(holder.getAdapterPosition()).credit) {
                        supplier.getCreditDialog(activity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                supplier.downloadWallpaper(activity, walls.get(holder.getAdapterPosition()));
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    else supplier.shareWallpaper(activity, walls.get(holder.getAdapterPosition()));
                }
            });

            CustomImageView fav = (CustomImageView) holder.v.findViewById(R.id.fav);
            if (walls.get(position).favorite) fav.setImageResource(R.drawable.fav_added);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ImageView) v).setImageResource(walls.get(holder.getAdapterPosition()).favorite ? R.drawable.fav_add : R.drawable.fav_added);
                    walls.get(holder.getAdapterPosition()).setFavorite(activity, !walls.get(holder.getAdapterPosition()).favorite);
                }
            });

            holder.v.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(activity).load(walls.get(holder.getAdapterPosition())).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            try {
                                WallpaperManager.getInstance(activity).setBitmap(Utils.drawableToBitmap(resource));
                                Toast.makeText(activity, R.string.set_wallpaper_success, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, R.string.set_wallpaper_failed, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Toast.makeText(activity, R.string.download_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        Glide.with(activity).load(walls.get(holder.getAdapterPosition()).url).into(image);
    }

    @Override
    public int getItemCount() {
        return walls.size();
    }
}
