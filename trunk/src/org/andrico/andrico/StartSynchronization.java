package org.andrico.andrico;

import org.andrico.andrico.facebook.Facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

public class StartSynchronization extends Activity 
{
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
        setContentView(R.layout.start_synchronization);
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
        
        
        
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
    
    

    
        this.findViewById(R.id.ToMenu).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(StartSynchronization.this,MainActivity.class);
        		i.putExtra("ConfigOrder", CONFIG_ORDER);
            	startActivity(i);
        		finish();
        	}
        });
        
        this.findViewById(R.id.StartSynch).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        	
        	}
        });
    }
}