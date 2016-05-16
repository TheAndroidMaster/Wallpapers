package com.james.wallpapers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.wallpapers.R;
import com.james.wallpapers.fragments.FeaturedFragment;
import com.james.wallpapers.fragments.ListFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FeaturedFragment();
            case 1:
                Bundle args = new Bundle();
                args.putInt("id", -1);
                Fragment f = new ListFragment();
                f.setArguments(args);
                return f;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.featured);
            case 1:
                return context.getString(R.string.all);
            default:
                return "";
        }
    }
}
