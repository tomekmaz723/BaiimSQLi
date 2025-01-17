package com.example.baiimsqli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";
    public DatabaseHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table users(username TEXT primary key, password TEXT, admin BOOLEAN CHECK (admin IN (0, 1)))");
        sqLiteDatabase.execSQL("create table movies(title TEXT primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
        sqLiteDatabase.execSQL("drop table if exists movies");
    }

    public Boolean insertData(String username, String password, int admin){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);
        values.put("admin", admin);

        long result = sqLiteDatabase.insert("users", null, values);
        return result != 1;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username=?", new String[] {username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username=? and password=?", new String[] {username, password});
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username='" + username + "' and password= '" + password + "'", null);
            return cursor.getCount() > 0;
        } catch (Exception e) {sqLiteDatabase.close();return false;}
    }

    public Boolean checkadmin(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username=? and password=?", new String[] {username, password});
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select admin from users where username='" + username + "' and password= '" + password + "'", null);
            Log.i("ArrayList", String.valueOf(cursor.getInt(0)));
            return cursor.getInt(0) == 1;
        } catch (Exception e) {sqLiteDatabase.close();return false;}

    }



    public String [] getMoviesList(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String [] mydata = new String[0];
        ArrayList<String> array = new ArrayList<>();
        //Inside the method you've read the cursor, loop through it and add those item to array
        String sql="SELECT * FROM movies";
        //execute SQL
        try {
        Cursor cr = sqLiteDatabase.rawQuery(sql, null);
        cr.moveToFirst();//cursor pointing to first row
        mydata = new String[cr.getCount()];//create array string based on numbers of row
        int i=0;
        do  {
            mydata[i] = cr.getString(0);//insert new stations to the array list
            //Log.i("ArrayList",mydata[i]);
            i++;
        }while(cr.moveToNext());} catch (Exception e){
            Log.e("ERR","exception",e);
        };
        //Finally Set the adapter to AutoCompleteTextView like this,
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, mydata);
        //populate the list to the AutoCompleteTextView controls
        sqLiteDatabase.close();
        return mydata;
    }

    public ArrayList<HashMap<String, String>> getTitle(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from movies where title='" + title +  "'", null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from movies where title=?", new String[] {title});
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                maplist.add(map);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        // return contact list
        return maplist;
    }
}
