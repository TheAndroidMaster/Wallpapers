package com.james.wallpapers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IconPagerAdapter extends FragmentPagerAdapter {

    IconFragment[] fragments = new IconFragment[3];

    public IconPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        Bundle bundle0 = new Bundle();
        bundle0.putInt("icon", R.mipmap.icontwo);
        fragments[0] = new IconFragment();
        fragments[0].setArguments(bundle0);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("icon", R.mipmap.iconone);
        fragments[1] = new IconFragment();
        fragments[1].setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("icon", R.mipmap.iconthree);
        fragments[2] = new IconFragment();
        fragments[2].setArguments(bundle2);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void onScroll(int position) {
        fragments[0].selected(false);
        fragments[1].selected(false);
        fragments[2].selected(false);
        fragments[position].selected(true);
    }
}
