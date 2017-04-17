package com.hungryhackers.tweeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

import static com.hungryhackers.tweeter.TwitterKeys.TWITTER_KEY;
import static com.hungryhackers.tweeter.TwitterKeys.TWITTER_SECRET;

public class MainActivity extends AppCompatActivity {



    private static final String TAG = "manav";

    TwitterLoginButton button;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

//        TwitterSession session = Twitter.getSessionManager().getActiveSession();
//        sp = getSharedPreferences("Tweeter", MODE_PRIVATE);
//        editor = sp.edit();
//        if (session != null) {
//            if (sp.getBoolean("isLoggedIn", false)){
//                Intent i = new Intent(MainActivity.this, ScrollingActivity.class);
//                startActivity(i);
//                finish();
//            }else {
//                editor.putBoolean("isLoggedIn",true);
//                editor.commit();
//            }
//        }

        setContentView(R.layout.activity_main);

        button = (TwitterLoginButton) findViewById(R.id.login_button);
        button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Intent i = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(i);
//                editor.putBoolean("isLoggedIn",true);
//                editor.commit();
                finish();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        button.onActivityResult(requestCode, resultCode, data);
    }
}
