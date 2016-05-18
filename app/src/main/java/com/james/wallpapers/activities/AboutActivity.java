package com.james.wallpapers.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.james.wallpapers.BuildConfig;
import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.adapters.AboutAdapter;
import com.james.wallpapers.data.AuthorData;
import com.james.wallpapers.data.HeaderListData;
import com.james.wallpapers.data.PersonListData;
import com.james.wallpapers.data.TextListData;

import java.util.ArrayList;


public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Parcelable> items = new ArrayList<>();

        if (getResources().getBoolean(R.bool.show_contributors)) {
            items.add(new HeaderListData(getString(R.string.contributors), null, true, null));

            for (AuthorData data : ((Supplier) getApplicationContext()).getAuthors(this)) {
                items.add(new PersonListData(data.image, data.name, data.description, data.url));
            }
        }

        String[] headers = getResources().getStringArray(R.array.namey);
        String[] contents = getResources().getStringArray(R.array.desc);
        String[] urls = getResources().getStringArray(R.array.uri);

        items.add(new HeaderListData(getString(R.string.libraries), null, true, null));

        for (int i = 0; i < headers.length; i++) {
            items.add(new TextListData(headers[i], contents[i], new Intent(Intent.ACTION_VIEW, Uri.parse(urls[i]))));
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 1) );
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
