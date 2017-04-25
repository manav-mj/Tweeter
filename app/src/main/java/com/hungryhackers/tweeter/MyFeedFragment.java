package com.hungryhackers.tweeter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hungryhackers.tweeter.network.ApiClient;
import com.hungryhackers.tweeter.network.ApiInterface;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders.HEADER_AUTH_CREDENTIALS;

/**
 * Created by YourFather on 25-04-2017.
 */

public class MyFeedFragment extends Fragment {

    LinearLayout feedProgressLayout;
    RecyclerView recyclerView;
    FeedRecyclerAdapter adapter;

    ArrayList<Tweet> feedList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_feed, container, false);

//        feedProgressLayout = (LinearLayout) v.findViewById(R.id.feed_progress_layout);
        recyclerView = (RecyclerView) v.findViewById(R.id.feed_recyclerView);
        adapter = new FeedRecyclerAdapter(getActivity(), feedList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = twitterSession.getAuthToken();
        OAuthSigning oAuthSigning = new OAuthSigning(authConfig, authToken);
        Map<String, String> authHeaders = oAuthSigning.getOAuthEchoHeaders("GET", "https://api.twitter.com/1.1/statuses/home_timeline.json", null);

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Tweet>> call = apiInterface.getHomeTimeline(authHeaders.get(HEADER_AUTH_CREDENTIALS));
        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {
                feedList = response.body();
                adapter = new FeedRecyclerAdapter(getActivity(), feedList);
                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                Log.i("TAG", "onResponse: " + response.code() + " : " + response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {

            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
