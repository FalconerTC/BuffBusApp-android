package com.cherish.bustracker.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Falcon on 6/5/2016.
 * Maintains a mapping of which routes are in what order on the main page
 * Maps route name to vertical button index
 */
public class RouteData {
    public static final DefaultHashMap<String, Integer> ROUTES_MAP;

    static {
        ROUTES_MAP = new DefaultHashMap<>();
        ROUTES_MAP.put("Buff Bus", 0);
        ROUTES_MAP.put("Will Vill Football", 0);
        ROUTES_MAP.put("Will Vill Basketball", 0);
        ROUTES_MAP.put("HOP Clockwise", 1);
        ROUTES_MAP.put("HOP Counter Clockwise", 2);
        ROUTES_MAP.put("Athens Court Shuttle", 3);
        ROUTES_MAP.put("Late Night Black", 4);
        ROUTES_MAP.put("Late Night Silver", 5);
        ROUTES_MAP.put("Late Night Gold", 6);
        ROUTES_MAP.put("Discovery Express Loop", 7);
    }

    /*
     * Slightly modified HashMap that returns a given default value
     * if the requested value is not in the map
     * Modified from http://stackoverflow.com/questions/7519339
     */
    public static class DefaultHashMap<K,V> extends HashMap<K,V> {
        public V get(Object k, V defaultValue) {
            return containsKey(k) ? super.get(k) : defaultValue;
        }
    }

}
