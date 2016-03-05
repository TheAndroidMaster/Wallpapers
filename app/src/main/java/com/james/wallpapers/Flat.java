package com.james.wallpapers;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Flat extends FragmentActivity {

    List<String> items;
    TabLayout tabLayout;
    Toolbar toolbar;
    String[] tabnames;
    Drawable drawable;

    Random random;

    ViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_picker);

        //inflates menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flat.this, SearchActivity.class);
                i.putExtra("up", "Flat");
                startActivity(i);
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        if(height > width) appbar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width));
        else appbar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/2));

        items = new ArrayList<>();

        TextView text = new TextView(this);
        text.setText("Wallpapers");
        text.setTextAppearance(this, R.style.MyTitleTextStyle);
        toolbar.addView(text);

        TypedArray tab_names = getResources().obtainTypedArray(R.array.wp_names);
        final TypedArray tab_urls = getResources().obtainTypedArray(R.array.wp_urls);

        //tabs
        tabLayout = (TabLayout) findViewById(R.id.tl);

        tabnames = getResources().getStringArray(R.array.people_names);

        mViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPagerAdapter.setFragments(tab_names, tab_urls);
        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        random = new Random();

        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        refresh(tab_urls.getResourceId(position, -1), tabnames[position]);
                    }
                });

        //initial tab
        refresh(tab_urls.getResourceId(0, -1), tabnames[0]);

        new Drawer(this).initDrawer(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void refresh(final int whichurl, final String tabname){
        final String[] url = getResources().getStringArray(whichurl);
        new Thread() {
            @Override
            public void run() {
                int rand =  random.nextInt(url.length);
                drawable = new BitmapDrawable(getResources(), Cache.cropToSquare(((BitmapDrawable) Cache.getDrawable(tabname.toLowerCase().replace(" ", "_"), url[rand].replace("/", "").replace(":", "").replace(".", ""), url[rand], Flat.this)).getBitmap()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = (ImageView) findViewById(R.id.header);
                        Drawable prev = imageView.getDrawable();
                        if (prev instanceof TransitionDrawable) prev = ((TransitionDrawable) prev).getDrawable(1);

                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{prev, drawable});
                        imageView.setImageDrawable(td);
                        td.startTransition(250);

                        Palette.from(((BitmapDrawable) drawable).getBitmap()).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                                collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(Color.GRAY));

                                TransitionDrawable td = new TransitionDrawable(new Drawable[]{tabLayout.getBackground(), new ColorDrawable(palette.getMutedColor(Color.DKGRAY))});
                                tabLayout.setBackground(td);
                                td.startTransition(250);
                            }
                        });
                    }
                });
            }
        }.start();
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