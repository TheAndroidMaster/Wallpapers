package com.james.wallpapers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class About extends ActionBarActivity {

    int counter;
    int iconcount;

    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        iconcount = prefs.getInt("iconcount", 0);

        vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(new IconPagerAdapter(this, getSupportFragmentManager()));
        vp.setCurrentItem(iconcount);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Fornax v" + BuildConfig.VERSION_NAME);

        Resources res = getResources();
        final TypedArray peopleicons = res.obtainTypedArray(R.array.people_icons);
        final String[] peoplenames = res.getStringArray(R.array.people_names);
        final String[] peopledesc = res.getStringArray(R.array.people_desc);
        final String[] peopleurls = res.getStringArray(R.array.people_urls);

        counter = 0;

        while (counter<peoplenames.length) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            CardView container = new CardView(About.this);
            container.setId(counter);
            container.setClickable(true);
            container.setForeground(getSelectedItemDrawable());
            container.setUseCompatPadding(true);

            LinearLayout vertical = new LinearLayout(About.this);
            vertical.setOrientation(LinearLayout.VERTICAL);

            LinearLayout horizontal = new LinearLayout(About.this);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            horizontal.setGravity(Gravity.CENTER_VERTICAL);

            TextView name = new TextView(About.this);
            name.setText(peoplenames[counter]);
            name.setPadding(20, 20, 20, 20);
            name.setTextSize(20);
            name.setTextColor(getResources().getColor(R.color.black));

            TextView desc = new TextView(About.this);
            desc.setText(peopledesc[counter]);
            desc.setPadding(20, 0, 20, 20);

            ImageView profile = new ImageView(About.this);
            profile.setImageResource(peopleicons.getResourceId(counter, -1));
            int fifty = 100;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(fifty, fifty);
            params.setMargins(20, 20, 20, 20);
            profile.setLayoutParams(params);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(peopleurls[v.getId()])));
                }
            });

            findViewById(R.id.jk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://justinkruit.nl/me/")));
                }
            });

            horizontal.addView(profile);
            horizontal.addView(name);
            vertical.addView(horizontal);
            vertical.addView(desc);
            container.addView(vertical);
            linearLayout.addView(container);

            counter = counter + 1;
        }
        peopleicons.recycle();

        final String[] header = res.getStringArray(R.array.namey);
        final String[] content = res.getStringArray(R.array.desc);
        final String[] url = res.getStringArray(R.array.uri);

        counter = 0;

        while (counter < header.length) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.repos);
            final CardView lastused = new CardView(About.this);
            lastused.setId(counter);
            lastused.setClickable(true);
            lastused.setForeground(getSelectedItemDrawable());
            LinearLayout vertical = new LinearLayout(About.this);
            vertical.setOrientation(LinearLayout.VERTICAL);
            lastused.setUseCompatPadding(true);
            TextView txt1 = new TextView(About.this);
            TextView txt2 = new TextView(About.this);
            txt1.setText(header[counter]);
            txt1.setPadding(40, 30, 40, 0);
            txt1.setTextSize(20);
            txt1.setTextColor(getResources().getColor(R.color.black));
            txt2.setText(content[counter]);
            txt2.setPadding(40, 30, 40, 40);
            linearLayout.addView(lastused);
            lastused.addView(vertical);
            vertical.addView(txt1);
            vertical.addView(txt2);
            lastused.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(url[lastused.getId()]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            counter = counter + 1;
        }

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
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        findViewById(R.id.jg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+JaredGauthier/posts")));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = About.this.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }
}
