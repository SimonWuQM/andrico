/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;

import com.google.gdata.data.Feed;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import 	android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;



public class ContactList extends ExpandableListActivity
{
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
	    
	    DBContact db = new DBContact();
     	
     	/*Contact contact = new Contact();
        contact.setName("Erik");
        contact.setSecondName("Brooks");
        contact.setPhone_private("7696980");
        //contact.setPhone_work("9698798");
        contact.setAdress("London");
         
        db.insert(ContactList.this, contact);
        */
	    contacts = db.getContactList(ContactList.this);
         
	    LinkedList<Map<String, String>> conts = new LinkedList<Map<String, String>>();
        LinkedList<LinkedList<Map<String, String>>> infos = new LinkedList<LinkedList<Map<String, String>>>();
        
        if(contacts!=null)
        {	
        	for(int j = 0; j < contacts.size(); j++) 
         	{
        		TreeMap<String, String> cont = new TreeMap<String, String> ();
                TreeMap<String, String> info = new TreeMap<String, String> ();
                
        		cont.put("contact", contacts.get(j).getName() + " " + contacts.get(j).getSecondName());
                info.put( "phone1", contacts.get(j).getPhone_private());
                info.put( "phone2", contacts.get(j).getPhone_work());
                info.put( "adress", contacts.get(j).getAdress());
                conts.add(cont);
                LinkedList<Map<String, String>> infolist = new LinkedList<Map<String, String>>();
                infolist.add(info);
                infos.add(infolist);
            }
         	
         	//setListAdapter(new ArrayAdapter<String>(ContactList.this, android.R.layout.simple_list_item_1, cont));
         
        	SimpleExpandableListAdapter listAdapter = new SimpleExpandableListAdapter(
        												this,
        												conts,
        												R.layout.group_row,
        												new String[] {"contact"},
        												new int[] { R.id.NameOfGroup },
        												infos,
        												R.layout.child_table,
        												new String[] {"phone1", "phone2", "adress"},
        												new int[] {R.id.phone1, R.id.phone2, R.id.adress});
        	setListAdapter(listAdapter);
        }	
        
     }
	
	//public boolean onChildClick (, View v, int groupPosition, int childPosition, long id)
	//{};
	
	
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
	};
	
 }
 