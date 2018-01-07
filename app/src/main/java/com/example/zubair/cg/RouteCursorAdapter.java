
package com.example.zubair.cg;

        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.speech.tts.TextToSpeech;
        import android.support.v4.widget.CursorAdapter;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.List;

/**
 * Created by MUNEERA on 12/19/2017.
 */


/**
 * {@link RouteCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of route data as its data source. This adapter knows
 * how to create list items for each row of route data in the {@link Cursor}.
 */
public class RouteCursorAdapter extends CursorAdapter {

    Context context;
    RouteProvider rp;
    View mView;
    public RouteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.context = context;
        rp = RouteProvider.get(context);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        mView = view;

        TextView startAddressTextView = (TextView) view.findViewById(R.id.start_address);
        TextView endAddressTextView = (TextView) view.findViewById(R.id.end_address);
        TextView keyword=(TextView) view.findViewById(R.id.keyword);
        Button delete_btn = (Button) view.findViewById(R.id.delete_btn);
        Button start_btn = (Button) view.findViewById(R.id.start_btn);

        // Read the route attributes from the Cursor for the current route
        final int routeId = Integer.parseInt(cursor.getString(0));
        String routeStart = cursor.getString(1);
        String routeEnd = cursor.getString(2);
        final String distance = cursor.getString(3);
        final String duration = cursor.getString(4);
        final String keywordtext=cursor.getString(5);
        startAddressTextView.setText(routeStart);
        endAddressTextView.setText(routeEnd);
        if(keywordtext.equals("")){
            keyword.setVisibility(View.GONE);
        }else{
            keyword.setText(keywordtext);
        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mView.setVisibility(View.GONE);
                if(rp.DeleteRoute(routeId+"")){
                    cursor.requery();
                    notifyDataSetChanged();
                    Toast.makeText(context,"Route Deleted Successfully",Toast.LENGTH_SHORT);

                }
                else
                    Toast.makeText(context,"Error Deleting Route",Toast.LENGTH_SHORT);
            }
        });
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Cursor c =  rp.getRoutePoints(routeId);
                if(c!=null)
                Log.e("c is not null","");
                MapsActivity.history_Route = true;
                MapsActivity.dis =distance;
                MapsActivity.dur = duration;
                DirectionsJSONParser.c = c;
                Intent intent = new Intent(context,MapsActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
