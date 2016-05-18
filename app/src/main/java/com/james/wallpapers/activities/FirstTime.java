package com.james.wallpapers.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.alexandrepiveteau.library.tutorial.CustomAction;
import com.alexandrepiveteau.library.tutorial.TutorialActivity;
import com.alexandrepiveteau.library.tutorial.TutorialFragment;
import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;

public class FirstTime extends TutorialActivity  {

    private int[] BACKGROUND_COLORS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BACKGROUND_COLORS = new int[]{
                ContextCompat.getColor(this, R.color.intro_color),
                ContextCompat.getColor(this, R.color.wallpapers_color),
                ContextCompat.getColor(this, R.color.design_color),
                ContextCompat.getColor(this, R.color.favorites_color),
                ContextCompat.getColor(this, R.color.community_color),
                ContextCompat.getColor(this, R.color.rate_color)
        };

        super.onCreate(savedInstanceState);
    }

    @Override
    public String getIgnoreText() {
        return "Skip";
    }
    @Override
    public int getCount() {
        return 6;
    }
    @Override
    public int getBackgroundColor(int position) {
        return BACKGROUND_COLORS[position];
    }
    @Override
    public int getNavigationBarColor(int position) {
        return BACKGROUND_COLORS[position];
    }
    @Override
    public int getStatusBarColor(int position) {
        return BACKGROUND_COLORS[position];
    }
    @Override
    public TutorialFragment getTutorialFragmentFor(int position) {
        switch (position) {
            case 0:
                return new TutorialFragment.Builder()
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_desc))
                        .setImageResourceBackground(R.mipmap.icontwo)
                        .build();
            case 1:
                return new TutorialFragment.Builder()
                        .setTitle(Supplier.getWallpapers(this).size() + " Wallpapers")
                        .setDescription(getString(R.string.wallpapers_desc))
                        .setImageResource(R.mipmap.star)
                        .setImageResourceBackground(R.mipmap.frames)
                        .build();
            case 2:
                return new TutorialFragment.Builder()
                        .setTitle(getString(R.string.design_title))
                        .setDescription(getString(R.string.design_desc))
                        .setImageResource(R.mipmap.ruler)
                        .setImageResourceBackground(R.mipmap.ruler_bg)
                        .build();
            case 3:
                return new TutorialFragment.Builder()
                        .setTitle(getString(R.string.favorites_title))
                        .setDescription(getString(R.string.favorites_desc))
                        .setImageResourceBackground(R.mipmap.fav_cards)
                        .build();
            case 4:
                return new TutorialFragment.Builder()
                        .setTitle(getString(R.string.community_title))
                        .setDescription(getString(R.string.community_desc))
                        .setCustomAction(new CustomAction.Builder(Uri.parse(getString(R.string.community_url)))
                                .setIcon(R.drawable.social)
                                .build())
                        .setImageResourceBackground(R.mipmap.googleplus)
                        .build();
            case 5:
                return new TutorialFragment.Builder()
                        .setTitle(getString(R.string.rate_title))
                        .setDescription(getString(R.string.rate_desc))
                        .setCustomAction(new CustomAction.Builder(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()))
                                .setIcon(R.drawable.download_play)
                                .build())
                        .setImageResource(R.mipmap.rate_fg)
                        .setImageResourceBackground(R.mipmap.rate_bg)
                        .build();
            default:
                return new TutorialFragment.Builder().build();
        }
    }

    @Override
    public boolean isNavigationBarColored() {
        return true;
    }

    @Override
    public boolean isStatusBarColored() {
        return true;
    }

    @Override
    public ViewPager.PageTransformer getPageTransformer() {
        return TutorialFragment.getParallaxPageTransformer(2.5f);
    }
}

