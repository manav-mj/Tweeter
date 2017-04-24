package com.hungryhackers.tweeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

import static com.hungryhackers.tweeter.TwitterKeys.TWITTER_KEY;
import static com.hungryhackers.tweeter.TwitterKeys.TWITTER_SECRET;

public class MainActivity extends AppCompatActivity {



    private static final String TAG = "manav";

    TwitterLoginButton button;

    LinearLayout loginButton;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    ImageView twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        sp = getSharedPreferences("Tweeter", MODE_PRIVATE);
        editor = sp.edit();
        if (session != null) {
            if (sp.getBoolean("isLoggedIn", false)){
                Intent i = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(i);
                finish();
            }else {
                editor.putBoolean("isLoggedIn",true);
                editor.commit();
            }
        }

        setContentView(R.layout.activity_main);

        twitter = (ImageView) findViewById(R.id.twitter);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.twitter_fade_in);
        twitter.startAnimation(fadeIn);

        button = (TwitterLoginButton) findViewById(R.id.login_button);
        button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Intent i = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(i);
                editor.putBoolean("isLoggedIn",true);
                editor.commit();
                finish();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

        loginButton = (LinearLayout) findViewById(R.id.login_button_layout);
        Animation popIn = AnimationUtils.loadAnimation(this, R.anim.login_in);
        loginButton.startAnimation(popIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.performClick();
                Log.i(TAG, "onClick: ");

//                Twitter.logIn(MainActivity.this, new Callback<TwitterSession>() {
//                    @Override
//                    public void success(Result<TwitterSession> result) {
//                        Intent i = new Intent(MainActivity.this, ScrollingActivity.class);
//                        startActivity(i);
//                        editor.putBoolean("isLoggedIn",true);
//                        editor.commit();
//                        finish();
//
//                        Log.i(TAG, "success: ");
//
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        Log.i(TAG, "failure: ");
//                    }
//                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        button.onActivityResult(requestCode, resultCode, data);
    }
}
