package com.james.wallpapers.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.james.wallpapers.BuildConfig;
import com.james.wallpapers.R;
import com.james.wallpapers.fragments.FavFragment;
import com.james.wallpapers.fragments.HomeFragment;
import com.james.wallpapers.fragments.WallpaperFragment;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    View fragmentView;
    Fragment fragment;
    Drawer drawer;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("first", true)) {
            startActivity(new Intent(MainActivity.this, FirstTime.class));
            prefs.edit().putBoolean("first", false).apply();
        }

        if (prefs.getInt("version", 0) != BuildConfig.VERSION_CODE) {
            new AlertDialog.Builder(this).setTitle("Changelog").setMessage(getString(R.string.changelog)).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putInt("version", BuildConfig.VERSION_CODE).apply();
                    dialog.dismiss();
                }
            }).create().show();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentView = findViewById(R.id.fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_toggle);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withToolbar(toolbar)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(this)
                        .withCompactStyle(false)
                        .withHeaderBackground(R.mipmap.wpicon)
                        .withProfileImagesClickable(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .addProfiles(
                                new ProfileDrawerItem().withName("Fornax").withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(ContextCompat.getDrawable(this, R.mipmap.wpicon))
                        )
                        .build())
                .withSelectedItem(0)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(getString(R.string.title_home)).withIdentifier(1).withIcon(R.drawable.ic_home),
                        new SecondaryDrawerItem().withName(getString(R.string.title_wallpapers)).withIdentifier(2).withIcon(R.drawable.ic_wallpaper),
                        new SecondaryDrawerItem().withName(getString(R.string.title_favorites)).withIdentifier(3).withIcon(R.drawable.ic_fav),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(getString(R.string.title_about)).withIdentifier(4).withSelectable(false).withIcon(R.drawable.ic_info)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                fragment = new HomeFragment();
                                setTitle(getString(R.string.title_home));
                                break;
                            case 2:
                                fragment = new WallpaperFragment();
                                setTitle(getString(R.string.title_wallpapers));
                                break;
                            case 3:
                                fragment = new FavFragment();
                                setTitle(getString(R.string.title_favorites));
                                break;
                            case 4:
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return false;
                    }
                }).buildView();

        View v = drawer.getSlider();
        v.setLayoutParams(new ViewGroup.LayoutParams(DrawerUIUtils.getOptimalDrawerWidth(this), ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.drawer)).addView(v);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                setSelection(fragment);
            }
        });

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (fragment != null) {
                setSelection(fragment);
                return;
            }
        }

        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
    }

    private void setSelection(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            drawer.setSelection(1);
        } else if (fragment instanceof WallpaperFragment) {
            drawer.setSelection(2);
        } else if (fragment instanceof FavFragment) {
            drawer.setSelection(3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
