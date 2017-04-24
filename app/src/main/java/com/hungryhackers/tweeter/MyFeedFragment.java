package com.hungryhackers.tweeter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by YourFather on 25-04-2017.
 */

public class MyFeedFragment extends Fragment {

    LinearLayout feedProgressLayout;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_feed, container, false);

        feedProgressLayout = (LinearLayout) v.findViewById(R.id.feed_progress_layout);
        recyclerView = (RecyclerView) v.findViewById(R.id.feed_recyclerView);
        return v;
    }
}
