package com.cherish.bustracker.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.cherish.bustracker.R;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.LocationManager;
import com.cherish.bustracker.main.ServerConnector;
import com.cherish.bustracker.main.ThreadManager;
import com.cherish.bustracker.main.view.MapController;
import com.cherish.bustracker.main.view.UIController;
import com.cherish.bustracker.parser.objects.Stop;

/*
  Simple class to maintain GoogleAPIClient and display controllers, built from the following
  https://developers.google.com/android/guides/api-client#Starting
 */
public class DisplayActivity extends FragmentActivity {
    public static final String TAG = "DisplayActivity";

    private MapController map;
    private ThreadManager threads;
    private LocationManager locManager;
    private ServerConnector connector;
    private UIController display;
    private DataModel model;
    private Toast routeInformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        String route = intent.getStringExtra(MainActivity.SELECTED_ROUTE);

        // Show pop-up with their selected route
        routeInformer = Toast.makeText(getApplicationContext(),
                "Route selected: " + route, Toast.LENGTH_SHORT);
        routeInformer.show();


        // Create a GoogleApiClient instance
        locManager = LocationManager.getLocationManager(this);

        // Create manager for threads
        threads = new ThreadManager(this);

        // Initialize server connector
        connector = ServerConnector.getServerConnector();
        threads.createServerThread(connector);

        // Initialize data model
        model = new DataModel(connector, route);
        threads.createModelThread(model);

        // Initialize map
        map = new MapController(this, model);

        // Initialize display controller and map
        display = new UIController(this, model, map);
        threads.createUIThread(display);
    }

    /* Proxy update requests to the UIController */
    public void updateSelector(String selectedStop) {
        display.changeSelectedStop(selectedStop);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationManager.REQUEST_RESOLVE_ERROR) {
            locManager.resolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!locManager.apiClient.isConnecting() &&
                        !locManager.apiClient.isConnected()) {
                    locManager.apiClient.connect();
                }
            }
        }
    }

    /* Finds the name for the stop closest to the current location */
    public String findClosestStop(Location currentLocation) {
        Stop[] stops = model.getStops();
        float closestDistance = Float.MAX_VALUE;
        String closestStop = "";
        int len = (stops != null) ? stops.length : 0;
        for (int i = 0; i < len; i++) {
            Location loc = new Location("");
            loc.setLongitude(stops[i].longitude);
            loc.setLatitude(stops[i].latitude);
            float distance = currentLocation.distanceTo(loc);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestStop = stops[i].name;
            }
        }
        return closestStop;
    }

    /* Send new stop information to map and selector */
    public void updateLocation(Location currentLocation, boolean initialLoad) {
        Log.i(TAG, "Sending updated location");
        String closestStop = findClosestStop(currentLocation);
        map.setClosestStop(closestStop);
        if (initialLoad)
            updateSelector(closestStop);
    }

    /* Activity actions */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Starting");
        if (!locManager.resolvingError) {
            locManager.apiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Resuming");

        if (locManager.apiClient.isConnected() && locManager.requestingLocationUpdates) {
            locManager.startLocationUpdates();
        }
        threads.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Pausing");

        if (locManager.apiClient.isConnected()) {
            locManager.stopLocationUpdates();
        }
        routeInformer.cancel();
        threads.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Stopping");

        locManager.apiClient.disconnect();
    }

}
