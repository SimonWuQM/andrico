/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico;


import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.LoginActivity;

import com.google.gdata.data.Feed;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;



public class ContactList extends ExpandableListActivity
{
	private LinkedList<Contact> contacts = null;
	final static String TAG = "ContactList";
	private static int CONFIG_ORDER = 0;
	private SimpleExpandableListAdapter listAdapter = null;
	private SimpleExpandableListAdapter listAdapterEmpty = null;
	private SimpleExpandableListAdapter listAdapterContacts = null;
	
	
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
	    
	    
	    if (contacts != null)
		{
	    	for(int j = 0; j < contacts.size(); j++) 
         	{
        		TreeMap<String, String> cont = new TreeMap<String, String> ();
                TreeMap<String, String> info = new TreeMap<String, String> ();
                
        		cont.put("contact", contacts.get(j).getName() + " " + contacts.get(j).getSecondName());
                info.put( "date", contacts.get(j).getDateOfBirth());
                info.put( "adress", contacts.get(j).getAdress());
                info.put( "page", contacts.get(j).getPage());
                
                conts.add(cont);
                LinkedList<Map<String, String>> infolist = new LinkedList<Map<String, String>>();
                infolist.add(info);
                infos.add(infolist);
            }
         	listAdapterContacts = new SimpleExpandableListAdapter(
        												this,
        												conts,
        												R.layout.group_row,
        												new String[] {"contact"},
        												new int[] { R.id.NameOfGroup },
        												infos,
        												R.layout.child_table,
        												new String[] {"date", "adress", "page"},
        												new int[] {R.id.date, R.id.adress, R.id.page});
         	listAdapter = listAdapterContacts;
        	
        }  
	    else
	    {
	    	TreeMap<String, String> cont = new TreeMap<String, String> ();
            TreeMap<String, String> info = new TreeMap<String, String> ();
            
	    	cont.put("empty", "no contacts avaliable");
            info.put( "notification", "please, synchronize your facebook account at first");
            conts.add(cont);
            LinkedList<Map<String, String>> infolist = new LinkedList<Map<String, String>>();
            infolist.add(info);
            infos.add(infolist);
            
	    	listAdapterEmpty = new SimpleExpandableListAdapter(
	    			 										this,
	    			 										conts,
	    			 										R.layout.group_row,
	    			 										new String[] {"empty"},
	    			 										new int[] { R.id.NameOfGroup },
	    			 										infos,
	    			 										R.layout.notification,
	    			 										new String[] {"notification"},
	    			 										new int[] {R.id.Notification});
	    	listAdapter = listAdapterEmpty;
	    }	
	    
    	setListAdapter(listAdapter);
        this.getExpandableListView().setOnChildClickListener(ContactList.this);
        //this.getExpandableListView().setOnGroupClickListener(this);
        this.getExpandableListView().setOnGroupExpandListener(ContactList.this);
	}
	
	public void onGroupExpand(int groupPosition)
	{
		Object group;
		
		group = listAdapter.getGroup(groupPosition);
		
	}
	
	
	public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		switch(childPosition) 
		{
			case 1:
			case 2:
				
				Toast.makeText(ContactList.this,"Calling", Toast.LENGTH_SHORT).show();
				return true;
			
			case 3:
				Toast.makeText(ContactList.this, v.toString() , Toast.LENGTH_SHORT).show();	
				return true;
		}
	
		return false;
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
 