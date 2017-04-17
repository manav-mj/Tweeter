package com.hungryhackers.tweeter.network;

import com.hungryhackers.tweeter.UserDetails;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by YourFather on 19-03-2017.
 */

public interface ApiInterface {

    @GET("users/show.json")
    Call<UserDetails> getUserDetails(@Query("user_id") String id, @Header("Authorization") String header);

    @GET("statuses/user_timeline.json")
    Call<ArrayList<String>> getUserTimeline(@Header("Authorization") String header);
}
