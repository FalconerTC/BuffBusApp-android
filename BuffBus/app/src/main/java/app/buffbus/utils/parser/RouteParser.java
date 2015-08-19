package app.buffbus.utils.parser;

import android.util.Log;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.buffbus.utils.parser.objects.ParsedObject;
import app.buffbus.utils.parser.objects.Route;

/**
 * Created by Falcon on 8/14/2015.
 */
public class RouteParser implements Parser{

    String data;

    public RouteParser(String data) {
        this.data = data;
    }

    /* Parse stringified JSON into ParsedObjects */
    public SparseArray<? extends ParsedObject> parse() {
        //Route[] routes = null;
        SparseArray<Route> routes = null;
        try {
            JSONArray arr = new JSONArray(data);
            int routesLen = arr.length();
            //routes = new Route[routesLen];
            Route route;
            routes = new SparseArray<>();
            for (int i = 0; i < routesLen; i++) {
                try {
                    JSONObject current = arr.getJSONObject(i);
                    route = new Route();
                    //routes[i] = new Route();
                    //routes[i].id = current.getInt("id");
                    Integer id = current.getInt("id");
                    route.name = current.getString("name");
                    // Read in int array of stops
                    JSONArray stops = current.getJSONArray("stops");
                    int stopsLen = stops.length();
                    int[] stopsArr = new int[stopsLen];
                    for (int j = 0; j < stopsLen; j++)
                        stopsArr[j] = stops.getInt(j);
                    route.stops = stopsArr;
                    routes.append(id, route);
                } catch (Exception e) {
                    Log.e("Error", "Something happened while parsing routes array");
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("JSON error", "Unable to parse JSON " + data);
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Error", "Something happened");
            e.printStackTrace();
        }

        return routes;
    }


}
