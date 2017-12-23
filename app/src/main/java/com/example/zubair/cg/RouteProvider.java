package com.example.zubair.cg;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MUNEERA on 12/19/2017.
 */
public class RouteProvider  {


    /** Tag for the log messages */
    public static final String LOG_TAG = RouteProvider.class.getSimpleName();

    /** Database helper object */
    private static RouteDbHelper mDbHelper;
    private static RouteProvider routeProvider;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    int [] arr;
    public static RouteProvider get(Context context) {
        if (routeProvider == null) {
            routeProvider = new RouteProvider(context);
        }
        return routeProvider;
    }

    private RouteProvider(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RouteDbHelper(mContext).getWritableDatabase();
    }
    public boolean  DeleteRoute(String ID){
        try {
            mDatabase.execSQL("DELETE FROM Address Where _id LIKE '%" + ID + "%'");
            mDatabase.execSQL("DELETE FROM LatLngPoints Where _id LIKE '%" + ID + "%'");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Cursor getRoutesAddress() {
        String selectQuery = "SELECT  * FROM Address";
        try {

            Cursor cursor = mDatabase.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception e) {

        }

        return null;
    }

    public Cursor getRoutePoints(int id){
        // Select All Query
        String selectQuery = "Select * from LatLngPoints where _id LIKE '%" + id + "%'";

        try {

            Cursor cursor = mDatabase.rawQuery(selectQuery, null);
            return cursor;
        }catch (Exception e){

        }

        return null;

    }
    public boolean insertRouteLocation(int id,String latitude,String longitude) {
        try {
            mDatabase.execSQL("Insert Into LatLngPoints Values ( " + id + "," + latitude + "," + longitude + ")");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean insertRouteAddress(int ID,String start_address,String end_address,String distance, String duration) {
        try {
            mDatabase.execSQL("Insert Into Address Values ( '" + ID + "','" + start_address + "','" + end_address + "','" + distance +"','" + duration + "')");
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
  public int getRouteCount(){
      Cursor cursor = mDatabase.rawQuery("Select * from Address",null);
      if(cursor!=null){
          int count = cursor.getCount();
          cursor.close();
          return  count;
          }
            return  0;
  }
    public int getPointsCount(){
        Cursor cursor = mDatabase.rawQuery("Select * from LatLngPoints",null);
        if(cursor!=null){
       int count = cursor.getCount();
            cursor.close();
            return  count;
        }
        return  0;
    }
    public int getLastId(){
        Cursor c = mDatabase.rawQuery("Select * from Address",null);
        int lastId = 0;
        c.moveToFirst();
        while(!c.isAfterLast()){
            lastId = Integer.parseInt(c.getString(0));
            c.moveToNext();
        }
        c.close();
        Log.e("Last Id", lastId+"");
        return lastId;
    }

}
