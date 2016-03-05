package com.james.wallpapers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fav extends AppCompatActivity {

    int counter, key;
    boolean isOdd, isFav;
    String[] ppl;
    List<Integer> names, urls, nums;
    List<String> auths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Fav.this, SearchActivity.class);
                i.putExtra("up", "Fav");
                startActivity(i);
            }
        });

        names = new ArrayList<>();
        urls = new ArrayList<>();
        nums = new ArrayList<>();
        auths = new ArrayList<>();

        ppl = getResources().getStringArray(R.array.people_names);

        isOdd = true;
        key = 0;

        final TypedArray tab_names = getResources().obtainTypedArray(R.array.wp_names);
        final TypedArray tab_urls = getResources().obtainTypedArray(R.array.wp_urls);

        for(counter = 0; counter < tab_names.length(); counter++){
            refresh(tab_names.getResourceId(counter, -1), tab_urls.getResourceId(counter, -1), ppl[counter]);
        }

        if(names.size() > 0){
            findViewById(R.id.imageView2).setVisibility(View.GONE);
            findViewById(R.id.textView4).setVisibility(View.GONE);
        }

        tab_names.recycle();
        tab_urls.recycle();

        new Drawer(this).initDrawer(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    private Intent getParentActivityIntentImpl() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return startMain;
    }

    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = Fav.this.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }

    public void refresh(int nameid, int urlid, final String folder) {
        SharedPreferences prefs = getSharedPreferences("com.james.wallpapers", 0);
        final String[] name = getResources().getStringArray(nameid);
        final String[] url = getResources().getStringArray(urlid);
        LinearLayout root = (LinearLayout) findViewById(R.id.rootView);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView1);
        for(int count = 0; count < name.length; count++){
            isFav = prefs.getBoolean(url[count], false);
            if(isFav){
                final int i = count;

                View v = getLayoutInflater().inflate(R.layout.layout_item, null, false);

                final FrameLayout cardView = (FrameLayout) v.findViewById(R.id.card);
                cardView.setId(key);

                String mName = name[count];
                mName = mName.replace("*", "");

                final TextView title = (TextView) v.findViewById(R.id.title);
                title.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                title.setText(mName);

                final SquareImageView image = (SquareImageView) v.findViewById(R.id.image);

                new Thread() {
                    public void run() {
                        final Drawable drawable;
                        if (Cache.dir(folder.toLowerCase().replace(" ", "_"), Fav.this)) drawable = Cache.getCompressedDrawable(folder.toLowerCase().replace(" ", "_"), url[i].replace("/", "").replace(":", "").replace(".", ""), url[i], Fav.this);
                        else drawable = Cache.downloadCompressedDrawable(url[i], Fav.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                                int color = getDominantColor(bmp);
                                title.setBackgroundColor(color);
                                image.setBackgroundColor(color);
                                TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(getResources().getColor(R.color.colorAccent)), new BitmapDrawable(bmp)});
                                image.setImageDrawable(td);
                                td.startTransition(200);
                            }
                        });
                    }
                }.start();

                /*Target target = new Target(){
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int color = getDominantColor(bitmap);
                        title.setBackgroundColor(color);
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(getResources().getColor(R.color.oranged)), new BitmapDrawable(bitmap)});
                        image.setImageDrawable(td);
                        td.startTransition(200);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(getResources().getColor(R.color.oranged)), errorDrawable});
                        image.setImageDrawable(td);
                        td.startTransition(200);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(getResources().getColor(R.color.oranged)), placeHolderDrawable});
                        image.setImageDrawable(td);
                        td.startTransition(200);
                    }
                };



                image.setTag(target);

                Picasso.with(this)
                        .load(url[count])
                        .resize(300, 300)
                        .centerCrop()
                        .placeholder(R.mipmap.downloading)
                        .error(R.mipmap.wifioff)
                        .into(target);*/

                names.add(key, nameid);
                urls.add(key, urlid);
                nums.add(key, count);
                auths.add(key, folder);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Fav.this, WallpaperView.class);
                        intent.putExtra("num", nums.get(v.getId()));
                        intent.putExtra("array", names.get(v.getId()));
                        intent.putExtra("arrays", urls.get(v.getId()));
                        intent.putExtra("auth", auths.get(v.getId()));
                        intent.putExtra("up", "Fav");
                        Fav.this.startActivity(intent);
                    }
                });
                if(isOdd){
                    root.addView(v);
                    isOdd=false;
                }else{
                    rootView.addView(v);
                    isOdd=true;
                }
                key = key + 1;
            }
        }
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.green
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }
}
