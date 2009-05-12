/************************************
 * Andrico Team Copyright 2009      *
 * http://code.google.com/p/andrico *
 ************************************/

package org.andrico.andrico.facebook;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Facebook extends FacebookBase {
    final static String LOG = "Facebook";

    /**
     * @param api
     * @param secret
     */
    public Facebook(String api, String secret) {
        super(api, secret);
    }

    /**
     * Create a method that requests an authorization token to pass to the
     * facebook login page.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       createToken
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @return
     */
    public FacebookMethod auth_createToken() {
        Log.d(LOG, "facebook.auth.createToken");

        // HashMap parameters =
        // getRequestParameters("facebook.auth.createToken");
        return getMethodFactory().FacebookMethod("facebook.auth.createToken");
    }

    /**
     * Create a FacebookMethod that expires a session object.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       getSession
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @param authToken
     * @return
     */
    public FacebookMethod auth_expireSession() {
        Log.d(LOG, "facebook.auth.expireSession");

        return getMethodFactory().create("facebook.auth.expireSession", null);
    }

    /**
     * Create a FacebookMethod that requests a session object. This should be
     * called after the user has received an auth token and logged into
     * facebook.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       getSession
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @param authToken
     * @return
     */
    public FacebookMethod auth_getSession(String authToken) {
        Log.d(LOG, "facebook.auth.getSession");
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("auth_token", authToken);

        return getMethodFactory().create("facebook.auth.getSession", parameters);
    }

    /**
     * Create a method to get events of a particular user.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=events
     *       .get
     * @param uid The uid to gather events for.
     * @return
     */
    public FacebookMethod events_get(String uid) {
        Log.d(LOG, "Creating facebook.events.get method.");

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("uid", uid);

        return getMethodFactory().create("facebook.events.get", parameters, true);
    }

    /**
     * Post a user action.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=feed.
     *       publishActionOfUser
     * @param title The title of the action in the mini-feed
     * @param body The body text of the action in the mini-feed
     * @param images ArrayList.ArrayList A list at most four elements in size,
     *            each element a list having the image's url and optional link.
     * @return
     */
    public FacebookMethod feed_publishActionOfUser(String title, String body,
            ArrayList<ArrayList<String>> images) {
        Log.e(LOG, "Posting Action");

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("title", title);
        parameters.put("body", body);

        images = (images == null) ? new ArrayList<ArrayList<String>>() : images;
        int imagesSize = images.size();
        for (int i = 0; i < imagesSize; i++) {
            ArrayList<String> image = images.get(i);
            String param = "image_" + Integer.toString(i);
            parameters.put(param, image.get(0));
            if (image.size() > 1) {
                parameters.put(param + "_link", image.get(1));
            }
        }
        return getMethodFactory().create("facebook.feed.publishActionOfUser", parameters, true);
    }

    /**
     * Create a method to query with the "Facebook Query Language"
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=fql.query
     * @param query A FBL query to send.
     * @return
     */
    public FacebookMethod fql_query(String query) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("query", query);

        return getMethodFactory().create("facebook.fql.query", parameters, true);
    }

    /**
     * Creates and returns a new album owned by the current session user.
     * 
     * @param name
     * @param description optional
     * @param location optional
     * @param visible if provided, must be one of: friends, friends-of-friends, networks or everyone 
     * @return
     */
/*
    public FacebookMethod photos_createAlbum(String name, String description, String location,
            String visible) {
        Log.e(LOG, "Getting Albums");
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", name);
        
        if (description != null) {
            parameters.put("description", description);
        }
        
        if (location != null) {
            parameters.put("aids", location);
        }
        
        if (visible != null) {
            if (visible != "friends" && visible != "friends-of-friends" && visible != "networks"
                    && visible != "everyone") {
                throw new IllegalArgumentException("visible parameter not a valid value");
            }
            parameters.put("visible", visible);
        }
        return getMethodFactory().create("facebook.photos.createAlbum", parameters, true);
    }
*/
    /**
     * Get a list of albums
     * http://wiki.developers.facebook.com/index.php/Photos.getAlbums
     * 
     * @param uid
     * @param aids
     * @return
     */
/* 
    public FacebookMethod photos_getAlbums(String uid, String aids) {
        Log.e(LOG, "Getting Albums");
        HashMap<String, String> parameters = new HashMap<String, String>();
        if (uid != null) {
            parameters.put("uid", uid);
        } else if (aids != null) {
            parameters.put("aids", aids);
        }
        return getMethodFactory().create("facebook.photos.getAlbums", parameters, true);
    }
*/
    /**
     * Create a method to post a photo to facebook.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=photos
     *       .upload
     * @param data binary data of a jpg.
     * @param aid Album Id of the album this photo should be associated with. if
     *            null, it is added to a default album.
     * @param caption An optional caption to add to the photo.
     * @return
     */
/*    
    public FacebookMethod photos_upload(byte[] data, String aid, String caption) {
        Log.e(LOG, "Uploading photo");
        HashMap<String, String> parameters = new HashMap<String, String>();

        if (aid != null)
            parameters.put("aid", String.valueOf(aid));
        if (caption != null)
            parameters.put("caption", caption);

        FacebookMethod m = getMethodFactory().create("facebook.photos.upload", parameters, true);
        m.setData(data, "somefilename.jpg", "image/jpg");
        return m;
    }
*/
    /**
     * Create a method to get status,first_name,last_name from the session's
     * user.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=users.
     *       getInfo
     * @return
     */
 
    public FacebookMethod users_getInfo() {
        return users_getInfo(getSession().getUid(), "status,first_name,last_name");
    }

    /**
     * Create a method to get information about a user or users.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=get.
     *       userInfo
     * @param uids String The users ids, comma-seperated.
     * @param fields String The fields to get, comma-seperated.
     * @return
     */
    public FacebookMethod users_getInfo(String uids, String fields) {
        Log.d(LOG, "Creating facebook.users.getInfo method.");

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("uids", uids);
        parameters.put("fields", fields);

        return getMethodFactory().create("facebook.users.getInfo", parameters, true);
    }

    /**
     * @param extPerm Must be one of email, offline_access, status_update,
     *            photo_upload, create_listing, create_event, rsvp_event, sms
     * @return
     */
    public FacebookMethod users_hasAppPermission(String extPerm) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("ext_perm", extPerm);
        return getMethodFactory().create("facebook.users.hasAppPermission", parameters, true);
    }

    /**
     * Create a method to set the session's uid's status.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=users.
     *       setStatus
     * @param status The status to set.
     * @return
     */
    public FacebookMethod users_setStatus(String status, Boolean includesVerb) {
        Log.e(LOG, "Setting Facebook Status: " + status);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("status", status);
        parameters.put("status_includes_verb", includesVerb.toString());
        return getMethodFactory().create("facebook.users.setStatus", parameters, true);
    }

}
