package com.cherish.bustracker.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.cherish.bustracker.R;
import com.cherish.bustracker.activity.DisplayActivity;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.util.threads.GenericThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Falcon on 9/27/2015.
 */
public class LocationManager implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = "LocationManager";

    public static final long FASTEST_INTERVAL = GenericThread.POLLING_INTERVAL / 2;

    /* Error codes */
    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    public static final String DIALOG_ERROR = "error";

    private static LocationManager manager;
    private static Activity original;
    public GoogleApiClient apiClient;
    public Location currentLocation;
    // Bool to track whether the app is already resolving an error
    public boolean resolvingError = false;
    // Bool to track if we are requesting location updates
    public boolean requestingLocationUpdates;
    private LocationRequest locationRequest;

    private LocationManager() {
        this.requestingLocationUpdates = true;
        buildGoogleApiClient();
    }

    public static LocationManager getLocationManager(Activity display) {
        original = display;
        return getLocationManager();
    }

    public static LocationManager getLocationManager() {
        if (manager == null)
            manager = new LocationManager();
        return manager;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        apiClient = new GoogleApiClient.Builder(original)
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

    public void startLocationUpdates() {
        Log.i(TAG, "Starting location updates. Current location: " + currentLocation);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                apiClient, locationRequest, this);
        if (currentLocation != null) {
            ((DisplayActivity) original).updateLocation(currentLocation, true);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);
    }

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
        Log.i(TAG, "Location changed");
        // Only indicate there was an update if location changed
        // Getting the same location constantly implies no network connection
        if (currentLocation != null &&
                currentLocation.getLongitude() != location.getLongitude() &&
                currentLocation.getLatitude() != location.getLatitude()) {
            currentLocation = location;
            // Show popup indicating an update
            Toast t = Toast.makeText(original.getApplicationContext(),
                    original.getResources().getString(R.string.location_updated_message),
                    Toast.LENGTH_SHORT);

            t.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 10, 10);
            t.show();
            // Update map, selector
            ((DisplayActivity) original).updateLocation(currentLocation, false);
        }
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
                result.startResolutionForResult(original, REQUEST_RESOLVE_ERROR);
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

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(original.getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        resolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            LocationManager.getLocationManager().onDialogDismissed();
        }
    }
}
