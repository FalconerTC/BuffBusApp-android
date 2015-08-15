package app.buffbus.utils.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public ParsedObject[] parse() {
        Route[] routes = null;
        try {
            JSONArray arr = new JSONArray(data);
            int routesLen = arr.length();
            routes = new Route[routesLen];
            for (int i = 0; i < routesLen; i++) {
                try {
                    JSONObject current = arr.getJSONObject(i);
                    routes[i] = new Route();
                    routes[i].id = current.getInt("id");
                    routes[i].name = current.getString("name");
                    // Read in int array of stops
                    JSONArray stops = current.getJSONArray("stops");
                    int stopsLen = stops.length();
                    int[] stopsArr = new int[stopsLen];
                    for (int j = 0; j < stopsLen; j++)
                        stopsArr[j] = stops.getInt(j);
                    routes[i].stops = stopsArr;
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
