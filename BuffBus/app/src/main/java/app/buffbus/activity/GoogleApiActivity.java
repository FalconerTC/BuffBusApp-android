package app.buffbus.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.MapFragment;

import app.buffbus.R;
import app.buffbus.main.MapController;
import app.buffbus.main.ServerConnector;

/*
  Simple class to maintain GoogleAPIClient and display controllers, built from the following
  https://developers.google.com/android/guides/api-client#Starting
 */
public class GoogleApiActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

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
    private DisplayActivity display;
    private LocationRequest locationRequest;
    private Location currentLocation;

    /* Primitives */
    // Bool to track whether the app is already resolving an error
    private boolean resolvingError = false;
    // Bool to track if we are requesting location updates
    private boolean requestingLocationUpdates = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(this.getLocalClassName(), "Creating GoogleApiClient");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Update values from saved bundle
        updateValuesFromBundle(savedInstanceState);

        // Create a GoogleApiClient instance
        buildGoogleApiClient();

        // Initialize map
        map = new MapController();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(map);

        // Initialize display
        display = new DisplayActivity(this);
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

    protected synchronized void buildGoogleApiClient() {
        Log.i(this.getLocalClassName(), "Building GoogleApiClient");
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        // Desired interval for polling
        locationRequest.setInterval(ServerConnector.POLLING_INTERVAL);
        // Fastest possible time it can poll
        locationRequest.setFastestInterval(ServerConnector.POLLING_INTERVAL);

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
        if (!resolvingError) {
            apiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (apiClient.isConnected() && requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (apiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(this.getLocalClassName(), "Connected to GoogleApiClient");

        if (currentLocation != null) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    apiClient);
            System.out.println("Lat: "+ String.valueOf(currentLocation.getLatitude()));
            System.out.println("Long" + String.valueOf(currentLocation.getLongitude()));
        }

        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        System.out.println("Location changed");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(this.getLocalClassName(), "Connection suspended");
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
            ((GoogleApiActivity) getActivity()).onDialogDismissed();
        }
    }

}
