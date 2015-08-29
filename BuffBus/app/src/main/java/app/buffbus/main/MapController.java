package app.buffbus.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.buffbus.activity.DisplayActivity;
import app.buffbus.activity.GoogleApiActivity;

/**
 * Created by Falcon on 8/29/2015.
 */

//TODO rethink when to use singletons
public class MapController implements OnMapReadyCallback {

    /* Objects */
    private static MapController controller;
    public GoogleMap map;

    private MapController() {
    }

    public static MapController getMapController() {
        if (controller == null)
            controller = new MapController();
        return controller;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        System.out.println("Initializing map");
        LatLng CU = new LatLng(40.001894, -105.260184);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CU, 13));

        map.addMarker(new MarkerOptions()
                .position(CU)
                .title("CU")
                .snippet("Go buffs"));
    }

}
