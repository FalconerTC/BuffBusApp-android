package com.cherish.bustracker.main;

import com.cherish.bustracker.utilities.parser.objects.Bus;
import com.cherish.bustracker.utilities.parser.objects.Route;
import com.cherish.bustracker.utilities.parser.objects.Stop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Falcon on 8/9/2015.
 */

public class DataModel {

    private ServerController connector;
    private Route route;
    //TODO change stops to be an arraylist ?
    private Stop[] stops;
    private String[] stopNames;
    private ArrayList<Bus> buses;
    private String selectedRoute;

    public Route getRoute() { return route; }
    public String[] getStopNames() { return stopNames; }
    public Stop[] getStops() { return stops; }
    public ArrayList<Bus> getBuses() { return buses; }

    public DataModel(ServerController connector, String route) {
        this.connector = connector;
        this.selectedRoute = route;
        setRoute(route);
    }

    /* Set the current route based on the user selection */
    public void setRoute(String selectedRoute) {
        // Load route from name
        Route[] routes = connector.getRoutes();
        if (routes != null) {
            int routeLen = routes.length;
            for (int i = 0; i < routeLen; i++) {
                if (routes[i].name.equals(selectedRoute)) {
                    this.route = routes[i];
                    break;
                }
            }
            update();
        }
    }

    //TODO Make this more efficient with SparseArray
    /* Update the stops based on the current route */
    public boolean update() {
        if (this.route == null) {
            setRoute(selectedRoute);
        }
        // Add relevant buses
        Bus[] busArr = connector.getBuses();
        if (busArr != null) {
            this.buses = new ArrayList<>();
            for (int i = 0; i < busArr.length; i++) {
                if (busArr[i].id == route.id)
                    buses.add(busArr[i]);
            }

            Stop[] stops = connector.getStops();
            if (stops != null) {
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

                // Create once an array of stop names
                if (this.stopNames == null) {
                    len = this.stops.length;
                    this.stopNames = new String[len];
                    for (int i = 0; i < len; i++) {
                        this.stopNames[i] = this.stops[i].name;
                    }
                }
                if (this.stops != null && this.stopNames != null)
                    return true;
            }
        }
        return false;
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

}
