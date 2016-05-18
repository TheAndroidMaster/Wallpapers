package com.james.wallpapers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.adapters.ListAdapter;
import com.james.wallpapers.data.WallData;

import java.util.ArrayList;
import java.util.Random;

public class FeaturedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        ArrayList<WallData> totalWalls = ((Supplier) getContext().getApplicationContext()).getWallpapers(getContext());
        ArrayList<WallData> walls = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < Math.min(10, totalWalls.size()); i++) {
            WallData data;
            do data = totalWalls.get(rand.nextInt(totalWalls.size()));
            while (data == null || walls.contains(data));
            walls.add(data);
        }

        ListAdapter adapter = new ListAdapter(getActivity(), walls);
        adapter.setLayoutMode(ListAdapter.LAYOUT_MODE_COMPLEX);
        recycler.setAdapter(adapter);

        return recycler;
    }
}
