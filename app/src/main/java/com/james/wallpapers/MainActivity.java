package com.james.wallpapers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends ActionBarActivity {

    private AccountHeader headerResult = null;

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

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://theandroidmaster.github.io/")));
            }
        });

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Flat.class));
            }
        });

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });

        findViewById(R.id.imageView6).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this, FirstTime.class));
                return true;
            }
        });


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
        int selected = 0;
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withStatusBarColorRes(R.color.blued)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withSelectedItem(selected)
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
                            intent = new Intent(MainActivity.this, MainActivity.class);
                        } else if (drawerItem.getIdentifier() == 2) {
                            intent = new Intent(MainActivity.this, Flat.class);
                        } else if (drawerItem.getIdentifier() == 3) {
                            intent = new Intent(MainActivity.this, About.class);
                        } else if (drawerItem.getIdentifier() == 5) {
                            intent = new Intent(MainActivity.this, SaveOfflineActivity.class);
                        } else if (drawerItem.getIdentifier() == 4) {
                            intent = new Intent(MainActivity.this, Fav.class);
                        }
                        if (intent != null) {
                            MainActivity.this.startActivity(intent);
                        }
                        return false;
                    }
                })


                .build();

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
