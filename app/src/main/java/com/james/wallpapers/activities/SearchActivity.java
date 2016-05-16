package com.james.wallpapers.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import com.james.wallpapers.R;
import com.james.wallpapers.adapters.SearchAdapter;
import com.james.wallpapers.fragments.FavFragment;
import com.james.wallpapers.fragments.WallpaperFragment;

public class SearchActivity extends ActionBarActivity  {

    RecyclerView rv;
    SearchAdapter adapter;
    String up;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        up = getIntent().getStringExtra("up");

        rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new SearchAdapter(SearchActivity.this);
        rv.setAdapter(adapter);

        final EditText editText = (EditText) findViewById(R.id.search_view);
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refresh(editText.getText().toString());
                if(editText.getText().toString().toLowerCase().matches("do a barrel roll")) findViewById(R.id.fl).animate().rotationBy(360).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.search_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                v.setVisibility(View.GONE);
            }
        });
    }

    public void refresh(String filter){
        boolean isNull = filter.matches("") || filter.matches(" ");

        if(isNull){
            findViewById(R.id.search_clear).setVisibility(View.GONE);
        }else{
            findViewById(R.id.search_clear).setVisibility(View.VISIBLE);
            adapter.filter(filter);
        }
    }
}
