package app.buffbus.main;

import android.app.Activity;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import app.buffbus.activity.DisplayActivity;
import app.buffbus.utils.ServerConnector;
import app.buffbus.utils.parser.objects.Route;
import app.buffbus.utils.parser.objects.Stop;

/**
 * Created by Falcon on 8/9/2015.
 */
public class MapController {

    private static MapController controller;
    private Activity original;
    private Intent map;
    private ServerConnector connector;

    private Route route;
    private Stop[] stops;
    private String[] stopNames;

    public Route getRoute() { return route; }
    public String[] getStopNames() { return stopNames; }

    private MapController(Activity original) {
        this.original = original;
        this.connector = ServerConnector.getServerConnector();
    }


    public static MapController getMapController(Activity original) {
        if (controller == null)
            controller = new MapController(original);
        return controller;
    }

    public static MapController getMapController() {
        return controller;
    }

    /* Set the current route based on the user selection */
    public void setRoute(String selectedRoute) {
        // Load route from name
        Route[] routes = this.connector.getRoutes();
        int routeLen = routes.length;
        for (int i = 0; i < routeLen; i++) {
            if (routes[i].name.equals(selectedRoute)) {
                this.route = routes[i];
                break;
            }
        }

        updateStopData();
        // Load stop names
        int len = stops.length;
        this.stopNames = new String[len];
        for (int i = 0; i < len; i++) {
            this.stopNames[i] = stops[i].name;
        }
    }

    //TODO Make this more efficient with SparseArray
    /* Update the stops based on the current route */
    public void updateStopData() {
        Stop[] stops = this.connector.getStops();
        // Convert stops int[] to list
        int[] stopsPrimative = this.route.stops;
        List<Integer> stopIDs = new ArrayList<>();
        for (int i = 0; i < stopsPrimative.length; i++)
            stopIDs.add(stopsPrimative[i]);

        int len = stops.length;
        ArrayList<Stop> currentStops = new ArrayList<>();
        for (int i = 0; i < stopsPrimative.length; i++)
            currentStops.add(i, null);
        // Match stopIDs with actual stops
        for (int i = 0; i < len; i++) {
            int index = stopIDs.indexOf(stops[i].id);
            if (index >= 0) {
                currentStops.set(index, stops[i]);
            }
        }
        // Check for nulls (when a route quotes a stop that does not exist)
        currentStops.removeAll(Collections.singleton(null));
        this.stops = currentStops.toArray(new Stop[currentStops.size()]);
    }

    /* */
    public void loadMap(String selectedRoute) {
        System.out.println("Loading map");
        setRoute(selectedRoute);
        if (map == null)
            map = new Intent(original, DisplayActivity.class);
        original.startActivity(map);
    }
}
