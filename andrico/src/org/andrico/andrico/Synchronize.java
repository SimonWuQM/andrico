/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.FB;
import org.andrico.andrico.facebook.LoginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Synchronize extends Activity 
{
	private EditText textProfile, textUsername, textPassword;
	private final String TAG = "CreateProfile";
	private int mState = 0;
	private static final int STATE_INSERT = 0;
    private static int CONFIG_ORDER=0;
    
    
    private Context mContext;
    private FB mFacebook;
    private SharedPreferences mSettings;
    private static final int FACEBOOK_LOGIN_REQUEST_CODE = 3;
    private static final int FACEBOOK_AUTH_STATUS_REQUEST_CODE = 2;
    private static final int MESSAGE_GET_USER_INFO = 1;
    private static final int MESSAGE_FRIEND_STATUS_UPDATE = 0;
    private static final String LOG = "PreferenceActivity";
    private UiHandler mHandler;
    private Handler mBackgroundHandler;
    private UserInfo mFacebookUserInfo;
    private static final String FRIENDS_STATUS_UPDATES_FQL = "SELECT uid, name, current_location, birthday, birthday_date, profile_url FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=";
        
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
            //handler.executeMethodForMessage(facebook.users_getMyInfo(), MESSAGE_GET_USER_INFO);
            handler.executeMethodForMessage(facebook.fql_query(getGetFriendsStatusUpdatesFql()),
                    MESSAGE_FRIEND_STATUS_UPDATE);
        }
    }
    
    private static class FbExecuteGetFriendsStatusUpdatesRunnable extends FbRunnable {

        public FbExecuteGetFriendsStatusUpdatesRunnable(UiHandler handler, FB facebook) {
            super(handler, facebook);
        }

        public void run() {
            UiHandler handler = mUiHandlerWeakRef.get();
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null || handler == null) {
                return;
            }
            handler.executeMethodForMessage(facebook.fql_query(getGetFriendsStatusUpdatesFql()),
                    MESSAGE_FRIEND_STATUS_UPDATE);
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

        void handleGetFriendsStatusUpdatesMessage(Message msg) {
            String result = msg.getData().getString("result");
            Integer errorCode = JsonParser.parseForErrorCode(result);
            if (errorCode != null && errorCode == -1) {
                try {
                    Log.d(LOG, "Setting status list from json result:" + result);
                    JSONArray jsonResult = new JSONArray(result);
                    
                    ///!!! HERE We can Pars Json Response
                    
                    //mListAdapter.setListFromJson(jsonResult);
                    
                    List<Bundle> newList = new Vector<Bundle>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            // Verify we have the things we want, otherwise an exception
                            // will be thrown.`
                            JSONObject obj = jsonResult.getJSONObject(i);
                            JSONObject jsonUserInfo = new JSONArray(result).getJSONObject(i);
                            
                            if (!obj.getJSONObject("first_name").getString("message").equals("")) {
                                Bundle update = new Bundle();
                                update.putString("name", obj.optString("name"));
                                update.putString("status", obj.optJSONObject("status").optString("message"));
                                update.putString("status_id", obj.optJSONObject("status")
                                        .optString("status_id"));
                                update.putString("time", obj.optJSONObject("status").optString("time"));
                                update.putString("uid", obj.optString("uid"));
                                                                
                                update.putString("firstName", jsonUserInfo.getString("first_name"));
                                update.putString("lastName", jsonUserInfo.getString("last_name"));
                                update.putString("location", jsonUserInfo.getJSONArray("location").toString());
                                update.putString("birthday", jsonUserInfo.getString("birthday"));
                                update.putString("birthday_date", jsonUserInfo.getString("birthday_date"));
                                update.putString("profile_url", jsonUserInfo.getString("profile_url"));
                     
                                
                                
                                
                                
                                newList.add(update);
                            }
                        } catch (NullPointerException e) {
                            // if we don't have the things we need for this friend, don't
                            // put it in newList
                        } catch (JSONException e) {
                            // if we don't have the things we need for this friend, don't
                            // put it in newList
                        }
                    }
                    // Sort the status list.
                    Collections.sort(newList, new Comparator<Bundle>() {
                        public int compare(final Bundle object1, final Bundle object2) {
                            // Reverse the comparison.
                            return -1 * object1.getString("time").compareTo(object2.getString("time"));
                        }
                    });

                    // Update the status list.
                  //  setList(newList);
                    
                    
                    
                    // -------------------- end of parsing -------------------------------
                    
                    
                    
                    
                    
                    notifyUser("", false);
                    return;
                } catch (JSONException jsonArrayConversionException) {
                    errorCode = 0; // Signify that something did go wrong but we
                    // don't know what.
                }
            }

            // We're checking for errorCode -1 because if we got an exception
            // above then the errorCode would indicate a success even though we
            // had a failure of some sort.
            handleErrorCode(errorCode);
            if (errorCode == null || errorCode == 1 || errorCode == 103 || errorCode == 104) {
                postToBackgroundHandler(new FbExecuteGetFriendsStatusUpdatesRunnable(mHandler,
                        mFacebook), mRandom.nextInt(10) * 1000 + 1000);
            }

        }

        
       /*
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
        */  
                            
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
             /* 
            	case MESSAGE_GET_USER_INFO:
                    handleGetUserInfoMessage(msg);
                    break;
             */      
                case MESSAGE_FRIEND_STATUS_UPDATE:
                    handleGetFriendsStatusUpdatesMessage(msg);
                    break;
              /*
                case MESSAGE_SET_STATUS:
                    handleSetStatusMessage(msg);
                    break;
              */      
                default:
                    break;
            }
        }
        
    }

    
    // -------------------------------------------------------------------------
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    	Window  w = getWindow(); 
        w.requestFeature(Window.FEATURE_LEFT_ICON);   
        setContentView(R.layout.synchronize);
        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
        final Intent intent = getIntent();
        CONFIG_ORDER=intent.getIntExtra("ConfigOrder", 0);
        
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
        mContext = this;

        // Load the preferences from an XML resource
        /*this.addPreferencesFromResource(R.xml.preferences);*/
 //       mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        mFacebook = new FB(getString(R.string.facebook_api_key),
                getString(R.string.facebook_secret_key));
        // setPrefsFromFakeFacebookSession();

        /*mFacebookLoggedInCheckBox = (CheckBoxPreference)getPreferenceScreen().findPreference(
                Preferences.FACEBOOK_CRED_LOGGED_IN);
        mFacebookPhotosAuthCheckBox = (CheckBoxPreference)getPreferenceScreen().findPreference(
                Preferences.FACEBOOK_CRED_PHOTOS_AUTH);
        mFacebookStatusAuthCheckBox = (CheckBoxPreference)getPreferenceScreen().findPreference(
                Preferences.FACEBOOK_CRED_STATUS_AUTH);

        mFacebookLoggedInCheckBox.setEnabled(true);
        mFacebookLoggedInCheckBox
                .setOnPreferenceClickListener(new FacebookLoggedInCheckboxOnPreferenceClickListener());

        mFacebookPhotosAuthCheckBox
                .setOnPreferenceClickListener(new FacebookAppPermissionPreferenceClickListener(
                        "photo_upload", FACEBOOK_AUTH_PHOTO_REQUEST_CODE));
        mFacebookStatusAuthCheckBox
                .setOnPreferenceClickListener(new FacebookAppPermissionPreferenceClickListener(
                        "status_update", FACEBOOK_AUTH_STATUS_REQUEST_CODE));
*/
        
        
        
               
        
        this.findViewById(R.id.BackToMenu).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				Intent i = new Intent(Synchronize.this,MainActivity.class);
				i.putExtra("ConfigOrder", CONFIG_ORDER);
                startActivity(i);
                finish();
       		}
		}); 
        
        this.findViewById(R.id.LogIn).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
			{
				startActivityForResult(mFacebook.createLoginActivityIntent(mContext), FACEBOOK_LOGIN_REQUEST_CODE);
        	}
		});
        
        this.findViewById(R.id.Synch).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				LinkedList <Contact> friends = null;
				
				//HERE FRIEND INFORMATION MUST BE WROTE TO THE LIST
				
				String getFriendsFQL = getGetFriendsStatusUpdatesFql(mFacebook);
				mHandler = new AndricoHandler(mContext, mFacebook);
				postToBackgroundHandler(new FbExecuteGetAllDataRunnable(mHandler, mFacebook));
				
				DBContact db = new DBContact();
				db.synchronize(Synchronize.this, friends);
				
				Intent i = new Intent(Synchronize.this,MainActivity.class);
	    		i.putExtra("ConfigOrder", CONFIG_ORDER);
	    		startActivity(i);
	            finish();
        	}
		});
    }
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG, "onActivityResult");
        if (mFacebook.handleLoginActivityResult(this, resultCode, data)) 
        {
        	this.findViewById(R.id.Synch).setEnabled(true);
                    //setPrefsFromFacebookSession();
                    // Heh. RPC to the server to make sure the login worked.
                    //verifyFacebookLoggedIn();
                	/*Intent i = new Intent(Synchronize.this, StartSynchronization.class);
					String[] s = {"",""};
					i.putExtra("ConfigOrder", CONFIG_ORDER);
					i.putExtra("PostTitleAndContent", s);
					try
					{
					startActivity(i);
					}
					catch (ActivityNotFoundException e)
					{
						 Log.e(TAG,"Failed to start activity");
					}
		            finish();*/
                	
        } 
        else 
        {
        	Toast.makeText(mContext, "Failure logging in.", Toast.LENGTH_SHORT).show();
            //unsetUiFacebookLoggedIn();

            // Wipe the user session.
            mFacebook.unsetSession();
            //setPrefsFromFacebookSession();
        }            
    }				
        
    
    
    
    
    
    /*private void createConfigDependentFields(BlogConfig bcb) {
    	BlogConfigBLOGGER.BlogInterfaceType blogtype = BlogConfigBLOGGER.getInterfaceTypeByNumber(bcb.getPostmethod());
        bi = BlogInterfaceFactory.getInstance(blogtype);
        bi.setInstanceConfig(myBlogConfig.getPostConfig());
        Button verify = (Button)findViewById(R.id.FETCH_BUTTON_ID);
        if(bi != null) {
                bi.createOnClickListener(this, verify);
        }
    }*/

    public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(Synchronize.this,MainActivity.class);
    		i.putExtra("ConfigOrder", CONFIG_ORDER);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}

    String getGetFriendsStatusUpdatesFql(FB facebook) {
        /*
    	FB facebook = mFacebookWeakRef.get();
        if (facebook == null) {
            return null;
        }
        */
        StringBuffer friendQuery = new StringBuffer();
        friendQuery.append(FRIENDS_STATUS_UPDATES_FQL);
        friendQuery.append(facebook.getSession().getUid());
        friendQuery.append(")");
        return friendQuery.toString();
    }
    
    
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
    
/*    
    private void shortMessage(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        Float horizontalMargin = toast.getHorizontalMargin();
        toast.setMargin(horizontalMargin, getRequestedOrientation());
    }
    
    private void longMessage(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        Float horizontalMargin = toast.getHorizontalMargin();
        toast.setMargin(horizontalMargin, getRequestedOrientation());
        toast.show();  
    }
*/  
}
