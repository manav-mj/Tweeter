package com.hungryhackers.tweeter;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hungryhackers.tweeter.OAuth.GetHeader;
import com.hungryhackers.tweeter.network.ApiClient;
import com.hungryhackers.tweeter.network.ApiInterface;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Locale;
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

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Toolbar toolbar;
    AppBarLayout appBar;

    Boolean expanded = true,collapsed;

    LinearLayout toolbarLinearLayout;
    TextView toolbarName, toolbarScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        toolbarLinearLayout = (LinearLayout) findViewById(R.id.toolbar_name_layout);
        toolbarName = (TextView) findViewById(R.id.toolbar_user_name);
        toolbarScreen = (TextView) findViewById(R.id.toolbar_screen_name);

        AssetManager am = getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "ProximaNovaSoft-Semibold.otf"));

        toolbarName.setTypeface(typeface);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int height = toolbar.getHeight() - appBar.getHeight();
                if (verticalOffset == 0){
                    expanded = true;
                    collapsed = false;
                }
                if(verticalOffset >= height && verticalOffset <= height + 130){
//                    mCollapsingToolbar.setTitle(mCollapsedTitle);
                    Log.i("TAG", height + "onOffsetChanged: " + verticalOffset);
                    getWindow().setStatusBarColor(getColor(R.color.blackBlue));
                    toolbarLinearLayout.setVisibility(View.VISIBLE);
                }else {
                    toolbarLinearLayout.setVisibility(View.INVISIBLE);
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
//                    mCollapsingToolbar.setTitle(mExpandedTitle);
                }

            }
        });

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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.blackBlue));
        tabLayout.setTabTextColors(R.color.lightGreyBlue, R.color.blackBlue);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        user = new UserSession(secret, token, twitterSession.getUserName(), twitterSession.getUserId()+"");

        OAuthSigning oauthSigning = new OAuthSigning(authConfig, authToken);
//        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();

//        Log.i("man", authHeaders.toString());
        Log.d("ConsumerKey", authConfig.getConsumerKey());
        Log.d("ConsumerSecret", authConfig.getConsumerSecret());
        Log.d("Token", token);
        Log.d("TokenSecret", secret);
        Log.d("UserName", twitterSession.getUserName());
        Log.d("UserId", String.valueOf(twitterSession.getUserId()));



        HashMap<String, String> map = new HashMap<>();
        map.put("user_id",user.userId);
        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeaders("GET", "https://api.twitter.com/1.1/users/show.json", map);

        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            Log.i("loop", entry.getKey()+":"+entry.getValue());
        }

//        try {
//            header = new GetHeader("https://api.twitter.com/1.1/users/show.json","GET",map,null).header;
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Log.d("abcd",header);

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<UserDetails> call = apiInterface.getUserDetails(twitterSession.getUserId(), authHeaders.get(HEADER_AUTH_CREDENTIALS));
//        Call<UserDetails> call = apiInterface.getUserDetails(twitterSession.getUserId(), header);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.i("man", call.request().url()+ "  : " + response.code() );

                if (response.isSuccessful()) {
                    userDetails = response.body();
                    userDetails.profile_image_url = userDetails.profile_image_url.replace("_normal","");
                    profileFragment.setProfile(userDetails);
                    toolbarScreen.setText("@"+userDetails.screen_name);
                    toolbarName.setText(userDetails.name);
                }

            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

            }
        });

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MyFeedFragment();
                case 1:
                    return new MessagesFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0:
                    return "My feed";
                case 1:
                    return "Messages";
            }
            return null;
        }
    }
}
