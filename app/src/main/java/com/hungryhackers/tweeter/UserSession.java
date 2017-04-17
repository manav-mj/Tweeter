package com.hungryhackers.tweeter;

/**
 * Created by YourFather on 13-04-2017.
 */

public class UserSession {
    String secret;
    String token;
    String userName;
    String userId;

    public UserSession(String secret, String token, String userName, String userId) {
        this.secret = secret;
        this.token = token;
        this.userName = userName;
        this.userId = userId;
    }
}
