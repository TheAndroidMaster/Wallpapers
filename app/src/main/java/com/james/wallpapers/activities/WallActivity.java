package com.james.wallpapers.activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.Utils;
import com.james.wallpapers.adapters.ListAdapter;
import com.james.wallpapers.adapters.SearchAdapter;
import com.james.wallpapers.data.WallData;

import java.io.IOException;


public class WallActivity extends AppCompatActivity {

    WallData data;
    BroadcastReceiver downloadReceiver;

    Toolbar toolbar;
    ImageView imageee;
    TextView wall, auth;
    LinearLayout bg;
    FloatingActionButton fab;
    View download, share, apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        data = getIntent().getParcelableExtra("wall");
        setTitle(data.name);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageee = (ImageView) findViewById(R.id.imageee);
        wall = (TextView) findViewById(R.id.wall);
        auth = (TextView) findViewById(R.id.auth);
        bg = (LinearLayout) findViewById(R.id.back);
        download = findViewById(R.id.download);
        share = findViewById(R.id.share);
        apply = findViewById(R.id.apply);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        byte[] b = getIntent().getByteArrayExtra("preload");
        if (b != null) imageee.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));

        wall.setText(getTitle());
        auth.setText(data.authorName);

        if(data.favorite) fab.setImageResource(R.drawable.fav_added);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Glide.with(this).load(data.url).into(new SimpleTarget<GlideDrawable>(metrics.widthPixels, metrics.heightPixels) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageee.setImageDrawable(resource);

                Palette.from(Utils.drawableToBitmap(resource)).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(Color.DKGRAY)));
                        bg.setBackgroundColor(palette.getVibrantColor(Color.DKGRAY));
                    }
                });

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setImageResource(data.favorite ? R.drawable.fav_add : R.drawable.fav_added);
                data.setFavorite(WallActivity.this, !data.favorite);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.credit) {
                    Supplier.getCreditDialog(WallActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Supplier.downloadWallpaper(WallActivity.this, data);
                            dialog.dismiss();
                        }
                    }).show();
                }
                else Supplier.downloadWallpaper(WallActivity.this, data);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.credit) {
                    Supplier.getCreditDialog(WallActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Supplier.shareWallpaper(WallActivity.this, data);
                            dialog.dismiss();
                        }
                    }).show();
                }
                else Supplier.shareWallpaper(WallActivity.this, data);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WallActivity.this).load(data.url).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        try {
                            WallpaperManager.getInstance(WallActivity.this).setBitmap(Utils.drawableToBitmap(resource));
                            Toast.makeText(WallActivity.this, R.string.set_wallpaper_success, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(WallActivity.this, R.string.set_wallpaper_failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Toast.makeText(WallActivity.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Supplier.getDownloadedDialog(WallActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WallActivity.this.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    }
                }).show();
                unregisterReceiver(this);
            }
        };
    }
}
