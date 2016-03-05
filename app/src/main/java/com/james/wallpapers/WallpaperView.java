package com.james.wallpapers;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


public class WallpaperView extends ActionBarActivity {

    String path, title, author, up;
    int num, position;
    boolean fav;

    BroadcastReceiver complete;

    Toolbar toolbar;
    ImageView imageee;
    TextView wall, auth;
    LinearLayout bg;

    Drawable transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_view);

        final android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_wallpaper_view);

        imageee = (ImageView) findViewById(R.id.imageee);
        wall = (TextView) findViewById(R.id.wall);
        auth = (TextView) findViewById(R.id.auth);
        bg = (LinearLayout) findViewById(R.id.back);

        transition = imageee.getDrawable();

        byte[] b = getIntent().getByteArrayExtra("preload");
        if (b != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageee.setImageBitmap(bmp);
            transition = new BitmapDrawable(getResources(), bmp);
        }

        imageee.setImageDrawable(transition);

        position = 0;

        final SharedPreferences prefs = getSharedPreferences("com.james.wallpapers", 0);

        final String[] arrayname = getResources().getStringArray(getIntent().getIntExtra("array", 0));
        final String[] arrayurl = getResources().getStringArray(getIntent().getIntExtra("arrays", 0));

        int nameid = getIntent().getIntExtra("array", 0);

        num = getIntent().getIntExtra("num", 0) ;
        position = num;
        author = getIntent().getStringExtra("auth");
        up = getIntent().getStringExtra("up");
        title = arrayname[num];
        path = arrayurl[num];

        String mName = title.replace("*", "");
        getSupportActionBar().setTitle(mName);

        wall.setText(mName);
        auth.setText(author);

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        fav = prefs.getBoolean(path, false);
        if(fav){
            fab.setImageResource(R.drawable.fav_added);
        }

        new Thread() {
            @Override
            public void run() {
                final BitmapDrawable bmp;
                if (Cache.dir(author.toLowerCase().replace(" ", "_"), WallpaperView.this)) bmp = (BitmapDrawable) Cache.getDrawable(author.toLowerCase().replace(" ", "_"), path.replace("/", "").replace(":", "").replace(".", ""), path, WallpaperView.this);
                else bmp = (BitmapDrawable) Cache.downloadDrawable(path, WallpaperView.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{transition, bmp});
                        imageee.setImageDrawable(td);
                        td.startTransition(200);
                        findViewById(R.id.progressBar).setVisibility(View.GONE);

                        Palette.from(bmp.getBitmap()).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                toolbar.setBackgroundColor(palette.getVibrantColor(Color.DKGRAY));
                                ((FloatingActionButton) findViewById(R.id.fab)).setBackgroundTintList(ColorStateList.valueOf(palette.getMutedColor(Color.GRAY)));
                                bg.setBackgroundColor(palette.getMutedColor(Color.GRAY));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(darkColor(palette.getVibrantColor(Color.DKGRAY)));
                                };
                            }
                        });

                        if(fav){
                            fab.setImageResource(R.drawable.fav_added);
                        }else{
                            fab.setImageResource(R.drawable.fav_add);
                        }
                    }
                });
            }
        }.start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav) {
                    fab.setImageResource(R.drawable.fav_add);
                    prefs.edit().putBoolean(path, false).apply();
                    fav = false;
                } else {
                    fab.setImageResource(R.drawable.fav_added);
                    prefs.edit().putBoolean(path, true).apply();
                    fav = true;
                }
            }
        });

        final TypedArray tab_names = getResources().obtainTypedArray(R.array.wp_names);
        final TypedArray tab_urls = getResources().obtainTypedArray(R.array.wp_urls);

        int authnum = -1;
        for(int i = 0; i < tab_names.length(); i++) {
            if (tab_names.getResourceId(i, -1) == nameid) {
                authnum = i;
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.more);
        GridLayoutManager grid = new GridLayoutManager(this, 1);
        grid.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(new ListAdapter(tab_names.getResourceId(authnum, -1), tab_urls.getResourceId(authnum, -1), WallpaperView.this, author, SquareImageView.HORIZONTAL));
        recyclerView.setHasFixedSize(true);

        tab_names.recycle();
        tab_urls.recycle();

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.similar);
        GridLayoutManager grid2 = new GridLayoutManager(this, 1);
        grid2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(grid2);
        SearchAdapter adapter = new SearchAdapter(WallpaperView.this);
        recyclerView2.setAdapter(adapter);
        recyclerView2.setHasFixedSize(true);

        adapter.filter(mName);

        complete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                new AlertDialog.Builder(context).setTitle("Download Complete").setMessage("Your wallpaper has been downloaded.").setPositiveButton("Downloads", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    }
                }).create().show();
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallpaper_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {


            if(title.contains("*")){
                new AlertDialog.Builder(this).setTitle("Credit Required").setMessage("Credit is required for this wallpaper. Make sure you check the about section for who made this wallpaper so you can give them credit.").setPositiveButton("Start Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(path));
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(getTitle() + ".png"));
                        r.allowScanningByMediaScanner();
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                        download();
                    }
                }).create().show();
            }else{
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(path));
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".png");
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
                download();
            }

            registerReceiver(complete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            return true;
        }

        if(id == R.id.action_set) {
            setWallpaperURL(path, author);
            return true;
        }

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
        Intent i = null;

        if (up.matches("Flat")) {
            i = new Intent(this, Flat.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if(up.matches("Fav")) {
            i = new Intent(this, Fav.class);
        } else if(up.matches("Search")){
            i = new Intent(this, SearchActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        System.gc();
        Runtime.getRuntime().gc();

        return i;
    }

    public void download(){
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Downloading...");

        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setProgress(0);

        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean downloading = true;
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                for (int downloadinger = 0; downloading; downloadinger++) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        progressBarDialog.dismiss();
                        downloading = false;
                    }

                    final int dl_progress = downloadinger;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarDialog.setProgress(dl_progress);
                        }
                    });

                    cursor.close();
                }
            }
        }).start();
        progressBarDialog.show();
    }

    public int darkColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    public void setWallpaperURL(final String src, final String folder) {
        new Thread() {
            @Override
            public void run() {
                try {
                    WallpaperManager manager = WallpaperManager.getInstance(WallpaperView.this);
                    Bitmap overlay = Bitmap.createBitmap(manager.getDesiredMinimumWidth(), manager.getDesiredMinimumHeight(), Bitmap.Config.ARGB_8888);
                    Bitmap wall = ((BitmapDrawable) Cache.getDrawable(folder.toLowerCase().replace(" ", "_"), src.replace("/", "").replace(":", "").replace(".", ""), src, WallpaperView.this)).getBitmap();

                    if (wall.getHeight() >= overlay.getHeight() && wall.getWidth() >= overlay.getWidth()) {
                        Canvas canvas = new Canvas(overlay);
                        canvas.drawBitmap(overlay, new Matrix(), null);
                        canvas.drawBitmap(wall, (overlay.getWidth()/2)-(wall.getWidth()/2), (overlay.getHeight()/2)-(wall.getHeight()/2), null);

                        manager.setBitmap(overlay);
                    } else {
                        manager.setBitmap(wall);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(WallpaperView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WallpaperView.this, "Enjoy your new wallpaper :)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageee.setImageDrawable(null);
    }
}
