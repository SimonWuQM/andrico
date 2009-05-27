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

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.AuthorizationActivity;
import org.andrico.andrico.facebook.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gdata.data.Feed;
import com.google.gdata.util.ServiceException;




public class ContactInfo extends Activity
{
    /** Called when the activity is first created. */
    private static String fbid = "";
	
	final static String LOG = "ContactInfo";
	public static Feed resultFeed = null;
	int viewStatus = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    Window  w = getWindow(); 
	    w.requestFeature(Window.FEATURE_LEFT_ICON);   
	    setContentView(R.layout.contact_info);
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	        
	    Intent i = this.getIntent();
	    
	    if(i.hasExtra("fbid"))
	    {
	        fbid = i.getStringExtra("fbid");
	    }
	    else
	    {
	    	AlertDialog dialog = new AlertDialog.Builder(ContactInfo.this)
			.setTitle("FAILED")
			.setMessage("UNKNOWN ERROR OCCURED")
			.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								dialog.dismiss();
								Intent j = new Intent(ContactInfo.this, ContactList.class);
								startActivity(j);
						        finish();
							}
						}).create(); 
	        	
	    	dialog.show(); 
	    }
	        
	    DBContact db = new DBContact();    
	    Contact contact = db.getContactByFBid(ContactInfo.this, fbid); 
	    
	    if (contact == null)
	    {
	    	AlertDialog dialog = new AlertDialog.Builder(ContactInfo.this)
			.setTitle("FAILED")
			.setMessage("CAN'T FIND THE CONTACT")
			.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								dialog.dismiss();
								Intent j = new Intent(ContactInfo.this, ContactList.class);
								startActivity(j);
						        finish();
							}
						}).create(); 
	        	
	    	dialog.show(); 
	    } 
	    else
	    {
	    	((TextView) this.findViewById(R.id.name)).setText(contact.getName());
	    	((TextView) this.findViewById(R.id.secondName)).setText(contact.getSecondName());
	    	((TextView) this.findViewById(R.id.date)).setText(contact.getDateOfBirth());
	    	((Button) this.findViewById(R.id.adress)).setText(contact.getAdress());
	    	((Button) this.findViewById(R.id.page)).setText(contact.getPage());
	    	
	    	((TextView) this.findViewById(R.id.name)).setVisibility(View.VISIBLE);
	    	((TextView) this.findViewById(R.id.secondName)).setVisibility(View.VISIBLE);
	    	((TextView) this.findViewById(R.id.date)).setVisibility(View.VISIBLE);
	    	((Button) this.findViewById(R.id.adress)).setVisibility(View.VISIBLE);
	    	((Button) this.findViewById(R.id.page)).setVisibility(View.VISIBLE);
	    }
	    
	    this.findViewById(R.id.page).setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View v)
				{   
	        		
	        		
	        		String path = (String)((Button) ContactInfo.this.findViewById(R.id.page)).getText();
	        		
	        		Intent i = new Intent(ContactInfo.this, WebActivity.class);
	        		i.putExtra("url", path);
	        		Log.d(LOG, "loadUrl: " + path.toString());
	        		startActivity(i);
	        		 
	       		}
			}); 
	        
	       
	}
		
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(ContactInfo.this,ContactList.class);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}
}