package com.cherish.busstracker.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.cherish.busstracker.lib.threads.GenericThread;
import com.cherish.busstracker.lib.threads.UIThread;
import com.cherish.busstracker.main.ThreadManager;
import com.cherish.busstracker.parser.objects.Stop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.cherish.busstracker.R;
import com.cherish.busstracker.main.DataModel;
import com.cherish.busstracker.main.view.MapController;
import com.cherish.busstracker.main.view.UIController;
import com.cherish.busstracker.lib.Log;


/*
  Simple class to maintain GoogleAPIClient and display controllers, built from the following
  https://developers.google.com/android/guides/api-client#Starting
 */
public class DisplayActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    public static final long FASTEST_INTERVAL = GenericThread.POLLING_INTERVAL / 2;
    public static final String TAG = "DisplayActivity";

    /* Error codes */
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;

    /* Bundle keys */
    private static final String LOCATION_KEY = "location-key";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String DIALOG_ERROR = "dialog_error";

    /* Objects */
    private GoogleApiClient apiClient;
    private MapController map;
    private ThreadManager manager;
    private UIController display;
    private DataModel model;
    private LocationRequest locationRequest;
    private Location currentLocation;

    /* Primitives */
    // Bool to track whether the app is already resolving an error
    private boolean resolvingError = false;
    // Bool to track if we are requesting location updates
    private boolean requestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(this.getLocalClassName(), "Creating GoogleApiClient");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        String route = intent.getStringExtra(MainActivity.SELECTED_ROUTE);

        this.requestingLocationUpdates = true;

        // Update values from saved bundle
        updateValuesFromBundle(savedInstanceState);

        // Create a GoogleApiClient instance
        buildGoogleApiClient();

        // Create manager for threads
        manager = new ThreadManager(this);

        // Initialize data model
        model = new DataModel(route);
        manager.createModelThread(model);

        // Initialize map
        map = new MapController(this, model);

        // Initialize display controller and map
        display = new UIController(this, model, map);
        manager.createUIThread(display);

    }

    /* Updates fields based on stored data passed on create */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(STATE_RESOLVING_ERROR)) {
                resolvingError = savedInstanceState.getBoolean(STATE_RESOLVING_ERROR);
            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
        }
    }

    /* Proxy update requests to the UIController */
    public void updateSelector(String selectedStop) {
        display.changeSelectedStop(selectedStop);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(this.getLocalClassName(), "Building GoogleApiClient");
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /* Define request parameters for current location */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        // Desired interval for polling
        locationRequest.setInterval(GenericThread.POLLING_INTERVAL);
        // Fastest possible time it can poll
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                apiClient, locationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Starting");

        if (!resolvingError) {
            apiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Resuming");

        if (apiClient.isConnected() && requestingLocationUpdates) {
            startLocationUpdates();
        }
        manager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Pausing");

        if (apiClient.isConnected()) {
            stopLocationUpdates();
        }
        manager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Stopping");

        apiClient.disconnect();
    }

/*    @Override
    public void onBackPressed() {
        Log.i(TAG, "Back button pressed");
        //moveTaskToBack(true);

        Intent a = new Intent(this, MainActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(a);
    }*/

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (currentLocation == null) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    apiClient);
        }

        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.i(TAG, "Location changed");
        Toast t = Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM|Gravity.RIGHT, 0, 0);
        t.show();
        String closestID = findClosestStop();
        map.setClosestStop(closestID);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (resolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                resolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                apiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            resolvingError = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!apiClient.isConnecting() &&
                        !apiClient.isConnected()) {
                    apiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_RESOLVING_ERROR, resolvingError);
        outState.putParcelable(LOCATION_KEY, currentLocation);
        super.onSaveInstanceState(outState);
    }

    /* Finds the name for the stop closest to the current location */
    public String findClosestStop() {
        Stop[] stops = model.getStops();
        float closestDistance = Float.MAX_VALUE;
        String closestStop = "";
        int len = (stops != null) ? stops.length : 0;
        for (int i = 0; i < len; i++) {
            Location loc = new Location("");
            loc.setLongitude(stops[i].longitude);
            loc.setLatitude(stops[i].latitude);
            float distance = currentLocation.distanceTo(loc);
            if (distance < closestDistance){
                closestDistance = distance;
                closestStop = stops[i].name;
            }
        }
        return closestStop;
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        resolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((DisplayActivity) getActivity()).onDialogDismissed();
        }
    }

}
