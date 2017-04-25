package com.hungryhackers.tweeter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Image;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.internal.TweetMediaView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YourFather on 25-04-2017.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {

    ArrayList<Tweet> mFeedList;
    Context mContext;



    public FeedRecyclerAdapter(Context context, ArrayList<Tweet> feedList) {
        mFeedList = feedList;
        mContext = context;
    }


    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_feed_list_item, parent, false);
        FeedViewHolder viewHolder = new FeedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        Tweet tweet = mFeedList.get(position);
//        holder.tweetMediaView.setTweetMediaEntities(tweet,null);
        User user = tweet.user;
        holder.userName.setText(user.name);
        holder.screenName.setText("@"+user.screenName);
        holder.feedText.setText(tweet.text);

        Log.d("adapter", user.name);

        Picasso.with(mContext).load(user.profileImageUrl.replace("_normal", "")).placeholder(R.drawable.twitter_icon_shadow).into(holder.userImage);
        List<MediaEntity> media = tweet.entities.media;
        if (media != null && !media.isEmpty()) {
            Picasso.with(mContext).load(media.get(0).mediaUrl).into(holder.feedMedia);
        }
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userImage;
        TextView userName, screenName, feedText;
        ImageView feedMedia;

        public FeedViewHolder(View itemView) {
            super(itemView);
            userImage = (CircleImageView) itemView.findViewById(R.id.feed_user_image);
            userName = (TextView) itemView.findViewById(R.id.feed_user_name);
            screenName = (TextView) itemView.findViewById(R.id.feed_user_screen);
            feedText = (TextView) itemView.findViewById(R.id.feed_text);
            feedMedia = (ImageView) itemView.findViewById(R.id.feed_media);

            AssetManager am = mContext.getApplicationContext().getAssets();

            Typeface typeface = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "ProximaNovaSoft-Semibold.otf"));

            userName.setTypeface(typeface);
        }
    }
}
