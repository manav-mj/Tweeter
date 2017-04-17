package com.hungryhackers.tweeter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hungryhackers.tweeter.OAuth.GetHeader;
import com.hungryhackers.tweeter.network.ApiClient;
import com.hungryhackers.tweeter.network.ApiInterface;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders.HEADER_AUTH_CREDENTIALS;

public class ScrollingActivity extends AppCompatActivity {

    String token, secret;

    UserSession user;

    UserDetails userDetails;

    String header;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = twitterSession.getAuthToken();

        profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profile_fragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        token = authToken.token;
        secret = authToken.secret;

        user = new UserSession(secret, token, twitterSession.getUserName(), twitterSession.getUserId()+"");

        OAuthSigning oauthSigning = new OAuthSigning(authConfig, authToken);
        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();

        Log.i("man", authHeaders.toString());
        Log.d("ConsumerKey", authConfig.getConsumerKey());
        Log.d("ConsumerSecret", authConfig.getConsumerSecret());
        Log.d("Token", token);
        Log.d("TokenSecret", secret);
        Log.d("UserName", twitterSession.getUserName());
        Log.d("UserId", String.valueOf(twitterSession.getUserId()));

        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            Log.i("loop", entry.getKey()+":"+entry.getValue());
        }



        try {
            header = new GetHeader("https://api.twitter.com/1.1/users/show.json","GET",null,null).header;
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<UserDetails> call = apiInterface.getUserDetails(user.userId, authHeaders.get(HEADER_AUTH_CREDENTIALS));
//        Call<UserDetails> call = apiInterface.getUserDetails(user.userId, header);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.i("man", call.request().url()+ "  : " + response.code() );

//                if (response.isSuccessful()) {
//                    userDetails = response.body();
//                    Picasso.with(ScrollingActivity.this).load(userDetails.profile_image_url).into(profileImage);
//                    Picasso.with(ScrollingActivity.this).load(userDetails.profile_banner_url).into(coverImage);
//                }

            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

            }
        });

//        // TODO: Use a more specific parent
//        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
//        // TODO: Base this Tweet ID on some data from elsewhere in your app
//        long tweetId = 631879971628183552L;
//        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                TweetView tweetView = new TweetView(EmbeddedTweetActivity.this, result.data);
//                parentView.addView(tweetView);
//            }
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("TwitterKit", "Load Tweet failure", exception);
//            }
//        });


    }
}
