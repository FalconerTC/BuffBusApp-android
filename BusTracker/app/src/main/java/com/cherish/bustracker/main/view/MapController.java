package com.cherish.bustracker.main.view;

import android.app.Activity;
import android.graphics.Color;

import com.cherish.bustracker.R;
import com.cherish.bustracker.activity.DisplayActivity;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.lib.Polylines;
import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.parser.objects.Bus;
import com.cherish.bustracker.parser.objects.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

/**
 * Created by Falcon on 8/29/2015.
 */

public class MapController implements OnMapReadyCallback, OnMarkerClickListener {
    public static final String TAG = "MapController";

    /* Map constants */
    public static final BitmapDescriptor BUS_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.bus);
    public static final BitmapDescriptor BUS_INDICATOR_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.bus_indicator_border);
    public static final BitmapDescriptor BUS_CLOSEST_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.nearest_stop_indicator);

    public static final LatLng CU_LATLNG = new LatLng(40.001894, -105.260184);
    public static final int DEFAULT_MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    public static final int DEFAULT_ZOOM_LEVEL = 14;

    /* Objects */
    public GoogleMap map;
    private DataModel model;
    private Activity original;
    private String lastStop;
    private Marker[] stopMarkers;
    private Marker[] busMarkers;
    private String closestStop;
    private String oldClosestStop;

    public MapController(Activity activity, DataModel model) {
        this.model = model;
        this.original = activity;

        this.lastStop = "";
        this.stopMarkers = null;
        this.closestStop = "";

        MapFragment mapFragment = (MapFragment) original.getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setClosestStop(String stopID) {
        this.closestStop = stopID;
        if ((!closestStop.equals(oldClosestStop)) && stopMarkers != null) {
            drawClosestStop();
        }
    }

    /* Initialize map */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        Log.i(TAG, "Initializing map");

        map.setMapType(DEFAULT_MAP_TYPE);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CU_LATLNG, DEFAULT_ZOOM_LEVEL));

        map.setOnMarkerClickListener(this);

        initializeData();
    }

    /* Called on create to add route information */
    public void initializeData() {
        Log.i(TAG, "Initializing data");

        // Draw the polyline for the current route
        int route = model.getRoute().id;
        String line_data = Polylines.POLYLINE_MAP.get(route);
        if (line_data != null) {
            map.addPolyline(new PolylineOptions()
                    .addAll(PolyUtil.decode(line_data))
                    .width(20)
                    .color(Color.BLACK));
        }

        Stop[] stops = model.getStops();
        int len = stops.length;
        stopMarkers = new Marker[len];
        for (int i = 0; i < len; i++) {
            LatLng pos = new LatLng(stops[i].latitude, stops[i].longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(pos)
                    .title(stops[i].name)
                    .icon(BUS_INDICATOR_ICON)
                    .anchor(0.5f, 0.5f);

            stopMarkers[i] = map.addMarker(options);
        }
    }

    /* Called by UI thread by interval or stop change
    *  Takes a boolean to indicate if it should redraw buses (if it was called by interval)
    */
    public void onUpdate(String stop, boolean redrawBuses) {
        // Simulate click for selected stop only if the stop is new
        if (!lastStop.equals(stop) && stopMarkers != null) {
            lastStop = stop;
            int len = stopMarkers.length;
            for (int i = 0; i < len; i++) {
                if (stopMarkers[i].getTitle().equals(stop)) {
                    onMarkerClick(stopMarkers[i]);
                    break;
                }
            }
        }
        // Draw updated bus locations
        if (redrawBuses)
            drawBuses();
    }

    /* Replace the closest stop marker with a green marker */
    public void drawClosestStop() {
        int len = stopMarkers.length;
        for (int i = 0; i < len; i++) {
            // Reset old stop icon
            if (stopMarkers[i].getTitle().equals(oldClosestStop)) {
                // Remove and recreate marker to avoid showing an empty snippet
                MarkerOptions options = new MarkerOptions()
                        .position(stopMarkers[i].getPosition())
                        .title(stopMarkers[i].getTitle())
                        .icon(BUS_INDICATOR_ICON)
                        .anchor(0.5f, 0.5f);
                stopMarkers[i].remove();
                stopMarkers[i] = map.addMarker(options);
                // Set new closest stop icon
            } else if (stopMarkers[i].getTitle().equals(closestStop)) {
                stopMarkers[i].setSnippet(original.getResources()
                        .getString(R.string.closest_stop_message));
                stopMarkers[i].setIcon(BUS_CLOSEST_ICON);
            }
        }
        oldClosestStop = closestStop;
    }

    /* Draw all running buses to the map */
    private void drawBuses() {
        Log.i(TAG, "Drawing bus location");
        // Add updated markers
        ArrayList<Bus> buses = model.getBuses();
        if (buses != null && map != null) {
            // Remove any existing markers
            if (busMarkers != null) {
                int len = busMarkers.length;
                for (int i = 0; i < len; i++)
                    busMarkers[i].remove();
            }

            int len = buses.size();
            busMarkers = new Marker[len];
            for (int i = 0; i < len; i++) {
                LatLng pos = new LatLng(buses.get(i).latitude, buses.get(i).longitude);
                MarkerOptions options = new MarkerOptions()
                        .position(pos)
                        .title("Bus")
                        .icon(BUS_ICON)
                        .flat(false)
                        .anchor(0.5f, 0.5f);
                busMarkers[i] = map.addMarker(options);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Ignore clicks on buses
        if (!marker.getTitle().equals("Bus")) {
            marker.showInfoWindow();
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);

            ((DisplayActivity) original).updateSelector(marker.getTitle());
        }
        // Return true to always override the default listener
        return true;
    }

}
