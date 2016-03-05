package com.james.wallpapers;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class SaveOfflineActivity extends AppCompatActivity {

    ArrayList<String[]> urls = new ArrayList<>();
    LinearLayout ll;
    ProgressBar progress;
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

        new Drawer(this).initDrawer((Toolbar) findViewById(R.id.toolbar));
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
