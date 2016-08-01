package com.james.wallpapers;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.apps.muzei.api.Artwork;
import com.james.wallpapers.data.WallData;

public class MuzeiArtSource extends com.google.android.apps.muzei.api.MuzeiArtSource {

    private Supplier supplier;

    public MuzeiArtSource() {
        super("MuzeiArtSource");
    }

    protected void onUpdate(int reason) {
        if (supplier != null) supplier = (Supplier) getApplicationContext();

        new Thread() {
            @Override
            public void run() {
                if (!supplier.getNetworkResources()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MuzeiArtSource.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        for (WallData data : supplier.getWallpapers(MuzeiArtSource.this)) {
                            publishArtwork(new Artwork.Builder()
                                    .imageUri(Uri.parse(data.url))
                                    .title(data.name)
                                    .byline(data.authorName)
                                    .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(data.url)))
                                    .build());
                        }
                    }
                });
            }
        }.start();
    }
}
