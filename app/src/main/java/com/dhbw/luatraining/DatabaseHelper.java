package com.dhbw.luatraining;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

// Databasehelper design by http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.dhbw.luatraining/databases/";
    private static String DB_NAME = "questionnaire.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        // create database if not exists
        createDataBase();
        // open the database
        LogHelper.addLogLine("open database");
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void createDataBase() {
        try{
            // TODO: the following must be uncommented in production but in dev the db has to be copied every time
            //
            //SQLiteDatabase checkDB = null;
            //checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
            //if(checkDB != null)
            //    checkDB.close();
            //else
            //{
                // By calling this method an empty database will be created that can be overwritten
                this.getReadableDatabase();

                // Open local db as the input stream
                InputStream myInput = myContext.getAssets().open(DB_NAME);

                // Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer))>0){
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
            //}
        }
        catch(Exception e)
        {
            LogHelper.addLogLine("Exception at createDataBase: " + e.toString());
        }
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}