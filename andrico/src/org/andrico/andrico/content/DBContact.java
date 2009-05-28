/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico.content;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;



public class DBContact 
{
      private SQLiteDatabase db = null;
      private static final String TAG = "DBContact";
      
        
        private boolean isDatabaseReady(Context app) {
                DBHelper helper = new DBHelper(app, "andricontacts.db" , null, 1);
                Log.d(TAG,"isDataBaseReady Called, helper is "+helper);
                db = helper.getWritableDatabase();
                Log.d(TAG,"getWritableDatabase called, db is "+db);
                if(db == null) 
                {
                        return false;
                } 
                else 
                {
                        return true;
                }
        }
                        
        
       
        
                     
        public Contact[] getContacts(Context app) 
        {
                final String request = "SELECT * from CONTACTS ORDER BY name";
                Contact[] cont = null;
                Cursor cur = null;
                if(isDatabaseReady(app)) 
                {
                	cur = db.rawQuery(request, null);
                } 
                else 
                {
                	Log.e(TAG,"Database is not open when getting contacts");
                    return null;
                }
                
                if((cur != null) && (cur.getCount() > 0)) 
                {
                	cont = new Contact[cur.getCount()];
                    cur.moveToFirst();
                    for(int i = 0; i < cur.getCount(); i++) 
                    {
                    	Contact c = new Contact();
                        c.setId(cur.getInt(0));
                        c.setName(cur.getString(1));
                        c.setSecondName(cur.getString(2));
                        c.setDateOfBirth(cur.getString(3));
                        c.setAdress(cur.getString(4));
                        c.setPage(cur.getString(5));
                        c.setFBid(cur.getString(6));
                        
                        cont[i] = c;        
                        cur.moveToNext();
                    }
                    cur.close();
                }
                db.close();
                return cont;
        }
        
        public LinkedList<Contact> getContactList(Context context) 
        {
                LinkedList<Contact> result = null;
                Contact[] cont = null;
                cont = getContacts(context);
                if(cont != null) 
                {
                	result = new LinkedList<Contact>();
                    for(int i = 0; i < cont.length; i++) 
                    {
                    	result.add(cont[i]);
                    }
                    return result;
                } 
                else 
                {
                	Log.e(TAG,"Failed to get the contact list!");
                    return null;
                }
        }
        
        public Contact getContactById(Context app,int id) {
                final String request = 
                        "SELECT name, second_name, date_of_birth, adress, page, fb_id FROM CONTACTS where id = ?"; 
                Cursor cur = null;
                Contact c = null;
                if(isDatabaseReady(app)) 
                {
                	cur = db.rawQuery(request, new String[]{""+id});
                } 
                else 
                {
                	Log.e(TAG,"Database is not open when getting contact by id!");
                    return null;
                }
                
                if((cur != null) && (cur.getCount() == 1)) 
                {
                        c = new Contact();
                        cur.moveToFirst();
                        c.setId(id);
                        c.setName(cur.getString(0));
                        c.setSecondName(cur.getString(1));
                        c.setDateOfBirth(cur.getString(2));
                        c.setAdress(cur.getString(3));
                        c.setPage(cur.getString(4));
                        c.setFBid(cur.getString(5));
                        cur.close();
                }
                db.close();
                return c;
        }
       
        public Contact getContactByFBid(Context app,String id) {
            final String request = 
                    "SELECT id, name, second_name, date_of_birth, adress, page FROM CONTACTS where fb_id = ?"; 
            Cursor cur = null;
            Contact c = null;
            if(isDatabaseReady(app)) 
            {
            	cur = db.rawQuery(request, new String[]{""+id});
            } 
            else 
            {
            	Log.e(TAG,"Database is not open when getting contact by id!");
                return null;
            }
            
            if((cur != null) && (cur.getCount() == 1)) 
            {
            	c = new Contact();
                cur.moveToFirst();
                c.setId(cur.getInt(0));
                c.setName(cur.getString(1));
                c.setSecondName(cur.getString(2));
                c.setDateOfBirth(cur.getString(3));
                c.setAdress(cur.getString(4));
                c.setPage(cur.getString(5));
                c.setFBid(id);
                cur.close();
            }
            db.close();
            return c;
        }
        
        public boolean insert(Context app, Contact cont) {
        	String request = "INSERT INTO CONTACTS (name, second_name, date_of_birth, adress, page, fb_id)" +
                " VALUES " + "(?,?,?,?,?,?)";
                
            if(isDatabaseReady(app)) 
            {
            	try 
            	{
            		SQLiteStatement insertStmt = db.compileStatement(request);
                    insertStmt.bindString(1, cont.getName());
                    insertStmt.bindString(2, cont.getSecondName());
                    insertStmt.bindString(3, cont.getDateOfBirth());
                    insertStmt.bindString(4, cont.getAdress());
                    insertStmt.bindString(5, cont.getPage());
                    insertStmt.bindString(6, cont.getFBid());  
                    insertStmt.execute();
                } 
            	catch (SQLException e) 
            	{
            		Log.e(TAG, "SQLException while executing insert\n");
                    return false;
                }
                        
                return true;
            } 
            else 
            {
            	Log.d(TAG,"DB was not open while inserting BlogConfig!");
            	return false;
            }
        }
      
        public void update(Context app,final Contact c) 
        {
                String id = "";
                String request = null;
                if(c != null) 
                {
                	id = c.getFBid();
                }
                if(id != "") 
                {
                	request = "UPDATE CONTACTS SET "+
                        "name = ?,"+
                        "second_name = ?,"+
                        "date_of_birth = ?,"+
                        "adress = ?,"+
                        "page = ?," +
                        "WHERE fbid = ?";
                } 
                else 
                {
                	Log.e(TAG,"Failed to update CONTACTS due to the fb id is not good!");
                }
                
                if(isDatabaseReady(app)) {
                        SQLiteStatement updateStmt = db.compileStatement(request);
                        updateStmt.bindString(1,c.getName());
                        updateStmt.bindString(2,c.getSecondName());
                        updateStmt.bindString(3,c.getDateOfBirth());
                        updateStmt.bindString(4,c.getAdress());
                        updateStmt.bindString(5,c.getPage());
                        updateStmt.bindString(6,c.getFBid());
                        updateStmt.execute();
                        db.close();
                        Log.d(TAG,"Executing: "+request);
                } 
                else 
                {
                        Log.e(TAG,"Database is not open when updating contacts!");
                }
                Log.d(TAG,"Blog config updated with, id = "+id);
        }
                
        
        public void deleteContact(Context app,int id) 
        {
        	if(isDatabaseReady(app)) 
        	{
        		db.delete("CONTACTS","id=?",new String[]{""+id});
                db.close();
            }
        }
        
        public void deleteContacts(Context app) 
        {
        	if(isDatabaseReady(app)) 
            {
                Log.d(TAG,"Deleting the contacts table contents.");
                db.delete("CONTACTS",null,null);
                db.close();
            }
        }     
        
        public void synchronize(Context app, LinkedList <Contact> contacts)
        {
        	for (int j = 0; j < contacts.size(); j++)
        	{
        		Contact newContact = contacts.get(j);
        		String fbid = newContact.getFBid();
        		Contact contact = this.getContactByFBid(app, fbid);
        		if (contact == null)
        		{
        			this.insert(app, newContact);
        		}
        		else
        		{
        			if (!newContact.Equals(contact))
        			{
        				//this.update(app, newContact);
        				this.deleteContact(app, contact.getId());
        				this.insert(app, newContact);
        			}
        		}
        	}
        }
        
        public void synchronizeDel(Context app, LinkedList <Contact> contacts)
        {
        	this.deleteContacts(app);
        	
        	for (int j = 0; j < contacts.size(); j++)
        	{
        		Contact newContact = contacts.get(j);
        		this.insert(app, newContact);
        	}
        }
        
        
        /*public void dropContacts(Context app)
        {
        	db.execSQL("DROP TABLE IF EXISTS CONTACTS");
        }*/
}