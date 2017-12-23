package com.example.zubair.cg;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MUNEERA on 12/19/2017.
 */
public class RouteContract implements BaseColumns {




    public final static String TABLE_ROUTE_ADDRESS = "Routes_Address";

    public final static String TABLE_ROUTE_LOCATION = "Routes_Location";
    /**
     * Unique ID number for the route (only for use in the database table).
     *
     * Type: INTEGER
     */
    public final static String _ID ="ID";

    public final static int ID=1;

    public final static String COLUMN_START_ADDRESS ="start_address";

    public final static String COLUMN_END_ADDRESS = "end_address";

    public final static String COLUMN_LATITUDE = "latitude";

    public final static String COLUMN_LONGITUDE = "longitude";






}
