package com.james.wallpapers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class About extends AppCompatActivity {

    int iconcount;

    ViewPager vp;
    IconPagerAdapter adapter;

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        iconcount = prefs.getInt("iconcount", 0);

        vp = (ViewPager) findViewById(R.id.view_pager);
        adapter = new IconPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(iconcount);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PackageManager pm = getPackageManager();
                ComponentName c1 = new ComponentName("com.james.wallpapers", "com.james.wallpapers.MainActOne");
                ComponentName c2 = new ComponentName("com.james.wallpapers", "com.james.wallpapers.MainActTwo");
                ComponentName c3 = new ComponentName("com.james.wallpapers", "com.james.wallpapers.MainActThree");
                switch(position) {
                    case 0:
                        pm.setComponentEnabledSetting(c3, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c1, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        break;
                    case 1:
                        pm.setComponentEnabledSetting(c3, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c2, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c1, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        break;
                    case 2:
                        pm.setComponentEnabledSetting(c3, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        pm.setComponentEnabledSetting(c1, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        break;
                }
                iconcount = position;
                prefs.edit().putInt("iconcount", iconcount).apply();

                adapter.onScroll(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Fornax v" + BuildConfig.VERSION_NAME);

        ArrayList<Parcelable> items = new ArrayList<>();

        TypedArray peopleicons = getResources().obtainTypedArray(R.array.people_icons);
        String[] peoplenames = getResources().getStringArray(R.array.people_names);
        String[] peopledescs = getResources().getStringArray(R.array.people_desc);
        String[] peopleurls = getResources().getStringArray(R.array.people_urls);

        items.add(new HeaderListData("Contributors:", null, true, null));

        for (int i = 0; i < peoplenames.length; i++) {
            items.add(new PersonListData(peopleicons.getResourceId(i, R.mipmap.icontwo), peoplenames[i], peopledescs[i], new Intent(Intent.ACTION_VIEW, Uri.parse(peopleurls[i]))));
        }
        peopleicons.recycle();

        items.add(new HeaderListData(null, "Thanks to Justin Kruit for letting me rent his server to load all these wallpapers, which take up more than 3GB of space.", false, new Intent(Intent.ACTION_VIEW, Uri.parse("http://justinkruit.nl/me/"))));
        items.add(new HeaderListData(null, "Also thanks to Jared Gauthier for making Fornax's amazing icons! (you can swipe between them at the top of this page)", false, new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+JaredGauthier"))));

        String[] headers = getResources().getStringArray(R.array.namey);
        String[] contents = getResources().getStringArray(R.array.desc);
        String[] urls = getResources().getStringArray(R.array.uri);

        items.add(new HeaderListData("Implemented Libraries:", null, true, null));

        for (int i = 0; i < headers.length; i++) {
            items.add(new TextListData(headers[i], contents[i], new Intent(Intent.ACTION_VIEW, Uri.parse(urls[i]))));
        }

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AboutAdapter(this, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
