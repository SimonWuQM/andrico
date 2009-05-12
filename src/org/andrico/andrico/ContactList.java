package org.andrico.andrico;

import java.util.LinkedList;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContacts;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;


public class ContactList extends ListActivity {
	private LinkedList<Contact> contact = null;
     
	protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
 
         setContentView(R.layout.contacts);
 
         // Query for all people contacts using the Contacts.People convenience class.
         // Put a managed wrapper around the retrieved cursor so we don't have to worry about
         // requerying or closing it as the activity changes state.
       /*  Cursor cur; 
         contact = DBContacts.getContactNames(ContactList.this);
         startManagingCursor(mCursor);
 */
         // Now create a new list adapter bound to the cursor. 
         // SimpleListAdapter is designed for binding to a Cursor.
  /*       ListAdapter adapter = new SimpleCursorAdapter(
                 this, // Context.
                 android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor 
 rows).
                 mCursor,                                    // Pass in the cursor to bind to.
                 new String[] {People.NAME, People.COMPANY}, // Array of cursor columns to bind to.
                 new int[]);                                 // Parallel array of which template objects to bind to those columns.
 */
         // Bind to our new adapter.
  //      setListAdapter(adapter);
     }
 }
 