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
import java.util.HashMap;
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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
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
    private SharedPreferences SharedPreferences;
    
    private static final int DIALOG_SET_STATUS = 0;
    private static final int FACEBOOK_LOGIN_REQUEST_CODE = 3;
    private static final int FACEBOOK_AUTH_STATUS_REQUEST_CODE = 2;
    private static final int MESSAGE_GET_USER_INFO = 1;
    private static final int MESSAGE_SET_STATUS = 3;
    private static final int MESSAGE_FRIEND_STATUS_UPDATE = 0;
    private static final String LOG = "PreferenceActivity";
    private UiHandler mHandler;
    private Handler mBackgroundHandler;
    private UserInfo mFacebookUserInfo;
    private static final String FRIENDS_STATUS_UPDATES_FQL = "SELECT uid, name, first_name, last_name, " +
    					"current_location, birthday, birthday_date, profile_url FROM user WHERE uid IN " +
    					"(SELECT uid2 FROM friend WHERE uid1=";
    private List<Bundle> newList;
    private Boolean creatingList;
    
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
                    
                    newList = new Vector<Bundle>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            // Verify we have the things we want, otherwise an exception
                            // will be thrown.`
                            JSONObject obj = jsonResult.getJSONObject(i);
                      //      JSONObject jsonUserInfo = new JSONArray(result).getJSONObject(i);
                            
                          /*  if (!obj.getJSONObject("first_name").getString("message").equals("")) {*/
                                Bundle update = new Bundle();
                               /*
                                update.putString("name", obj.optString("name"));
                                update.putString("status", obj.optJSONObject("status").optString("message"));
                                update.putString("status_id", obj.optJSONObject("status")
                                        .optString("status_id"));
                                update.putString("time", obj.optJSONObject("status").optString("time"));
                                */    
                                
                                update.putString("name", obj.optString("name"));
                                update.putString("birthday", obj.optString("birthday"));
                                update.putString("birthdayDate", obj.optString("birthday_date"));
                                update.putString("profileUrl", obj.optString("profile_url"));
                                 
                                update.putString("firstName",obj.optString("first_name"));
                                update.putString("lastName", obj.optString("last_name"));
                                update.putString("location_zip", (String)obj.optJSONObject("current_location").get("zip"));
                                if (obj.optJSONObject("current_location").has("country"))
                                {
                                	update.putString("location_country", (String)obj.optJSONObject("current_location").get("country"));
                                }
                                
                                if (obj.optJSONObject("current_location").has("state"))
                                {
                                	update.putString("location_state", (String)obj.optJSONObject("current_location").get("state"));
                                }
                                
                                if (obj.optJSONObject("current_location").has("city"))
                                {
                                	update.putString("location_city", (String)obj.optJSONObject("current_location").get("city"));
                                }
                                
                                update.putString("uid", obj.optString("uid"));
                                
                                // update.putString("location2", obj.optJSONArray("location"));
                              
                                                                                                                              
                                newList.add(update);
                                
                                
                                /*}*/
                        } catch (NullPointerException e) {
                            // if we don't have the things we need for this friend, don't
                            // put it in newList
                        } catch (JSONException e) {
                            // if we don't have the things we need for this friend, don't
                            // put it in newList
                        }
                    }
                    
                    
                    
                    
                    
                    
                    
                    LinkedList <Contact> friends = new LinkedList<Contact>();
    				DBContact db = new DBContact();
    				try
    				{
    					int size = newList.size();
    				
    					for(int i = 0; i<size; i++)
    					{
    						Bundle bundContact = null;
    						bundContact = newList.get(i);
    						String adress = createAdress(bundContact.getString("location_zip"), 
    								bundContact.getString("location_country"), 
    								bundContact.getString("location_state"),
    								bundContact.getString("location_city"));
    								
    						Contact contact = new Contact();
    					
    						contact.setAdress(adress);
    						contact.setDateOfBirth(bundContact.getString("birthday"));
    						contact.setFBid(bundContact.getString("uid"));
    						contact.setName(bundContact.getString("firstName"));
    						contact.setSecondName(bundContact.getString("lastName"));
    						contact.setPage(bundContact.getString("profileUrl"));
    					
    						friends.add(contact);
    					}
    				
    					db.synchronize(Synchronize.this, friends);
    						
    					dismissDialog(DIALOG_SET_STATUS);
    					Toast t = Toast.makeText(getApplicationContext(), "SYNCHRONIZATION COMPLETE",Toast.LENGTH_SHORT);
    					t.setGravity(Gravity.CENTER, 0, 0);
    					t.show();
    					
    					Intent i = new Intent(Synchronize.this,MainActivity.class);
    					i.putExtra("ConfigOrder", CONFIG_ORDER);
    					startActivity(i);
    					finish();
    				}
    				catch (NullPointerException e)
    				{
    					Log.e(TAG,"Failed to synch");
    					Toast t = Toast.makeText(getApplicationContext(), "FAILURE SYNCHRONIZING",Toast.LENGTH_LONG);
    					t.setGravity(Gravity.CENTER, 0, 0);
    					t.show();
    					
    					creatingList = false;
    				}
                    
                    
                    
                    // Sort the status list.
                  /*  
                    Collections.sort(newList, new Comparator<Bundle>() {
                        public int compare(final Bundle object1, final Bundle object2) {
                            // Reverse the comparison.
                            return -1 * object1.getString("time").compareTo(object2.getString("time"));
                        }
                    });
				*/
                    // Update the status list.
                  //  setList(newList);
                    
                    
                    
                    // -------------------- end of parsing -------------------------------
                    
                    
                    
                    
                    
                    notifyUser("", false);
                    return;
                } 
                catch (JSONException jsonArrayConversionException) 
                {
                    errorCode = 0; 
                    
                    Toast t = Toast.makeText(getApplicationContext(), "FAILURE SYNCHRONIZING",Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER ,0, 0);
                    t.show();
					creatingList = false;
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
            
            creatingList = false;
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
        creatingList = false;

        // Load the preferences from an XML resource
        /*this.addPreferencesFromResource(R.xml.preferences);*/
 //       mSettings = PreferenceManager.getDefaultSharedPreferences(this);

       // mFacebook = new FB(getString(R.string.facebook_api_key),
         //       getString(R.string.facebook_secret_key));
        
        SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
       
        mFacebook = new FB(getString(R.string.facebook_api_key),
                getString(R.string.facebook_secret_key));
       
        
        mFacebook.setSession(
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_SESSION_KEY, "facebook_cred_session_key"),
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_SECRET, "facebook_cred_secret"), 
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_UID, "facebook_cred_uid"));
        
        mHandler = new AndricoHandler(/*mContext*/ getApplicationContext(), mFacebook);
        
        if (SharedPreferences.getString(Preferences.FACEBOOK_CRED_SESSION_KEY, "facebook_cred_session_key") != "facebook_cred_session_key")
        {
        	this.findViewById(R.id.Synch).setEnabled(true);
        }
        else
        {
        	this.findViewById(R.id.Synch).setEnabled(false);
        }
       
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
				ConnectivityManager cm = (ConnectivityManager) Synchronize.this.getSystemService(Synchronize.this.CONNECTIVITY_SERVICE); 
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				         
				if(netInfo.getState() != NetworkInfo.State.CONNECTED)
				{
					Toast t = Toast.makeText(getApplicationContext(), "INTERNET CONNECTION UNAVALIABLE", Toast.LENGTH_LONG);;
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
				}
				else
				{
					showDialog(DIALOG_SET_STATUS);
					
					buildBackgroundHandler();
					postToBackgroundHandler(new FbExecuteGetAllDataRunnable(mHandler, mFacebook));
				}
        	}
		});
    }
    
    
    
    
	private void setPrefsFromFacebookSession() 
	{
        SharedPreferences.Editor editor = SharedPreferences.edit();

        if (mFacebook.getSession() != null) 
        {
            Log.d(LOG, "Saving session to preferences.");
            FB.Session session = mFacebook.getSession();
            editor.putString(Preferences.FACEBOOK_CRED_SESSION_KEY, session.getSession());
            editor.putString(Preferences.FACEBOOK_CRED_SECRET, session.getSecret());
            editor.putString(Preferences.FACEBOOK_CRED_UID, session.getUid());
        } 
        else 
        {
            editor.remove(Preferences.FACEBOOK_CRED_SESSION_KEY);
            editor.remove(Preferences.FACEBOOK_CRED_SECRET);
            editor.remove(Preferences.FACEBOOK_CRED_UID);
        }
        editor.commit();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG, "onActivityResult");
        if (mFacebook.handleLoginActivityResult(this, resultCode, data)) 
        {
        	setPrefsFromFacebookSession();
        	this.findViewById(R.id.Synch).setEnabled(true);
                    

        	/*
        			//setPrefsFromFacebookSession();
                    // Heh. RPC to the server to make sure the login worked.
                    //verifyFacebookLoggedIn();
                	Intent i = new Intent(Synchronize.this, StartSynchronization.class);
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
		            finish();
		            */
                	
        } 
        else 
        {
        	Toast t = Toast.makeText(mContext, "FAILURE LOGGING IN", Toast.LENGTH_LONG);
        	t.setGravity(Gravity.CENTER, 0, 0);
        	t.show();
            
        	this.findViewById(R.id.Synch).setEnabled(false);
            
        	//unsetUiFacebookLoggedIn();

            // Wipe the user session.
            mFacebook.unsetSession();
            SharedPreferences.Editor editor = SharedPreferences.edit();
            editor.remove(Preferences.FACEBOOK_CRED_SESSION_KEY);
            editor.remove(Preferences.FACEBOOK_CRED_SECRET);
            editor.remove(Preferences.FACEBOOK_CRED_UID);
            editor.commit();
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
    
    private void buildBackgroundHandler() {
        // Start up the thread running fb requests. Note that we create a
        // separate thread because we don't want to block.
        HandlerThread thread = new HandlerThread(LOG, Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper fbLooper = thread.getLooper();
        mBackgroundHandler = new Handler(fbLooper);
    }
    
    String createAdress(String zip, String country, String state, String city)
    {
    	String adr = "";
    	
    	if (zip != "" && zip != null)
    	{
    		adr = zip;
    	}
    	
    	if (country != "" && country != null)
    	{
    		if (adr != "")
    		{
    			adr = adr + ", " + country;
    		}
    		else
    		{
    			adr = country;
    		}
    	}
    	
    	if (state != "" && state != null)
    	{
    		if (adr != "")
    		{
    			adr = adr + ", " + state;
    		}
    		else
    		{
    			adr = state;
    		}
    	}
    	
    	if (city != "" && city != null)
    	{
    		if (city != "")
    		{
    			adr = adr + ", " + city;
    		}
    		else
    		{
    			adr = city;
    		}
    	}
    	
    	return adr;
    }
    
    
    // Creating ProgressBar Dialog
    
    @Override
    public Dialog onCreateDialog(int id) {        
        ProgressDialog dialog = new ProgressDialog(this);
        switch (id) {
            case DIALOG_SET_STATUS:
                dialog.setTitle("SYNCHRONIZING");
                dialog.setIndeterminate(true);
                
                return dialog;
            /*    
            case DIALOG_GET_USER_INFO:
                dialog.setTitle("Retrieving Status");
                dialog.setIndeterminate(true);
                dialog.setMessage("Looking up your current status.");
                return dialog;
            case DIALOG_SET_STATUS_LOCATION:
                dialog.setTitle("Update Status with Location");
                return dialog;
            */    
        }
        return null;
    }

    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_SET_STATUS:
            	            	               
            	((ProgressDialog)dialog).setMessage("LOADING FRIENDS INFO");
                        
                break;
            /*
            case DIALOG_SET_STATUS_LOCATION:
                ((ProgressDialog)dialog).setMessage("Setting status: "
                        + mEditStatus.getText().toString());
                break;
            */    
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
