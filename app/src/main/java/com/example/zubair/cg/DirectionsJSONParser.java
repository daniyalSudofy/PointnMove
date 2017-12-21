package com.example.zubair.cg;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zubair on 13-Dec-17.
 */
public class DirectionsJSONParser {


    LatLng firstLocation,secondLocation;
    String voicetext;
Context mContext;
    public static List voicelist;
    RouteProvider rp;
    int ID;
    String start_address;
    String end_address;
    public static Cursor c;
    DirectionsJSONParser (LatLng firstLocation, LatLng secondLocation, Context context){
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.mContext = context;
        voicelist=new ArrayList();
        rp = RouteProvider.get(mContext);
    }

    List addStartLine(double Lat, double Lng, LatLng firstLocation){
        List li = new ArrayList();
        //Log.e("In func","");
        for(float t = 0 ; t<1; t+=0.2 ){
            double lat = firstLocation.latitude +(Lat-firstLocation.latitude)*t;
            double lng = firstLocation.longitude +(Lng-firstLocation.longitude)*t;
            //Log.e("Lat Lang",lat+" "+lng);
            LatLng p = new LatLng(lat,lng);
            li.add(p);
            if(t==0){
                if(!MapsActivity.history_Route)
                voicelist.add("Journey started");
            }
            else{
                if(!MapsActivity.history_Route)
                voicelist.add("no");
            }
        }

        return  li;
    }
    List addEndLine(double Lat, double Lng, LatLng secondLocation){
        List li = new ArrayList();
        // Log.e("In func","");
        for(double t = 0 ; t<=1; t+=0.2 ){
            double lat = Lat  +(secondLocation.latitude-Lat)*t;
            double lng = Lng +(secondLocation.longitude-Lng)*t;
            //Log.e("Lat Lang",lat+" "+lng);
            LatLng p = new LatLng(lat,lng);
            li.add(p);
            if(!MapsActivity.history_Route)
            voicelist.add("no");
        }
        double lat = Lat  +(secondLocation.latitude-Lat)*1;
        double lng = Lng +(secondLocation.longitude-Lng)*1;
        LatLng p = new LatLng(lat,lng);
        li.add(p);
        if(!MapsActivity.history_Route)
        voicelist.add("You reach your destination");

        return  li;
    }
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap>> parse(JSONObject jObject){

        List<List<HashMap>> routes = new ArrayList<List<HashMap>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
Log.e("In Parse function","func");
        try {

            jRoutes = jObject.getJSONArray("routes");
            Log.e("a","jroutes fetched");
            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++) {
                Log.e("a", "in for loop");
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                Log.e("a", "jLegs fetched");
                for (int j = 0; j < jLegs.length(); j++) {
                    MapsActivity.dur   = (String) ((JSONObject) ((JSONObject) jLegs.get(j)).get("duration")).get("text");
                    MapsActivity.dis = (String) ((JSONObject)    ((JSONObject) jLegs.get(j)).get("distance")).get("text");
                    start_address=(String)((JSONObject) jLegs.get(j)).get("start_address");
                    end_address=(String)((JSONObject) jLegs.get(j)).get("end_address");
                    Log.e("Querry Database","1");
                   ID = rp.getRouteCount()+1;
                    Log.e("Count is", ID+"");
                    rp.insertRouteAddress(ID,start_address,end_address);
                }
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    Double start_lat,start_lng,end_lat,end_lng;
                    voicetext=null;
                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        Log.e("In Jsteps",k+"");
                        voicetext = (String) (((JSONObject) jSteps.get(k)).get("html_instructions"));
                        if (checkRight(voicetext))
                            voicetext = "Turn Right";
                        else
                            voicetext = "Turn Left";

                        if (k == 0) {
                            Double lat = (Double) ((JSONObject) ((JSONObject) jSteps.get(k)).get("start_location")).get("lat");
                            Double lng = (Double) ((JSONObject) ((JSONObject) jSteps.get(k)).get("start_location")).get("lng");

                            // List li = getPoints(firstLocation,new LatLng(lat,lng),voicetext);
                            List li = addStartLine(lat, lng, firstLocation);
                            for (int z = 0; z < li.size(); z++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng) li.get(z)).latitude));
                                hm.put("lng", Double.toString(((LatLng) li.get(z)).longitude));
                                path.add(hm);
                            }
                        }
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        // List list=getPoints(new LatLng(start_lat,start_lng),new LatLng(end_lat,end_lng),voicetext);
                        List list = decodePoly(polyline);
//
//
                        for(int z=0;z<list.size();z++)
                        {
                            Log.e("got some points","points");
                            String latitude=Double.toString(((LatLng) list.get(z)).latitude);
                            String longitude=Double.toString(((LatLng) list.get(z)).longitude);
                            rp.insertRouteLocation(ID,latitude,longitude);
                        }
//                        for(int z=0;z<list.size();z++)
//                        {
//                            Log.e("got some points","points");
//                            String latitude=Double.toString(((LatLng) list.get(z)).latitude);
//                            String longitude=Double.toString(((LatLng) list.get(z)).longitude);
//                            ContentValues values = new ContentValues();
//                            values.put(RouteContract._ID,RouteContract.ID);
//                            values.put(RouteContract.COLUMN_LATITUDE, latitude);
//                            values.put(RouteContract.COLUMN_LONGITUDE, longitude);
//
//                            long id = RouteProvider.insertRouteLocation(values);
//
//                        } //database walay kaam mai hi koi masla araha
                         for(int y = 0 ; y <list.size()-1; y++) { //since the points are not in equal space..so breaking them into equal spaces
                            List bp = getInBetweenPoints((LatLng) list.get(y), (LatLng) list.get(y + 1));
                            if (bp != null) {
                                for (int l = 0; l < bp.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng) bp.get(l)).latitude));
                                    hm.put("lng", Double.toString(((LatLng) bp.get(l)).longitude));
                                    path.add(hm);
                                    if(!MapsActivity.history_Route)
                                    voicelist.add("no");
                                }
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng) bp.get(bp.size()-1)).latitude));
                                hm.put("lng", Double.toString(((LatLng) bp.get(bp.size()-1)).longitude));
                                path.add(hm);
                                if(!MapsActivity.history_Route)
                                voicelist.add("no");

                            } else {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng) list.get(y)).latitude));
                                hm.put("lng", Double.toString(((LatLng) list.get(y)).longitude));
                                path.add(hm);
                                if(!MapsActivity.history_Route)
                                voicelist.add("no");
                            }
                        }
if(!MapsActivity.history_Route) {
    voicelist.remove(voicelist.size() - 1);
    voicelist.add(voicetext);
}
                    }

                        Double lat = (Double) ((JSONObject) ((JSONObject) jSteps.get(jSteps.length() - 1)).get("end_location")).get("lat");
                        Double lng = (Double) ((JSONObject) ((JSONObject) jSteps.get(jSteps.length() - 1)).get("end_location")).get("lng");
                        List li=addEndLine(lat,lng,secondLocation);

                        // List li = getPoints(new LatLng(lat,lng), secondLocation,voicetext);
                        for (int z = 0; z < li.size(); z++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) li.get(z)).latitude));
                            hm.put("lng", Double.toString(((LatLng) li.get(z)).longitude));
                            path.add(hm);
                        }


                        routes.add(path);
                    }
                Log.e("DB Points",rp.getPointsCount()+"");
                Log.e("DB Address",rp.getRouteCount()+"");
                }

        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Error here","Json exception");
        }
//        catch (Exception e){
//            Log.e("Error here","Json exception");
//        }


        return routes;
    }
    public static List getInBetweenPoints(LatLng point1, LatLng point2){
        double v=0.00001789875;  //25m step
        List points =new ArrayList();
        int lat_steps=(int)Math.abs((point2.latitude-point1.latitude)/v);
        int  lng_steps=(int)Math.abs((point2.longitude-point1.longitude)/v);
        int  count=getmax(lat_steps,lng_steps);
        if(count == 0)
            return  null;
        double lat_inc =(point2.latitude-point1.latitude)/count;
        double lng_inc =(point2.longitude-point1.longitude)/count;
        for(int i=0;i<count; i++){
            points.add(new LatLng(point1.latitude+lat_inc*i,point1.longitude+lng_inc*i) );
        }
        points.add(new LatLng(point1.latitude+lat_inc*(count),point1.longitude+lng_inc*(count)));
        return points;
    }

//    public List getPoints(LatLng start,LatLng end,String voicetext){
//        double v=0.00001789875;  //25m step
//        List points =new ArrayList();
//        int lat_steps=(int)Math.abs((end.latitude-start.latitude)/v);
//        int  lng_steps=(int)Math.abs((end.longitude-start.longitude)/v);
//        int  count=getmax(lat_steps,lng_steps);
//        double lat_inc =(end.latitude-start.latitude)/count;
//        double lng_inc =(end.longitude-start.longitude)/count;
//        for(int i=0;i<count; i++){
//            points.add(new LatLng(start.latitude+lat_inc*i,start.longitude+lng_inc*i) );
//            voicelist.add("no");
//        }
//        points.add(new LatLng(start.latitude+lat_inc*(count),start.longitude+lng_inc*(count)));
//        if(checkRight(voicetext))
//            voicelist.add("Turn Right");
//        else if(checkLeft(voicetext))
//        voicelist.add("Turn Left");
//        else
//        voicelist.add("no");
//
//        return points;
//
//    }
    public static int  getmax(int lat,int lng){
        if(lat>lng){
            return lat;
        }
        else
            return lng;
    }
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    boolean checkRight(String text){
        for (int i = 0; i < text.length()-3; i++)
        {
            if (text.charAt(i) == 'l') {

                if (text.charAt(i+1) == 'e') {

                    if (text.charAt(i+2) == 'f') {

                        if (text.charAt(i+3) == 't')
                        {
                           return true;
                        }
                    }

                }

            }
        }
return  false;
    }
    boolean checkLeft(String text){
        for (int i = 0; i < text.length()-4; i++)
        {
            if (text.charAt(i) == 'r') {

                if (text.charAt(i+1) == 'i') {

                    if (text.charAt(i+2) == 'g') {

                        if (text.charAt(i+3) == 'h')
                        {
                            if(text.charAt(i+4)=='t')
                            return true;
                        }
                    }

                }

            }
        }
return false;
    }

}
