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

public class FavFragment extends Fragment {

    ArrayList<WallData> walls;
    RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_fav, container, false);

        walls = new ArrayList<>();
        for (WallData data : Supplier.getWallpapers(getContext())) {
            if (data.favorite) walls.add(data);
        }

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        ListAdapter adapter = new ListAdapter(getActivity(), walls);
        adapter.setLayoutMode(ListAdapter.LAYOUT_MODE_COMPLEX);
        recycler.setAdapter(adapter);

        return recycler;
    }
}
