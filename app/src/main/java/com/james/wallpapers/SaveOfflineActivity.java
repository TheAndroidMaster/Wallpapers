package com.james.wallpapers;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class SaveOfflineActivity extends AppCompatActivity {

    ArrayList<String[]> urls = new ArrayList<>();
    LinearLayout ll;
    ProgressBar progress;
    private AccountHeader headerResult = null;
    Drawer result;
    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        ll = (LinearLayout) findViewById(R.id.container);
        progress = (ProgressBar) findViewById(R.id.progress);

        final String[] pplnames = getResources().getStringArray(R.array.people_names);
        final TypedArray tab_urls = getResources().obtainTypedArray(R.array.wp_urls);

        for(int i = 0; i < pplnames.length; i++){
            AppCompatCheckBox switchCompat = new AppCompatCheckBox(this);
            switchCompat.setText(pplnames[i]);
            switchCompat.setTag(i);
            urls.add(getResources().getStringArray(tab_urls.getResourceId(i, -1)));
            switchCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatCheckBox box = (AppCompatCheckBox) v;
                    if (box.isChecked() && !running) {
                        running = true;
                        progress.setIndeterminate(true);
                        download(urls.get((int) v.getTag()), pplnames[(int) v.getTag()]);
                    } else {
                        if(running && !box.isChecked()) running = false;
                        if (!box.isChecked()) {
                            Cache.delete(pplnames[(int) v.getTag()].toLowerCase().replace(" ", "_"), SaveOfflineActivity.this);
                            if (!running) Snackbar.make(findViewById(R.id.root), "Deleted", Snackbar.LENGTH_SHORT).show();
                        }
                        else if (running) box.setChecked(false);
                    }
                }
            });
            switchCompat.setPadding(16, 16, 16, 16);
            switchCompat.setChecked(Cache.dir(pplnames[i].toLowerCase().replace(" ", "_"), this));
            ll.addView(switchCompat);
        }

        tab_urls.recycle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
                .withSelectedItem(3)
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
                            intent = new Intent(SaveOfflineActivity.this, MainActivity.class);
                        } else if (drawerItem.getIdentifier() == 2) {
                            intent = new Intent(SaveOfflineActivity.this, Flat.class);
                        } else if (drawerItem.getIdentifier() == 3) {
                            intent = new Intent(SaveOfflineActivity.this, About.class);
                        } else if (drawerItem.getIdentifier() == 5) {
                            intent = new Intent(SaveOfflineActivity.this, SaveOfflineActivity.class);
                        } else if (drawerItem.getIdentifier() == 4) {
                            intent = new Intent(SaveOfflineActivity.this, Fav.class);
                        }
                        if (intent != null) {
                            SaveOfflineActivity.this.startActivity(intent);
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

    public void download(final String[] downloads, final String folder) {
        new Thread() {
            public void run() {
                for (String download : downloads) {
                    if (running)
                        Cache.saveDrawable(folder, download.replace("/", "").replace(":", "").replace(".", ""), Cache.getDrawable(folder, "", download, SaveOfflineActivity.this), getApplicationContext());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setIndeterminate(false);
                        if (running) {
                            Snackbar.make(findViewById(R.id.root), "Downloaded", Snackbar.LENGTH_SHORT).show();
                            running = false;
                        }
                    }
                });
            }
        }.start();

    }
}
