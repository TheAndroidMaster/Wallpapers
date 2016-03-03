package com.james.wallpapers;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IconPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public IconPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new IconFragment();
        Bundle bundle = new Bundle();
        switch(position) {
            case 0:
                bundle.putInt("icon", R.mipmap.icontwo);
                break;
            case 1:
                bundle.putInt("icon", R.mipmap.iconone);
                break;
            case 2:
                bundle.putInt("icon", R.mipmap.iconthree);
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Icon " + position;
    }
}
