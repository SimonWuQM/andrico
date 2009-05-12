package org.andrico.andrico.content;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {


        private static final String TAG = "MobiLoggerDBHelper";
        private static final String CREATE_CONTACTS_TABLE = 
            					"CREATE TABLE IF NOT EXISTS CONTACTS ("+
            					"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            					"name TEXT," +
            					"second_name TEXT,"+
            					"info TEXT"+
            					");";
                
        
        boolean dbIsOpen;
        
        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
                super(context, name, factory, version);
                Log.d(TAG,"DBHelper constructor called, called super()");
                // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                Log.d(TAG, "onCreate called!");
                try {   
                        Log.d(TAG, "Create memo table...");                    
                        db.execSQL(CREATE_CONTACTS_TABLE);
                        // reset to default settings
                        // a bit ugly, but should work. Open db so that the settings
                        // insert method works.
                        this.dbIsOpen = true;
                        //this.createDefaultSettings(db);
                        
                        // let the isDatabaseOpen decide whether this shop is open.
                        this.dbIsOpen = false;
                } 
                catch (SQLException e) {
                        Log.e(TAG, "Failed to create table.");
                }
        }
        
/*        public boolean insert(Contact s, SQLiteDatabase db) 
        {
                String sql = "INSERT INTO CONTACTS (name, second_name, info) VALUES (?,?,?)";
                if(dbIsOpen) 
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

*/
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onUpgrade called!");
        }


}