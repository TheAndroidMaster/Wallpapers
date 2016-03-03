package com.james.wallpapers;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private TypedArray names;
    private TypedArray urls;

    public ViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    public void setFragments(TypedArray names, TypedArray urls) {
        this.names = names;
        this.urls = urls;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("names", names.getResourceId(position, -1));
        bundle.putInt("urls", urls.getResourceId(position, -1));
        bundle.putString("tabname", getPageTitle(position).toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.people_names)[position];
    }
}
