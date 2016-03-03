package com.james.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private String[] names, urls;
    int array, arrays, loaded = -1;
    String tabname;
    Context context;
    Activity activity;
    ArrayList<Drawable> imgs = new ArrayList<>();
    boolean imageload, loading;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public View imagel;
        public Thread thread;
        public int loaded;
        public ViewHolder(View v, View imagel) {
            super(v);
            this.v = v;
            this.imagel = imagel;
        }
    }

    public ListAdapter(int names, int urls, Activity activity, final String tabname) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.names = context.getResources().getStringArray(names);
        this.urls = context.getResources().getStringArray(urls);
        this.tabname = tabname;
        array = names;
        arrays = urls;

        imageload = Cache.dir(tabname.toLowerCase().replace(" ", "_"), context);

        if (imageload) {
            loading = true;
            new Thread() {
                public void run() {
                    for (loaded += 1; loaded < ListAdapter.this.urls.length; loaded++) {
                        imgs.add(loaded, Cache.getCompressedDrawable(tabname.toLowerCase().replace(" ", "_"), ListAdapter.this.urls[loaded].replace("/", "").replace(":", "").replace(".", ""), ListAdapter.this.urls[loaded], context));
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading = false;
                        }
                    });
                }
            }.start();
        } else {
            loading = true;
            new Thread() {
                public void run() {
                    for (loaded += 1; loaded < ListAdapter.this.urls.length; loaded++) {
                        imgs.add(loaded, Cache.downloadCompressedDrawable(ListAdapter.this.urls[loaded], context));
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading = false;
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v, v.findViewById(R.id.image));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.v.setTag(position);
        final SquareImageView image = (SquareImageView) holder.imagel;
        final TextView title = (TextView) holder.v.findViewById(R.id.title);

        holder.loaded = 0;

        image.setImageBitmap(null);

        String titlestring = names[position];
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WallpaperView.class);
                intent.putExtra("num", (int) v.getTag());
                intent.putExtra("array", array);
                intent.putExtra("arrays", arrays);
                intent.putExtra("auth", tabname);
                if (imgs.size() > (int) v.getTag() || !loading) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ((BitmapDrawable) imgs.get((int) v.getTag())).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    intent.putExtra("preload", b);
                }
                intent.putExtra("up", "Flat");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && (imgs.size() > (int) v.getTag() || !loading)) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, ((BitmapDrawable) imgs.get((int) v.getTag())).getBitmap(), 5, 5);
                    ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });
        title.setBackgroundColor(context.getResources().getColor(R.color.orange));
        titlestring = titlestring.replace("*", "");
        title.setText(titlestring);

        if (!loading && loaded > position) {
            manage(position);
            image.setImageDrawable(imgs.get(position));
            title.setBackgroundColor(getDominantColor(((BitmapDrawable) imgs.get(position)).getBitmap()));
        } else {
            holder.loaded = 1;
            holder.thread = new Thread() {
                public void run() {
                    while(loaded <= position) {
                        if (!loading) {
                            ListAdapter.this.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    reload();
                                }
                            });
                        }
                        try {
                            this.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        if (holder.loaded == 0) return;
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(context.getResources().getColor(R.color.orange)), imgs.get(position)});
                            image.setImageDrawable(td);
                            holder.loaded = 0;
                            td.startTransition(200);
                            title.setBackgroundColor(getDominantColor(((BitmapDrawable) imgs.get(position)).getBitmap()));
                        }
                    });
                }
            };
            holder.thread.start();
        }
    }

    private void manage(final int pos) {
        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    private void reload() {
        loading = true;
        if (imageload) {
            new Thread() {
                public void run() {
                    int load = loaded + 1;
                    if (ListAdapter.this.urls.length <= 1) load = 0;
                    for (loaded = load; loaded < ListAdapter.this.urls.length; loaded++) {
                        imgs.add(loaded, Cache.getCompressedDrawable(ListAdapter.this.tabname.toLowerCase().replace(" ", "_"), ListAdapter.this.urls[loaded].replace("/", "").replace(":", "").replace(".", ""), ListAdapter.this.urls[loaded], context));
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading = false;
                        }
                    });
                }
            }.start();
        } else {
            new Thread() {
                public void run() {
                    int load = loaded + 1;
                    if (ListAdapter.this.urls.length <= 1) load = 0;
                    for (loaded = load; loaded < ListAdapter.this.urls.length; loaded++) {
                        imgs.add(loaded-1, Cache.downloadCompressedDrawable(ListAdapter.this.urls[loaded-1], context));
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading = false;
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.green
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public void close() {
        if (!loading) {
            loaded = -1;
            for (int i = imgs.size() - 1; i >= 0; i--) {
                imgs.remove(i);
            }
        }
        System.gc();
        Runtime.getRuntime().gc();
    }
}
