package com.hungryhackers.tweeter.OAuth;

import com.hungryhackers.tweeter.TwitterKeys;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.HashMap;

/**
 * Created by ayush on 2/4/17.
 */

public class GetHeader {

    public String header;
    OAuthClass oAuthClass=new OAuthClass();
    public GetHeader(String URL, String method, HashMap<String,String> querymap, HashMap<String,String> bodymap) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = twitterSession.getAuthToken();

        header=oAuthClass
                .setConsumersecret("8iDHNwoIuORRGiLhssW2KlOLlizIcyolTHYprIo0fd7KpyXNNa")//These 4 should be fetched from Shared Preference
                .setTokensecret("NIN0GUPGRfxOIYPYEIGCmnwCcEgLXlQood0ux9s4pPyEf")
                .setOauth_consumer_key("LbeKxu1IpJ9OfAmDMC4VngLZ4")
                .setOauth_token("2977074438-bKgGkyKydiIrYmjdqttu963SDSoNnTah9JOeqdd")
                .setOauth_signature_method("HMAC-SHA1")
                .setOauth_version("1.0")

                .setOauth_nonce(randomString())//should be a random string everytime
                .setOauth_timestamp(System.currentTimeMillis()/1000+"") //current epoch time
                .setBody(bodymap) //set to null if there is no request body
                .setQuery(querymap)
                .setBaseurl(URL)
                .setMethod(method)
                .getAuthheader();
    }

    public String randomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);

    }
}
