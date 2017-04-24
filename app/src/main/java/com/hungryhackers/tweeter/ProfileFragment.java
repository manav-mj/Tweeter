package com.hungryhackers.tweeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import developer.shivam.library.CrescentoContainer;
import developer.shivam.library.CrescentoImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by YourFather on 13-04-2017.
 */

public class ProfileFragment extends Fragment {

    CircleImageView profileImage;
    CrescentoContainer coverImage;

    TextView userName;

    TextView logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        coverImage = (CrescentoContainer) v.findViewById(R.id.bg);
        coverImage.setVisibility(View.VISIBLE);
        profileImage = (CircleImageView) v.findViewById(R.id.dp);

        userName = (TextView) v.findViewById(R.id.user_name);

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

//        Typeface typeface = Typeface.createFromFile("fonts/ProximaNovaSoft-Semibold.otf");

        userName.setTypeface(typeface);

        return v;
    }

    public void setProfileImage(){

    }
}
