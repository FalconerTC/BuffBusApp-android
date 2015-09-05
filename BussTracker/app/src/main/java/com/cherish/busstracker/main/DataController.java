package com.cherish.busstracker.main;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cherish.busstracker.activity.DisplayActivity;
import com.cherish.busstracker.parser.objects.Bus;
import com.cherish.busstracker.parser.objects.Route;
import com.cherish.busstracker.parser.objects.Stop;
import com.cherish.busstracker.lib.threads.ControllerThread;

/**
 * Created by Falcon on 8/9/2015.
 */

//TODO refactor naming to Model, reconsider singleton usage
public class DataController {

    private static DataController controller;
    private static ControllerThread updater;
    private Activity original;
    public Intent map;
    private ServerConnector connector;

    private Route route;
    //TODO change stops to be an arraylist ?
    private Stop[] stops;
    private String[] stopNames;
    private ArrayList<Bus> buses;

    // Maintains 3-way state of route (including "unknown" null)
    private Boolean routeActive;

    public Route getRoute() { return route; }
    public String[] getStopNames() { return stopNames; }
    public Stop[] getStops() { return stops; }
    public ArrayList<Bus> getBuses() { return buses; }
    public Boolean getRouteActive(){ return routeActive; }
    public void setRouteActive(Boolean routeActive) { this.routeActive = routeActive; }

    private DataController(Activity original) {
        this.original = original;
        this.connector = ServerConnector.getServerConnector();
        //this.updater = new ControllerThread();
    }

    public static DataController getDataController(Activity original) {
        if (controller == null)
            controller = new DataController(original);
        updater = new ControllerThread(controller);
        return controller;
    }

    public static DataController getDataController() {
        return controller;
    }

    /* Set the current route based on the user selection */
    public void setRoute(String selectedRoute) {
        // Reset route switch
        this.routeActive = null;
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
        // Add relevant buses
        Bus[] bussArr = connector.getBuses();
        this.buses = new ArrayList<>();
        for (int i = 0; i < bussArr.length; i++) {
            if (bussArr[i].id == route.id)
                buses.add(bussArr[i]);
        }


        Stop[] stops = connector.getStops();
        // Convert stops int[] to list
        int[] stopsPrimitive = this.route.stops;
        List<Integer> stopIDs = new ArrayList<>();
        for (int i = 0; i < stopsPrimitive.length; i++)
            stopIDs.add(stopsPrimitive[i]);

        int len = stops.length;
        ArrayList<Stop> currentStops = new ArrayList<>();
        for (int i = 0; i < stopsPrimitive.length; i++)
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

    /* Get the next bus times for the given stop */
    public int[] getNextTimes(String selectedStop) {
        int id = route.id;
        int len = stops.length;
        for (int i = 0; i < len; i++)
            if (stops[i].name.equals(selectedStop))
                return stops[i].busTimes.get(id);
        return null;
    }

    /* Transition from the route view to the bus view */
    public void loadMap(String selectedRoute) {
        setRoute(selectedRoute);
        // Activate thread
        if (!updater.isAlive())
            updater.start();
        else if (updater.isPaused())
            updater.onResume();

        //TODO resume activities instead of recreating
        if (map == null) {
            map = new Intent(original, DisplayActivity.class);
            map.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        original.startActivity(map);
    }


}
