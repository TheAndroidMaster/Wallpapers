package com.james.wallpapers;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;

import com.alexandrepiveteau.library.tutorial.CustomAction;
import com.alexandrepiveteau.library.tutorial.TutorialActivity;
import com.alexandrepiveteau.library.tutorial.TutorialFragment;

public class FirstTime extends TutorialActivity  {

    private int[] BACKGROUND_COLORS = {
            Color.parseColor("#2196F3"),
            Color.parseColor("#009688"),
            Color.parseColor("#212121"),
            Color.parseColor("#607D8B"),
            Color.parseColor("#212121"),
            Color.parseColor("#F44336"),
            Color.parseColor("#2196F3")};
    @Override
    public String getIgnoreText() {
        return "Skip";
    }
    @Override
    public int getCount() {
        return 7;
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
                        .setTitle("Fornax")
                        .setDescription("Free wallpapers, made by everyone, for everyone.")
                        .setImageResource(R.mipmap.wp)
                        .setImageResourceBackground(R.mipmap.hires)
                        .build();
            case 1:
                String[] name = getResources().getStringArray(R.array.name_james);
                String[] name1 = getResources().getStringArray(R.array.name_brice);
                String[] name2 = getResources().getStringArray(R.array.name_anas);
                String[] name3 = getResources().getStringArray(R.array.name_zan);
                String[] name4 = getResources().getStringArray(R.array.name_greg);
                int length = name.length + name1.length + name2.length + name3.length + name4.length;
                return new TutorialFragment.Builder()
                        .setTitle(length + " Wallpapers")
                        .setDescription("New wallpapers are always being added, and the app is improved a little every time.")
                        .setImageResource(R.mipmap.star)
                        .setImageResourceBackground(R.mipmap.frames)
                        .build();
            case 2:
                return new TutorialFragment.Builder()
                        .setTitle("Flat Design")
                        .setDescription("All wallpapers are made from scratch, based on a flat sheet.")
                        .setImageResource(R.mipmap.ruler)
                        .setImageResourceBackground(R.mipmap.ruler_bg)
                        .build();
            case 3:
                return new TutorialFragment.Builder()
                        .setTitle("Favorites Section")
                        .setDescription("The favorites section is a place for you to keep all your favorite wallpapers.")
                        .setImageResourceBackground(R.mipmap.fav_cards)
                        .build();
            case 4:
                return new TutorialFragment.Builder()
                        .setTitle("Daily Wallpapers")
                        .setDescription("Get a fresh look every day with Muzei.")
                        .setImageResource(R.mipmap.timer_fg)
                        .setImageResourceBackground(R.mipmap.timer_bg)
                        .setCustomAction(new CustomAction.Builder(Uri.parse("https://play.google.com/store/apps/details?id=net.nurik.roman.muzei"))
                                .setIcon(R.drawable.download_play)
                                .build())
                        .build();
            case 5:
                return new TutorialFragment.Builder()
                        .setTitle("Community")
                        .setDescription("Join the google plus community for news about upcoming updates, more wallpapers, and pictures of cute cats.")
                        .setCustomAction(new CustomAction.Builder(Uri.parse("https://plus.google.com/communities/104074488451953797559"))
                                .setIcon(R.drawable.social)
                                .build())
                        .setImageResourceBackground(R.mipmap.googleplus)
                        .build();
            case 6:
                return new TutorialFragment.Builder()
                        .setTitle("Ratings and Reviews")
                        .setDescription("Reviews are always appreciated. Please provide some feedback about your experience with this app.")
                        .setCustomAction(new CustomAction.Builder(Uri.parse("https://play.google.com/store/apps/details?id=com.james.wallpapers"))
                                .setIcon(R.drawable.download_play)
                                .build())
                        .setImageResource(R.mipmap.rate_fg)
                        .setImageResourceBackground(R.mipmap.rate_bg)
                        .build();
            default:
                return new TutorialFragment.Builder()
                        .setTitle("")
                        .setDescription("")
                        .setImageResource(R.mipmap.ic_launcher, false)
                        .build();
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

