package com.james.wallpapers;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Flat extends FragmentActivity {

    private AccountHeader headerResult = null;
    int color;
    Drawer result;
    List<String> items;
    TabLayout tabLayout;
    Toolbar toolbar;
    String[] tabnames;
    Drawable drawable;

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

        final TypedArray tab_names = getResources().obtainTypedArray(R.array.wp_names);
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

        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        refresh(tab_names.getResourceId(position, -1), tab_urls.getResourceId(position, -1), tabnames[position]);
                    }
                });

        //initial tab
        refresh(tab_names.getResourceId(0, -1), tab_urls.getResourceId(0, -1), tabnames[0]);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.mipmap.wpicon)
                .withProfileImagesClickable(false)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem().withName("Fornax").withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(getResources().getDrawable(R.mipmap.wpicon))
                )
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withStatusBarColorRes(R.color.blued)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(
                        //pass your items here
                        new SecondaryDrawerItem().withName("Home").withIdentifier(1).withIcon(FontAwesome.Icon.faw_home),
                        new SecondaryDrawerItem().withName("Wallpapers").withIdentifier(2).withIcon(FontAwesome.Icon.faw_picture_o),
                        new SecondaryDrawerItem().withName("Favorites").withIdentifier(4).withIcon(FontAwesome.Icon.faw_heart),
                        new SecondaryDrawerItem().withName("Offline").withIdentifier(5).withIcon(FontAwesome.Icon.faw_download),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("About").withIdentifier(3).withCheckable(false).withIcon(FontAwesome.Icon.faw_info_circle)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        Intent intent = null;
                        if (drawerItem.getIdentifier() == 1) {
                            intent = new Intent(Flat.this, MainActivity.class);
                        } else if (drawerItem.getIdentifier() == 2) {
                            intent = new Intent(Flat.this, Flat.class);
                        } else if (drawerItem.getIdentifier() == 3) {
                            intent = new Intent(Flat.this, About.class);
                        } else if (drawerItem.getIdentifier() == 5) {
                            intent = new Intent(Flat.this, SaveOfflineActivity.class);
                        } else if (drawerItem.getIdentifier() == 4) {
                            intent = new Intent(Flat.this, Fav.class);
                        }
                        if (intent != null) {
                            Flat.this.startActivity(intent);
                        }
                        return false;
                    }
                })


                .build();

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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

    public void refresh(final int whichname, final int whichurl, final String tabname){
        final String[] url = getResources().getStringArray(whichurl);
        new Thread() {
            @Override
            public void run() {
                drawable = Cache.getDrawable(tabname.toLowerCase().replace(" ", "_"), url[random(whichurl)].replace("/", "").replace(":", "").replace(".", ""), url[random(whichurl)], Flat.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = (ImageView) findViewById(R.id.header);
                        imageView.setImageDrawable(drawable);

                        Palette.from(((BitmapDrawable) drawable).getBitmap()).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                                collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(Color.GRAY));
                                tabLayout.setBackgroundColor(palette.getMutedColor(Color.DKGRAY));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(darkColor(palette.getVibrantColor(Color.GRAY))) ;
                                };
                            }
                        });
                    }
                });
            }
        }.start();
    }

    public int random(int urls){
        Random random = new Random();
        return random.nextInt(getResources().getStringArray(urls).length);
    }

    public int darkColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(darkColor(color));
        }
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