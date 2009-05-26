/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gdata.data.Feed;
import com.google.gdata.util.ServiceException;




public class MainActivity extends Activity 
{
    /** Called when the activity is first created. */
    
	Context cont;
	
	private static final int BLOGCONFIG_REQUEST = 4;
	final static String TAG = "MainActivity";
	private static int CONFIG_ORDER = 0;
	private ProgressDialog viewProgress = null;
	private final String MSG_KEY="value";
	public static Feed resultFeed = null;
	int viewStatus = 0;
	
	
		@Override
	    public void onCreate(Bundle savedInstanceState)
		{
	        super.onCreate(savedInstanceState);
	        Window  w = getWindow(); 
	        w.requestFeature(Window.FEATURE_LEFT_ICON);   
	        setContentView(R.layout.main);
	        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	        
	        Intent i = this.getIntent();
	        if(i.hasExtra("ConfigOrder"))
	        {
	        	CONFIG_ORDER=i.getIntExtra("ConfigOrder", 0);
	        }
	        else
	        {
	        	CONFIG_ORDER=0;
	        }
	        
	        this.findViewById(R.id.ViewContacts).setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View v)
				{        		
					Intent i = new Intent(MainActivity.this, ContactList.class);
					String[] s = {"",""};
					i.putExtra("ConfigOrder", CONFIG_ORDER);
					i.putExtra("PostTitleAndContent", s);
					startActivity(i);
		            finish();
	       		}
			}); 
	        
	        this.findViewById(R.id.Synchronize).setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v)
				{   
					ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(MainActivity.this.CONNECTIVITY_SERVICE); 
					NetworkInfo netInfo = cm.getActiveNetworkInfo();
					         
					if(netInfo == null)
					{
						Toast t = Toast.makeText(getApplicationContext(), "INTERNET CONNECTION UNAVALIABLE", Toast.LENGTH_LONG);;
						t.setGravity(Gravity.CENTER, 0, 0);
						t.show();
					} 
					else
					{
						Intent i = new Intent(MainActivity.this, Synchronize.class);
						String[] s = {"",""};
						i.putExtra("ConfigOrder", CONFIG_ORDER);
						i.putExtra("PostTitleAndContent", s);
						startActivity(i);
						finish();
					}
	       		}
			});
	        
	        this.findViewById(R.id.Settings).setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v)
				{
	       		}
			});
	        
	        this.findViewById(R.id.Exit).setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{   
	                finish();
	       		}
			});
	    }

		public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
		{
			// TODO Auto-generated method stub
			CONFIG_ORDER=position;
		}

		public void onNothingSelected(AdapterView<?> arg0) 
		{
			// TODO Auto-generated method stub	
		}
		
		private void showViewStatus() 
		{
	        viewProgress.dismiss();
	        if(viewStatus != 5) 
	        {
	        	/*Alert.showAlert(this,"View status","View failed! (Code "+viewStatus+")\nTry again.");*/
	        }
	    }
	
}