package com.example.zubair.cg;

import com.example.zubair.cg.RouteContract;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by MUNEERA on 12/19/2017.
 */
public class RouteDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = RouteDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "Routes.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link RouteDbHelper}.
     *
     * @param context of the app
     */
    public RouteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the routes address table
//        String SQL_CREATE_ADDRESS_TABLE =  "CREATE TABLE " + RouteContract.TABLE_ROUTE_ADDRESS + " ("
//                + RouteContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + RouteContract.COLUMN_START_ADDRESS + " TEXT NOT NULL, "
//                + RouteContract.COLUMN_END_ADDRESS + " TEXT NOT NULL)";
        db.execSQL("Create table LatLngPoints(_id,Lattitude,Longitude)");
        db.execSQL("Create table Address(_id,start_address VARCHAR,end_address VARCHAR,distance,duration)");

        // Create a String that contains the SQL statement to create the routes location table
//        String SQL_CREATE_LOCATION_TABLE =  "CREATE TABLE " + RouteContract.TABLE_ROUTE_LOCATION + " ("
//                + RouteContract._ID + " INTEGER, "
//                + RouteContract.COLUMN_LATITUDE + " TEXT NOT NULL, "
//                + RouteContract.COLUMN_LONGITUDE + " TEXT NOT NULL )";

        // Execute the SQL statement
      //  db.execSQL(SQL_CREATE_ADDRESS_TABLE);
       // db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}


