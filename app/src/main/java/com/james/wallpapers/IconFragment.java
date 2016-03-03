package com.james.wallpapers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class IconFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView frag = (ImageView) inflater.inflate(R.layout.icon_item, container, false);
        Bundle bundle = getArguments();
        int icon = bundle.getInt("icon");
        frag.setImageResource(icon);
        return frag;
    }
}