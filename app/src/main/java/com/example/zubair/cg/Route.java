package com.example.zubair.cg;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Route extends AppCompatActivity {
    RouteCursorAdapter mCursorAdapter;
    RouteProvider rp;
    TextView noRoutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        noRoutes = (TextView) findViewById(R.id.noRoutes);
        rp = RouteProvider.get(getApplicationContext());
        Cursor cursor = rp.getRoutesAddress();
if(cursor==null || cursor.getCount()==0){
    noRoutes.setVisibility(View.VISIBLE);
}
        // Find the ListView which will be populated with the Route data
        ListView RouteListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.


        // Setup an Adapter to create a list item for each row of Route data in the Cursor.
        // There is no Route data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new RouteCursorAdapter(this,cursor);
        RouteListView.setAdapter(mCursorAdapter);

    }
}
