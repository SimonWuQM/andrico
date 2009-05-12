package org.andrico.andrico.content;


import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


public class DBContacts {
        
        private SQLiteDatabase db = null;
        private static final String TAG = "DBContacts";
        
       
        private boolean isDatabaseReady(Context app) 
        {
                DBHelper helper = new DBHelper(app,"andrico.db",null, 1);
                Log.d(TAG,"isDataBaseReady Called... helper is "+helper);
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
                        
        
        
        public boolean insert(Context app,Contact s) 
        {
            String sql = "INSERT INTO CONTACTS (name, second_name, info) VALUES (?,?,?)";
            if(isDatabaseReady(app)) 
            {
            	try 
                {
            		SQLiteStatement insertStmt = db.compileStatement(sql);
            		insertStmt.bindString(1,s.getName());
                    insertStmt.bindString(2,s.getSecondName());
                    insertStmt.bindString(3,s.getInfo());
                    insertStmt.execute();
                } 
                catch (SQLException e) 
                {
                	Log.e(TAG, "SQLException while executing: "+sql+"\n");
                	return false;
                }
                
                return true;
            } 
            else 
            {
            	Log.d(TAG,"DB was not open while inserting contact");
                return false;
            }
        }

        
        public Contact[] getContacts(Context app) 
        {
                final String SQL = "SELECT id, name, second_name, info from CONTACTS";
                Cursor cur = null;
                Contact[] conts = null;
                if(isDatabaseReady(app)) 
                {
                        cur = db.rawQuery(SQL, null);
                } 
                else 
                {
                        Log.e(TAG,"Database is not open when getting contact names!");
                        return null;
                }
                
                if((cur != null) && (cur.getCount() > 0)) 
                {
                	conts = new Contact[cur.getCount()];
                        cur.moveToFirst();
                        for(int i = 0; i < cur.getCount(); i++) 
                        {
                        	Contact contact = new Contact();
                            contact.setId(cur.getInt(0));
                            contact.setName(cur.getString(1));
                            contact.setSecondName(cur.getString(2));
                            contact.setInfo(cur.getString(3));
                            conts[i] = contact;
                            cur.moveToNext();
                        }
                        cur.close();
                }
                db.close();
                return conts;
        }
        
        
        public LinkedList<Contact> getContactNames(Context context) 
        {
                LinkedList<Contact> result = null;
                Contact [] conts = null;
                conts = getContacts(context);
                if(conts != null) 
                {
                        result = new LinkedList<Contact>();
                        for(int i = 0; i < conts.length; i++) 
                        {
                                result.add(conts[i]);
                        }
                        return result;
                } 
                else 
                {
                        Log.e(TAG,"Failed to get the contact names from DB!");
                        return null;
                }
        }
        
                
        public Contact getConactById(Context app,int id) 
        {
                final String request = "SELECT name, secondname, info FROM CONTACTS where id = ?"; 
                Cursor cur = null;
                Contact con = null;
                if(isDatabaseReady(app)) 
                {
                	cur = db.rawQuery(request, new String[]{""+id});
                } 
                else 
                {
                	Log.e(TAG,"Database is not open when getting blog entries!");
                    return null;
                }
                if((cur != null) && (cur.getCount() == 1)) {
                        con = new Contact();
                        cur.moveToFirst();
                        con.setId(id);
                        con.setName(cur.getString(0));
                        con.setSecondName(cur.getString(2));
                        con.setInfo(cur.getString(3));
                        cur.close();
                }
                db.close();
                return con;
        }
       
        /**
         * Update SettingBean in the DB, have ID there in the bean, otherwise it won't
         * work!
         * @param mb
         */
        
        public void update(Context app,final Contact mb) 
        {
                int id = -1;
                String request = null;
                if(mb != null) 
                {
                        id = mb.getId();
                }
                
                if(id != -1) 
                {
                    request = "UPDATE SETTINGS SET "+
                        "name = ?,"+
                        "second_name = ?,"+
                        "info = ? "+
                        "WHERE id = ?";
                } 
                else 
                {
                        Log.e(TAG,"Failed to update SETTINGS since the id is not good!");
                }
                
                if(isDatabaseReady(app)) {
                        Log.d(TAG,"Executing: "+request);
                        SQLiteStatement updateStmt = db.compileStatement(request);
                        updateStmt.bindString(1,mb.getName());
                        updateStmt.bindString(2,mb.getSecondName());
                        updateStmt.bindString(3,mb.getInfo());
                        updateStmt.bindLong(4, mb.getId());
                        updateStmt.execute();
                        db.close();
                } 
                else 
                {
                	Log.e(TAG,"Database is not open when updating conacts!");
                }
                
                Log.d(TAG,"Settings updated with, id = "+id);
        }

       
        public void deleteContact(Context app,int id) 
        {
        	if(isDatabaseReady(app)) 
            {
        		db.delete("SETTINGS","id=?", new String[]{""+id});
                db.close();
            }
        }
}