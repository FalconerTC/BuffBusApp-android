package com.cherish.busstracker.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cherish.busstracker.parser.objects.Bus;
import com.cherish.busstracker.parser.objects.ParsedObject;

/**
 * Created by Falcon on 8/13/2015.
 */
public class BusParser implements Parser{

    String data;

    public BusParser(String data) {
        this.data = data;
    }

    /* Parse stringified JSON into ParsedObjects */
    public ParsedObject[] parse() {
        Bus[] buses = null;
        try {
            JSONArray arr = new JSONArray(data);
            int busesLen = arr.length();
            buses = new Bus[busesLen];
            for (int i = 0; i < busesLen; i++) {
                try {
                    JSONObject current = arr.getJSONObject(i);
                    buses[i] = new Bus();
                    buses[i].latitude = current.getDouble("lat");
                    buses[i].longitude = current.getDouble("lng");
                    buses[i].id = current.getInt("routeID");
                } catch (Exception e) {
                    Log.e("Error", "Something happened while parsing buses array");
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
        return buses;
    }
}
