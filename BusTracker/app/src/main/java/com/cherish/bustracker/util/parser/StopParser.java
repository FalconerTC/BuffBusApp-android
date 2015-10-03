package com.cherish.bustracker.util.parser;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.util.parser.objects.ParsedObject;
import com.cherish.bustracker.util.parser.objects.Stop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Falcon on 8/15/2015.
 */
public class StopParser implements Parser {

    String data;

    public StopParser(String data) {
        this.data = data;
    }

    /* Parse stringified JSON into ParsedObjects */
    public ParsedObject[] parse() {
        Stop[] stops = null;
        try {
            JSONArray arr = new JSONArray(data);
            int stopsLen = arr.length();
            stops = new Stop[stopsLen];
            for (int i = 0; i < stopsLen; i++) {
                try {
                    JSONObject current = arr.getJSONObject(i);
                    stops[i] = new Stop();
                    stops[i].id = current.getInt("id");
                    stops[i].name = current.getString("name");
                    stops[i].latitude = current.getDouble("lat");
                    stops[i].longitude = current.getDouble("lng");
                    // Interpret bus times object
                    JSONObject timesObj = current.getJSONObject("nextBusTimes");
                    if (timesObj != null) {
                        try {
                            Iterator<String> keys = timesObj.keys();
                            while (keys.hasNext()) {
                                int key = Integer.parseInt(String.valueOf(keys.next()));
                                JSONArray times = timesObj.getJSONArray(Integer.toString(key));
                                int timesLen = times.length();
                                int[] timesArr = new int[timesLen];
                                for (int j = 0; j < timesLen; j++)
                                    timesArr[j] = times.getInt(j);
                                stops[i].busTimes.put(key, timesArr);
                            }
                        } catch (Exception e) {
                            Log.e("Error", "Something happened while parsing nextBusTimes object");
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Something happened while parsing stops array");
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
        return stops;
    }
}
