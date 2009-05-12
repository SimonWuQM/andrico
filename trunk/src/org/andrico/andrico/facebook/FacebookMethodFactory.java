/**
 * Copyright 2008 Joe LaPenna
 */

package org.andrico.andrico.facebook;


import android.util.Log;

import java.util.HashMap;

import org.andrico.andrico.facebook.FacebookBase.Session;

/**
 * @author jlapenna
 */
public class FacebookMethodFactory {
    private String LOG = "FacebookMethodFactory";

    private String mApiKey;
    private String mApiSecret;
    private Session mSession;
    private Session mFacebookSession;

    /**
     * @param cookieDomain
     */
    public FacebookMethodFactory(String cookieDomain, String apiKey, String secret) {
        mApiKey = apiKey;
        mApiSecret = secret;
    }

    public FacebookMethod create(String method, HashMap<String, String> parameters) {
        return create(method, parameters, false);
    }

    public FacebookMethod create(String method, HashMap<String, String> parameters, Boolean session) {
        if (session && mSession == null) {
            throw new RuntimeException(
                    "Unable to associate an established session with new facebook method.");
        }
        String secretParam = (session) ? mSession.getSecret() : mApiSecret;
        String sessionParam = (session) ? mSession.getSession() : null;
        Log.d(LOG, "Using secret: " + secretParam);
        Log.d(LOG, "Using session: " + sessionParam);
        return new FacebookMethod(method, mApiKey, secretParam, sessionParam, parameters);
    }

    public FacebookMethod FacebookMethod(String method) {
        return this.create(method, null);
    }

    /**
     * @return
     */
    public Session getSession() {
        if (mSession == null) {
            return null;
        } else {
            return mFacebookSession;
        }
    }

    public Boolean hasSession() {
        return mSession == null ? false : true;
    }

    /**
     * Using the string returned from the http sesion request, cast it to a JSON
     * object and store it.
     * 
     * @param session
     */
    public void setSession(Session session) {
        mSession = session;
    }

    /**
     * 
     */
    public void unsetSession() {
        mSession = null;
    }

}
