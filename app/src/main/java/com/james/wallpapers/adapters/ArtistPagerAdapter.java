package com.james.wallpapers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.wallpapers.fragments.ListFragment;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.data.AuthorData;

import java.util.ArrayList;

public class ArtistPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<AuthorData> datas;

    public ArtistPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        datas = Supplier.getAuthors(context);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", datas.get(position).id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return datas.get(position).name;
    }
}
