/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico;

import java.util.HashMap;
import java.util.LinkedList;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContacts;

import com.google.gdata.data.Feed;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;


public class ContactList extends ListActivity {
	private LinkedList<Contact> contacts = null;
	private static final int BLOGCONFIG_REQUEST = 4;
	final static String TAG = "ContactList";
	private static int CONFIG_ORDER = 0;
	private ProgressDialog viewProgress = null;
	private final String MSG_KEY="value";
	public static Feed resultFeed = null;
	int viewStatus = 0;
	
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
	    Window  w = getWindow(); 
	    w.requestFeature(Window.FEATURE_LEFT_ICON);   
	    setContentView(R.layout.contacts);
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	        
 
         // Query for all people contacts using the Contacts.People convenience class.
         // Put a managed wrapper around the retrieved cursor so we don't have to worry about
         // requerying or closing it as the activity changes state. 
         
	    
	    DBContacts db = new DBContacts();
     	 
        Contact contact = new Contact();
        contact.setName("Bob");
        contact.setSecondName("Hooks");
        contact.setInfo("neighbour");
         
        db.insert(ContactList.this, contact);
         
         
        contacts = db.getContactNames(ContactList.this);
         
        String [] cont = null;
         
        if(contacts!=null)
        {
     		//HashMap<Integer, Integer> configItemOrder = new HashMap<Integer,Integer>(contacts.size());
         	
        	cont = new String[contacts.size()]; 
        	
        	for(int j = 0; j < contacts.size(); j++) 
         	{
                 /*try 
                 {
                	 configItemOrder.put(new Integer(j), new Integer(contacts.get(j).getId()));
                 } 
                 catch (NullPointerException ne) 
                 {
                 	Log.d(TAG,"Config items contains a null entry at "+j+"! Default to first config.");
                 	configItemOrder.put(new Integer(j), new Integer(0));*/
        		cont[j] = contacts.get(j).getName() + " " + contacts.get(j).getSecondName();
                //}
            }
         	
         	setListAdapter(new ArrayAdapter<String>(ContactList.this, android.R.layout.simple_list_item_1, cont));
         }	
     }
	
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(ContactList.this,MainActivity.class);
    		i.putExtra("ConfigOrder", CONFIG_ORDER);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}
 }
 