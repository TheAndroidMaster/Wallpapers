package com.james.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] names, urls;
    int array, arrays, loaded = -1, orientation;
    String tabname;
    Context context;
    Activity activity;
    ArrayList<Drawable> imgs = new ArrayList<>();
    boolean loading;

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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar v;
        public ProgressViewHolder(ProgressBar v) {
            super(v);
            this.v = v;
        }
    }

    public ListAdapter(int names, int urls, Activity activity, final String tabname, int orientation) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.names = context.getResources().getStringArray(names);
        this.urls = context.getResources().getStringArray(urls);
        this.tabname = tabname;
        array = names;
        arrays = urls;

        this.orientation = orientation;

        new Thread() {
            public void run() {
                for (loaded += 1; loaded < ListAdapter.this.urls.length; loaded++) {
                    try {
                        imgs.add(loaded, Cache.getCompressedDrawable(tabname.toLowerCase().replace(" ", "_"), ListAdapter.this.urls[loaded].replace("/", "").replace(":", "").replace(".", ""), ListAdapter.this.urls[loaded], context));
                        ListAdapter.this.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemInserted(loaded);
                            }
                        });
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        try {
                            sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
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

    @Override
    public int getItemViewType(int position) {
        if (loading && position == imgs.size() + 1) return 1;
        else return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) return new ProgressViewHolder(new ProgressBar(parent.getContext()));
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
            return new ViewHolder(v, v.findViewById(R.id.image));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            ((ProgressViewHolder) holder).v.setIndeterminate(true);
            new Thread() {
                public void run() {
                    while(loading) {
                        try {
                            sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                        if (!loading) break;
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    });
                }
            };
            return;
        }

        ViewHolder viewHolder = (ViewHolder) holder;
        if (viewHolder.thread != null && !viewHolder.thread.isInterrupted()) {
            viewHolder.thread.interrupt();
        }

        final SquareImageView image = (SquareImageView) viewHolder.imagel;
        image.setOrientation(orientation);

        TextView title = (TextView) viewHolder.v.findViewById(R.id.title);

        viewHolder.loaded = 0;

        image.setImageBitmap(null);

        String titlestring = names[position];
        titlestring = titlestring.replace("*", "");
        title.setText(titlestring);

        viewHolder.v.findViewById(R.id.card).setTag(position);
        viewHolder.v.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
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
                if (imgs.size() > (int) v.getTag() || !loading) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, ((BitmapDrawable) imgs.get((int) v.getTag())).getBitmap(), 5, 5);
                    ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });

        if (!loading && loaded > position) {
            manage(position);
            image.setImageDrawable(imgs.get(position));
        } else if (!loading) {
            viewHolder.loaded = 1;
            viewHolder.thread = new Thread() {
                public void run() {
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reload();
                        }
                    });
                    while(loaded <= holder.getAdapterPosition() || imgs.size() <= holder.getAdapterPosition()) {
                        try {
                            sleep(2000);
                        } catch (InterruptedException ignored) {
                        }
                        if (((ViewHolder) holder).loaded == 0) return;
                    }
                    ListAdapter.this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(context.getResources().getColor(R.color.orange)), imgs.get(holder.getAdapterPosition())});
                            image.setImageDrawable(td);
                            ((ViewHolder) holder).loaded = 0;
                            td.startTransition(200);
                        }
                    });
                }
            };
            ((ViewHolder) holder).thread.start();
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
        new Thread() {
            public void run() {
                int load = loaded + 1;
                if (ListAdapter.this.urls.length <= 1) load = 0;
                for (loaded = load; loaded < ListAdapter.this.urls.length; loaded++) {
                    try {
                        imgs.add(loaded, Cache.getCompressedDrawable(ListAdapter.this.tabname.toLowerCase().replace(" ", "_"), ListAdapter.this.urls[loaded].replace("/", "").replace(":", "").replace(".", ""), ListAdapter.this.urls[loaded], context));
                        ListAdapter.this.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemInserted(loaded);
                            }
                        });
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        break;
                    }
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

    @Override
    public int getItemCount() {
        if (loading) return imgs.size() + 1;
        else return imgs.size();
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
