package com.james.wallpapers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewFragment extends Fragment {
    Bundle bundle;
    View frag;
    RecyclerView recyclerView;
    boolean shown;
    ListAdapter list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.layout_fragment, container, false);
        bundle = getArguments();
        refresh(bundle.getInt("names"), bundle.getInt("urls"), bundle.getString("tabname"));
        return frag;
    }

    public void refresh(int name, int url, String tabname){
        recyclerView = (RecyclerView) frag.findViewById(R.id.my_recycler_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        list = new ListAdapter(name, url, getActivity(), tabname);
        recyclerView.setAdapter(list);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && list != null) list.close();
    }
}
