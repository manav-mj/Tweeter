package com.hungryhackers.tweeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import developer.shivam.library.CrescentoContainer;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YourFather on 13-04-2017.
 */

public class ProfileFragment extends Fragment {

    CircleImageView profileImage;
    CrescentoContainer coverImageContainer;
    ImageView coverImage;

    TextView userName;
    TextView screenName;

    TextView logout;

    TextView followers, following;
    LinearLayout progressBarLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBarLayout = (LinearLayout) v.findViewById(R.id.profile_progress_layout);
        progressBarLayout.setVisibility(View.VISIBLE);

        coverImageContainer = (CrescentoContainer) v.findViewById(R.id.bg);
        coverImageContainer.setVisibility(View.VISIBLE);
        coverImage = (ImageView) v.findViewById(R.id.cover_image);

        profileImage = (CircleImageView) v.findViewById(R.id.dp);

        userName = (TextView) v.findViewById(R.id.user_name);
        screenName = (TextView) v.findViewById(R.id.screen_name);

        logout = (TextView) v.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("Tweeter", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().commit();
                Twitter.logOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        AssetManager am = getActivity().getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "ProximaNovaSoft-Semibold.otf"));

        userName.setTypeface(typeface);

        following = (TextView) v.findViewById(R.id.following_count);
        followers = (TextView) v.findViewById(R.id.followers_count);

        return v;
    }

    public void setProfile(UserDetails userDetails){

        Picasso.with(getActivity()).load(userDetails.profile_image_url).into(profileImage);
        Picasso.with(getActivity()).load(userDetails.profile_banner_url).noFade().into(coverImage);
        userName.setText(userDetails.name);
        screenName.setText("@"+userDetails.screen_name);
        followers.setText(userDetails.followers_count + "\nfollowers");
        following.setText(userDetails.friends_count + "\nfollowing");
        progressBarLayout.setVisibility(View.GONE);
    }
}
