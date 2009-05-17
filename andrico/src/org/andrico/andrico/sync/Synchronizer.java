/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico.sync;


import org.andrico.andrico.facebook.FB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Synchronizer {
	
    public boolean mfbExecuteSetStatusMessage;

    private static abstract class FbRunnable implements Runnable {
        final WeakReference<FB> mFacebookWeakRef;
        final WeakReference<UiHandler> mUiHandlerWeakRef;

        private FbRunnable(UiHandler handler, FB facebook) {
            this.mUiHandlerWeakRef = new WeakReference<UiHandler>(handler);
            this.mFacebookWeakRef = new WeakReference<FB>(facebook);
        }

        String getGetFriendsStatusUpdatesFql() {
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null) {
                return null;
            }
            StringBuffer friendQuery = new StringBuffer();
            friendQuery.append(FRIENDS_STATUS_UPDATES_FQL);
            friendQuery.append(facebook.getSession().getUid());
            friendQuery.append(")");
            return friendQuery.toString();
        }
    }

    private static class FbExecuteGetAllDataRunnable extends FbRunnable {

        public FbExecuteGetAllDataRunnable(UiHandler handler, FB facebook) {
            super(handler, facebook);
        }

        public void run() {
            UiHandler handler = mUiHandlerWeakRef.get();
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null || handler == null) {
                return;
            }
            handler.executeMethodForMessage(facebook.users_getMyInfo(), MESSAGE_GET_USER_INFO);
            handler.executeMethodForMessage(facebook.fql_query(getGetFriendsStatusUpdatesFql()),
                    MESSAGE_FRIEND_STATUS_UPDATE);
        }
    }

    private static class FbExecuteGetUserInfoRunnable extends FbRunnable {

        public FbExecuteGetUserInfoRunnable(UiHandler handler, FB facebook) {
            super(handler, facebook);
        }

        public void run() {
            UiHandler handler = mUiHandlerWeakRef.get();
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null || handler == null) {
                return;
            }
            handler.executeMethodForMessage(facebook.users_getMyInfo(), MESSAGE_GET_USER_INFO);
        }
    }

    
    

    private class AndricoHandler extends UiHandler {
        private final Random mRandom = new Random();

        /**
         * S@param context
         * 
         * @param statusBar
         */
        AndricoHandler(Context context, FB facebook) {
            super(context, facebook);
        }
       

        void handleGetUserInfoMessage(Message msg) {
            String result = msg.getData().getString("result");
            Integer errorCode = JsonParser.parseForErrorCode(result);
            if (errorCode != null && errorCode == -1) {
                try {                    
                    JSONObject jsonUserInfo = new JSONArray(result).getJSONObject(0);
                    String firstName = jsonUserInfo.getString("first_name");
                    String lastName = jsonUserInfo.getString("last_name");                 
                    String location = jsonUserInfo.getJSONArray("location").toString();      
                    String birthday = jsonUserInfo.getString("birthday");
                    String birthday_date = jsonUserInfo.getString("birthday_date");
                    String profile_url = jsonUserInfo.getString("profile_url");                    
                    setFacebookUserInfo(new UserInfo(firstName, lastName,
                    		location, birthday, birthday_date, profile_url));
                    return;
                } catch (JSONException e) {
                    errorCode = 0; // Signify that something did go wrong but we
                    // don't know what.
                }
            }

        }  
          
                            
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_GET_USER_INFO:
                    handleGetUserInfoMessage(msg);
                    break;
                default:
                    break;
            }
        }
        
    }

    private static final int DIALOG_SET_STATUS = 0;
    private static final int DIALOG_GET_USER_INFO = 1;
    private static final int DIALOG_SET_STATUS_LOCATION = 2;

    private static final int MENU_PREFS = 1;
    private static final int MENU_REFRESH = 2;
    private static final int MENU_UPLOAD_PICTURE = 3;
    private static final int MENU_MY_LOCATION = 4;

    private static final int MESSAGE_FRIEND_STATUS_UPDATE = 0;
    private static final int MESSAGE_GET_USER_INFO = 1;
    private static final int MESSAGE_SET_STATUS = 3;

    private static final String STATE_KEY_FACEBOOK_USER_INFO = "facebook_user_info";
    private static final String STATE_KEY_FACEBOOK_STATUS_LIST = "facebook_status_list";

    private TextView mAppStatus;
    private EditText mEditStatus;
    private FB mFacebook;
    private Handler mBackgroundHandler;
    private UiHandler mHandler;
    private TextView mIamLabel;
    private MenuItem mRefreshMenuItem;
    private Button mUpdateStatusButton;
    private UserInfo mFacebookUserInfo;
    private static final String FRIENDS_STATUS_UPDATES_FQL = "SELECT uid, name, current_location, birthday, birthday_date, profile_url FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=";
    private BroadcastReceiver mNetworkBroadcastReciever;
    private MenuItem mSharePhotoMenuItem;
    private MenuItem mShareLocationMenuItem;
    private boolean mConnected = true;

    /**
     * @return the mFacebookUserInfo
     */
    public synchronized UserInfo getFacebookUserInfo() {
        return mFacebookUserInfo;
    }

    /**
     * @param facebookUserInfo the mFacebookUserInfo to set
     */
    public synchronized void setFacebookUserInfo(UserInfo facebookUserInfo) {
        mFacebookUserInfo = facebookUserInfo;
    }
  
    
    private void postToBackgroundHandler(Runnable runnable) {
        postToBackgroundHandler(runnable, 0);
    }

    /**
     * @param r
     * @param delayMillis if -1, post at front of queue, for values > 0 post
     *            with a delay.
     */
    private void postToBackgroundHandler(final Runnable r, final int delayMillis) {
        if (mFacebook.getSession() == null) {
            // TODO: go to loginactivity
        	// forceToPreferencesActivity();
            return;
        }
      
        if (delayMillis > 0) {
            mBackgroundHandler.postDelayed(new Runnable() {

                public void run() {
                   
                    mBackgroundHandler.postDelayed(r, delayMillis);
                }
            }, delayMillis);
        } else if (delayMillis == -1) {
            mBackgroundHandler.postAtFrontOfQueue(r);
        } else {
            mBackgroundHandler.post(r);
        }
    }

    public void startSynchronization(Context mContext,FB mFacebook){
        
        mHandler = new AndricoHandler(mContext, mFacebook);
        postToBackgroundHandler(new FbExecuteGetAllDataRunnable(mHandler, mFacebook));
    }
    
   

}

