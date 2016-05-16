package com.james.wallpapers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.wallpapers.R;
import com.james.wallpapers.Supplier;
import com.james.wallpapers.adapters.ListAdapter;

public class ListFragment extends Fragment {

    RecyclerView recycler;
    int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       recycler = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        id = getArguments().getInt("id");
        if (id == -1) recycler.setAdapter(new ListAdapter(getActivity(), Supplier.getWallpapers(getContext())));
        else recycler.setAdapter(new ListAdapter(getActivity(), Supplier.getWallpapers(getContext(), id)));
        return recycler;
    }
}
