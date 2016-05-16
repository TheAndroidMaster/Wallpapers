package com.james.wallpapers.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.james.wallpapers.Utils;
import com.james.wallpapers.data.AuthorData;
import com.james.wallpapers.views.CustomImageView;
import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.adapters.ArtistPagerAdapter;
import com.james.wallpapers.data.WallData;

import java.util.ArrayList;
import java.util.Random;


public class WallpaperFragment extends Fragment {

    TabLayout tabLayout;
    CustomImageView header, headerIcon;
    TextView title;

    ArtistPagerAdapter adapter;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallpapers, container, false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        header = (CustomImageView) v.findViewById(R.id.header);
        headerIcon = (CustomImageView) v.findViewById(R.id.headerIcon);
        title = (TextView) v.findViewById(R.id.title);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        tabLayout = (TabLayout) v.findViewById(R.id.tl);

        adapter = new ArtistPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                refresh(position);
            }
        });

        refresh(0);
        return v;
    }

    public void refresh(int position) {
        AuthorData authorData = Supplier.getAuthors(getContext()).get(position);
        title.setText(authorData.name);
        Glide.with(getContext()).load(authorData.image).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                headerIcon.transition(getActivity(), resource);
            }
        });

        ArrayList<WallData> walls = Supplier.getWallpapers(getContext(), position);
        Random rand = new Random();
        Glide.with(getContext()).load(walls.get(rand.nextInt(walls.size())).url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                header.transition(getActivity(), resource);

                Palette.from(Utils.drawableToBitmap(resource)).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        collapsingToolbarLayout.setContentScrimColor(palette.getLightVibrantColor(Color.LTGRAY));

                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{tabLayout.getBackground(), new ColorDrawable(palette.getLightVibrantColor(Color.LTGRAY))});
                        tabLayout.setBackground(td);
                        td.startTransition(250);
                    }
                });
            }
        });
    }
}