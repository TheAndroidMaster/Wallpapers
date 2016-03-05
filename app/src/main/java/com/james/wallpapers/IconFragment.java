package com.james.wallpapers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class IconFragment extends Fragment {

    ImageView frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frag = (ImageView) inflater.inflate(R.layout.icon_item, container, false);
        Bundle bundle = getArguments();
        int icon = bundle.getInt("icon");
        frag.setImageResource(icon);
        return frag;
    }

    public void selected(boolean selected) {
        if (frag != null && selected) frag.animate().alpha(1.0f).setInterpolator(new AccelerateInterpolator()).setDuration(250).start();
        else if (frag != null) frag.animate().alpha(0.5f).setInterpolator(new DecelerateInterpolator()).setDuration(250).start();
    }
}