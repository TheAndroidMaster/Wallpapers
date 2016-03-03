package com.james.wallpapers;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MuzeiArtSource extends com.google.android.apps.muzei.api.MuzeiArtSource {

    int counter;
    List<String> name, author;
    List<Uri> url;

    public MuzeiArtSource(){
        super("MuzeiArtSource");
    }
    protected void onUpdate(int reason) {

        name = new ArrayList<>();
        url = new ArrayList<>();
        author = new ArrayList<>();

        counter = 0;
        final TypedArray tab_names = getResources().obtainTypedArray(R.array.wp_names);
        final TypedArray tab_urls = getResources().obtainTypedArray(R.array.wp_urls);

        while(counter<tab_names.length()){
            refresh(tab_names.getResourceId(counter, -1), tab_urls.getResourceId(counter, -1));
            counter = counter + 1;
        }
        tab_names.recycle();
        tab_urls.recycle();

        Random random = new Random();
        int rand = random.nextInt(name.size());

        publishArtwork(new Artwork.Builder()
                .imageUri(url.get(rand))
                .title(name.get(rand))
                .byline(author.get(rand))
                .viewIntent(new Intent(Intent.ACTION_VIEW, url.get(rand)))
                .build());
    }

    public void refresh(int nameid, int urlid) {
        String[] names = getResources().getStringArray(nameid);
        String[] urls = getResources().getStringArray(urlid);
        String[] authors = getResources().getStringArray(R.array.people_names);
        int count = 0;
        while(count<names.length){
            name.add(count, names[count]);
            url.add(count, Uri.parse(urls[count]));
            author.add(count, authors[counter]);
            count = count + 1;
        }
    }
}
