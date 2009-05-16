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
                DBHelper helper = new DBHelper(app, "DataBaseAndrico.db" , null, 1);
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
                //id, name, second_name, phone_private, phone_home, adress
                Contact[] cont = null;
                Cursor cur = null;
                if(isDatabaseReady(app)) 
                {
                	cur = db.rawQuery(request, null);
                	//cur = db.query("CONTACTS", null, "id = 1", null, null, null, "name");
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
                        c.setPhone_private(cur.getString(3));
                        c.setPhone_work(cur.getString(4));
                        c.setAdress(cur.getString(5));
                                
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
                        "SELECT name, second_name, phone_private, phone_home, adress FROM CONTACTS where id = ?"; 
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
                        c.setPhone_private(cur.getString(2));
                        c.setPhone_work(cur.getString(3));
                        c.setAdress(cur.getString(4));
                        cur.close();
                }
                db.close();
                return c;
        }
       
        public boolean insert(Context app, Contact cont) {
        	String request = "INSERT INTO CONTACTS (name, second_name, phone_private, phone_work, adress)" +
                " VALUES " + "(?,?,?,?,?)";
                
            if(isDatabaseReady(app)) 
            {
            	try 
            	{
            		SQLiteStatement insertStmt = db.compileStatement(request);
                    insertStmt.bindString(1, cont.getName());
                    insertStmt.bindString(2, cont.getSecondName());
                    insertStmt.bindString(3, cont.getPhone_private());
                    insertStmt.bindString(4, cont.getPhone_work());
                    insertStmt.bindString(5, cont.getAdress());
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
      
        public void update(Context app,final Contact c) {
                int id = -1;
                String request = null;
                if(c != null) 
                {
                	id = c.getId();
                }
                if(id != -1) 
                {
                	request = "UPDATE CONTACTS SET "+
                        "name = ?,"+
                        "second_name = ?,"+
                        "phone_private = ?,"+
                        "phone_work = ?,"+
                        "adress = ? "+
                        "WHERE id = ?";
                } 
                else 
                {
                	Log.e(TAG,"Failed to update CONTACTS since the id is not good!");
                }
                
                if(isDatabaseReady(app)) {
                        SQLiteStatement updateStmt = db.compileStatement(request);
                        updateStmt.bindString(1,c.getName());
                        updateStmt.bindString(2,c.getSecondName());
                        updateStmt.bindString(3,c.getPhone_private());
                        updateStmt.bindString(4,c.getPhone_work());
                        updateStmt.bindString(5,c.getAdress());
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
        		db.delete("CONTACT","id=?",new String[]{""+id});
                db.close();
            }
        }
        
        public void deleteContacts(Context app) 
        {
        	if(isDatabaseReady(app)) 
            {
                Log.d(TAG,"Deleting the settings table contents.");
                db.delete("CONTACTS",null,null);
                db.close();
            }
        }     
        
        /*public void dropContacts(Context app)
        {
        	db.execSQL("DROP TABLE IF EXISTS CONTACTS");
        }*/
}