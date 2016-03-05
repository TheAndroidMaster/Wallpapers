package com.james.wallpapers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Fornax");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        if(height > width) appbar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width));
        else appbar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/2));

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.putExtra("up", "Main");
                startActivity(i);
            }
        });

        final SharedPreferences prefs = getSharedPreferences("com.james.wallpapers", 0);

        prefs.edit().putBoolean("installed", true).apply();

        if(prefs.getBoolean("first", true)){
            startActivity(new Intent(MainActivity.this, FirstTime.class));
            prefs.edit().putBoolean("first", false).apply();
        }

        String changelog = getString(R.string.changelog);
        changelog = changelog.replace("ande", System.getProperty("line.separator") + "-");

        int versionCode = BuildConfig.VERSION_CODE;
        int version = prefs.getInt("version", 0);
        if(!(version == versionCode)){
            new AlertDialog.Builder(this).setTitle("Changelog").setMessage(changelog).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putInt("version", BuildConfig.VERSION_CODE).apply();
                    dialog.dismiss();
                }
            }).create().show();
        }

        //inflates menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        ArrayList<Parcelable> items = new ArrayList<>();
        items.add(new HeaderListData(null, getResources().getString(R.string.app_desc), true, null));
        items.add(new PersonListData(R.mipmap.wallpaperhome, "Flat Wallpapers", "All of these wallpapers are designed with a flat base, to make the depth of material design seem more convincing.", new Intent(MainActivity.this, Flat.class)));
        items.add(new PersonListData(R.mipmap.websitehome, "The Website", "The website contains information about this app and a few other apps made by me.", new Intent(Intent.ACTION_VIEW, Uri.parse("http://theandroidmaster.github.io"))));
        items.add(new PersonListData(R.mipmap.infohome, "The About Section", "Information about this app's creator and designers.", new Intent(MainActivity.this, About.class)));

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AboutAdapter(this, items));

        new Drawer(this).initDrawer(toolbar);
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
}
