package com.james.wallpapers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.adapters.ListAdapter;
import com.james.wallpapers.data.WallData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RandomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), metrics.widthPixels > metrics.heightPixels ? 3 : 2));

        ArrayList<WallData> walls = Supplier.getWallpapers(getContext());
        Collections.sort(walls, new Comparator<WallData>() {
            @Override
            public int compare(WallData first, WallData second) {
                //it's called RandomFragment.java but it's not random?
                //whaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAT
                return first.name.compareToIgnoreCase(second.name);
            }
        });

        recycler.setAdapter(new ListAdapter(getActivity(), walls));

        return recycler;
    }
}
