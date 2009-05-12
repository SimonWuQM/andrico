package org.andrico.andrico;

import java.text.ParseException;

import com.google.gdata.data.Feed;
/*import com.googlecode.statusinator2.Preferences;
import com.googlecode.statusinator2.R;
import com.googlecode.statusinator2.PreferenceActivity.FacebookAppPermissionPreferenceClickListener;
import com.googlecode.statusinator2.PreferenceActivity.FacebookLoggedInCheckboxOnPreferenceClickListener;
import com.googlecode.statusinator2.twitter.Twitter;
*/
import org.andrico.andrico.facebook.Facebook;
import org.andrico.andrico.facebook.LoginActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
	/*BlogInterface bi;
	BlogConfig myBlogConfig = null;*/
	private final String TAG = "CreateProfile";
	private int mState = 0;
	private static final int STATE_INSERT = 0;
    private static int CONFIG_ORDER=0;
    
    
    private Context mContext;
    private Facebook mFacebook;
    private SharedPreferences mSettings;
    private static final int FACEBOOK_LOGIN_REQUEST_CODE = 3;
    private static final int FACEBOOK_AUTH_STATUS_REQUEST_CODE = 2;
    private static final String LOG = "PreferenceActivity";
    
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
        /*if(intent.getAction().equals(Intent.ACTION_INSERT)) {
            mState = STATE_INSERT;
            /*myBlogConfig = new BlogConfig();
            myBlogConfig.setPostmethod(BlogConfigBLOGGER.getInterfaceNumberByType(BlogConfigBLOGGER.BlogInterfaceType.BLOGGER));
            myBlogConfig.setPostConfig("");
            if(mState == STATE_INSERT) {
                createConfigDependentFields(myBlogConfig);
            }
        }*/
        
        /*int w = this.getWindow().getWindowManager().getDefaultDisplay().getWidth()-12;
        ((Button)this.findViewById(R.id.BackToMenu)).setWidth(w/3);
        ((Button)this.findViewById(R.id.Save)).setWidth(w/3);
        ((Button)this.findViewById(R.id.FETCH_BUTTON_ID)).setWidth(w/3);
        */
        
        
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
        mContext = this;

        // Load the preferences from an XML resource
        /*this.addPreferencesFromResource(R.xml.preferences);*/
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        mFacebook = new Facebook(getString(R.string.facebook_api_key),
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
        // If Twidroid is installed, enable the preferences option.
        
        
        
        
        
        
        
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
				/*textProfile = (EditText)findViewById(R.id.ProfileName);
		        textUsername = (EditText)findViewById(R.id.Username);
		        textPassword = (EditText)findViewById(R.id.Password);
                if(textPassword == null || textPassword.getText() == null) {
                        Log.d(TAG,"password editor view is null when trying to read!");
                        return;
                }
                if (textUsername == null || textUsername.getText() == null) {
                        Log.d(TAG,"username editor view is null when trying to read!");
                        return;
                }
                if (textProfile == null || textProfile.getText() == null) {
                        Log.d(TAG,"blogname editor view is null when trying to read!");
                        return;
                }
                String blognameStr = textProfile.getText().toString();
                String usernameStr = textUsername.getText().toString();
                String passwordStr = textPassword.getText().toString();
                
                if(blognameStr.length() < 1) {
                        Alert.showAlert(CreateProfile.this,"Empty name of profile","You need to have a name for this profile.");
                        return;
                }       
                if(usernameStr.length() < 1) {
                        Alert.showAlert(CreateProfile.this,"Empty username","You need to have a username for this blog.");
                        return;
                } 
                if(passwordStr.length() < 1) {
                        Alert.showAlert(CreateProfile.this,"Empty password","You need to have a password for this blog.");
                        return;
                } 
                myBlogConfig.setBlogname(textProfile.getText().toString());
                myBlogConfig.setUsername(textUsername.getText().toString());
                myBlogConfig.setPassword(textPassword.getText().toString());
                /*if(bi != null) {
                        CharSequence cs = bi.getConfigEditorData();
                        myBlogConfig.setPostConfig(cs);
                } else {
                        Alert.showAlert(CreateProfile.this,"nag", "nag");
                }*/
                //remember that the post method (i.e. which type of blog this is)
                //is already set by the OnItemSelected callback.
          /*      DBClient conn = new DBClient();
                if(conn != null) {
                        if(mState == STATE_INSERT) {
                                conn.insert(CreateProfile.this,myBlogConfig);
                                Log.d(TAG,"Blog Config saved to database.");
                                Alert.showAlert(CreateProfile.this,"Success","Your profile has been successfully saved.");
                        }/*else if(mState == STATE_EDIT) {
                                conn.update(this,myBlogConfig);
                                Log.d(TAG,"Blog Config with id="+myBlogConfig.getId()+"updated in database.");
                        }*/
                /*} else {
                        Alert.showAlert(CreateProfile.this,"ERROR","Unable to save configuration. Check your device memory.");
                        Log.e(TAG, "ERROR saving blog config to DB due to DBUtil error.");
                }*/
        	}
		});
        
        this.findViewById(R.id.CreateProfile).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				startActivityForResult(mFacebook.createLoginActivityIntent(mContext), FACEBOOK_LOGIN_REQUEST_CODE);
        	}
		});
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG, "onActivityResult");
        switch (requestCode) {
            case FACEBOOK_LOGIN_REQUEST_CODE:
                if (mFacebook.handleLoginActivityResult(this, resultCode, data)) 
                {
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
                } 
                else 
                {
                    Toast.makeText(mContext, "Failure logging in.", Toast.LENGTH_SHORT).show();
                    //unsetUiFacebookLoggedIn();

                    // Wipe the user session.
                    mFacebook.unsetSession();
                    //setPrefsFromFacebookSession();
                }
                break;

            
						
            default:
                break;
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
