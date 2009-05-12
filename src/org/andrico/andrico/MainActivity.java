/************************************
 * Andrico Team Copyright 2009      *
 * http://code.google.com/p/andrico *
 ************************************/

package org.andrico.andrico;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

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
	/*private BlogConfig config = null;
	private LinkedList<BlogConfig> configs = null;*/
	
	
	
		@Override
	    public void onCreate(Bundle savedInstanceState)
		{
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_RIGHT_ICON);
	        setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON,R.drawable.ic_andrico);
	        setContentView(R.layout.main);
	        
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
	        		
					/*Intent i = new Intent(MainActivity.this,CreateProfile.class);
	                i.setAction(Intent.ACTION_INSERT);
	                i.putExtra("ConfigOrder", CONFIG_ORDER);
	                Uri cURI = null;
	                cURI = Uri.parse("content://com/sadko/androblogger/blogconfig");
	                i.setData(cURI);
	                startActivityForResult(i,BLOGCONFIG_REQUEST);
	                finish();*/
	       		}
			}); 
	        
	        this.findViewById(R.id.Synchronize).setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v)
				{        		
					Intent i = new Intent(MainActivity.this, Synchronize.class);
					String[] s = {"",""};
					i.putExtra("ConfigOrder", CONFIG_ORDER);
					i.putExtra("PostTitleAndContent", s);
					startActivity(i);
		            finish();
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

		/*protected void viewBlogPosts() {
			viewProgress = ProgressDialog.show(MainActivity.this, "Viewing blog entries", "Starting to view blog entries...");

			Thread viewThread = new Thread () {
	        	public void run() {
	        		Bundle status = new Bundle();
	                mHandler.getLooper();
					Looper.prepare();
	                Message statusMsg = mHandler.obtainMessage();
	                viewStatus = 0;
	                status.putString(MSG_KEY, "1");
	                statusMsg.setData(status);
	                mHandler.sendMessage(statusMsg);
	                boolean viewOk = false;
	        		DBClient db = new DBClient();
	        		LinkedList<BlogConfig> configList = db.getBlogNames(MainActivity.this);
	        		config = db.getBlogConfigById(MainActivity.this, configList.get(CONFIG_ORDER).getId());
	        		String username = config.getUsername();
	        		String password = config.getPassword();
	        		BlogInterface blogapi = null;
	        	    BlogConfigBLOGGER.BlogInterfaceType typeEnum = BlogConfigBLOGGER.getInterfaceTypeByNumber(config.getPostmethod());
	        	    blogapi = BlogInterfaceFactory.getInstance(typeEnum);
	        	    Log.d(TAG,"Using interface type: "+typeEnum);
	                Log.d(TAG,"Preparing the API with saved editor data: "+config.getPostConfig());
	                blogapi.setInstanceConfig(config.getPostConfig());
	                status.putString(MSG_KEY, "2");
	                statusMsg = mHandler.obtainMessage();
	                statusMsg.setData(status);
	                mHandler.sendMessage(statusMsg);
	                String auth_id = blogapi.getAuthId(username, password);
	                viewStatus = 1;
	                Log.d(TAG,"Got auth token:"+auth_id);
	                viewStatus = 2;
	                if(auth_id != null) {
	                	status.putString(MSG_KEY, "3");
	                	statusMsg = mHandler.obtainMessage();
	                	statusMsg.setData(status);
	                	mHandler.sendMessage(statusMsg);
	            	    try {
	            	    	resultFeed=blogapi.getAllPosts(username, password);
	            	    	Log.i(TAG,"Blog entries successfully received");
	            	    	viewOk=true;
	            		} catch (ServiceException e) {
	            			e.printStackTrace();
	            		} catch (IOException e) {
	            			e.printStackTrace();
	            		}
	                }else {
	                    viewStatus = 3;
	                }
	                status.putString(MSG_KEY, "4");
	                statusMsg = mHandler.obtainMessage();
	                statusMsg.setData(status);
	                mHandler.sendMessage(statusMsg);
	                if(viewOk) {
	                	Log.d(TAG,"Success!");
	                	viewStatus = 5;
	                } else {
	                	Log.d(TAG,"Viewing of the blog failed!");
	                	viewStatus = 4;
	                }
	                mHandler.post(mViewResults);
	                
	                if((resultFeed!=null)&&(viewOk)){
	                	Intent i = new Intent(MainActivity.this,ViewBlog.class);
	    				i.putExtra("ConfigOrder", CONFIG_ORDER);
	    				startActivity(i);
	    		        finish();
	                }
	        	}
	        }; 
	        viewThread.start();
	        viewProgress.setMessage("Viewing in progress...");
		}*/

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