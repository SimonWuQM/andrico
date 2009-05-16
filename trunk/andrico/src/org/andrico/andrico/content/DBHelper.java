/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico.content;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {


        private static final String TAG = "MobiLoggerDBHelper";
        
        boolean dbIsOpen;
        
        public DBHelper(Context context, String name, CursorFactory factory, int version) 
        {
                super(context, name, factory, version);
                Log.d(TAG,"DBHelper constructor called, called super()");
                // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) 
        {
                Log.d(TAG, "onCreate called!");
                try 
                {   
                        Log.d(TAG, "Create CONTACT table...");
                        
                        db.execSQL(CREATE_CONTACT_TABLE);
                        this.dbIsOpen = true;
                        this.dbIsOpen = false;
                } 
                catch (SQLException e) 
                {
                        Log.e(TAG, "Failed to create CONTACTS tables.");
                }
        }
        
        
        

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
                // TODO Auto-generated method stub
                Log.d(TAG, "onUpgrade called!");
        }
}