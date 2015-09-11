package com.cherish.busstracker.main;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;

import com.cherish.busstracker.activity.DisplayActivity;
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

import com.cherish.busstracker.R;
import com.cherish.busstracker.lib.Polylines;
import com.cherish.busstracker.parser.objects.Bus;
import com.cherish.busstracker.parser.objects.Stop;
import com.cherish.busstracker.lib.Log;

/**
 * Created by Falcon on 8/29/2015.
 */

//TODO rethink when to use singletons
public class MapController implements OnMapReadyCallback, OnMarkerClickListener {

    /* Map constants */
    public static final BitmapDescriptor BUS_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.bus);
    public static final BitmapDescriptor BUS_INDICATOR_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.bus_indicator_border);

    public static final LatLng CU_LATLNG = new LatLng(40.001894, -105.260184);
    public static final int DEFAULT_MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    public static final int DEFAULT_ZOOM_LEVEL = 14;
    public static final String TAG = "MapController";

    /* Objects */
    private DataModel model;
    private Activity original;
    public GoogleMap map;

    private String lastStop;
    //private Location currentLocation;
    private Marker[] stopMarkers;
    private Marker[] busMarkers;

    public MapController(Activity activity, DataModel model) {
        this.model = model;
        this.original = activity;

        this.lastStop = "";
        this.stopMarkers = null;


        MapFragment mapFragment = (MapFragment) original.getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //public void setLocation(Location location) { this.currentLocation = location; }

    /* Initialize map */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        System.out.println("Initializing map");

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
                    //.snippet("Go buffs")
                    .icon(BUS_INDICATOR_ICON)
                    .anchor(0.5f, 0.5f);

            stopMarkers[i] = map.addMarker(options);
        }
    }

    /* Called by UI thread by interval or stop change
    *  Takes a boolean to indicate if it should redraw buses (ie if it was called by interval)
    */
    public void onUpdate(String stop, boolean redrawBuses) {
        // Simulate click for selected stop only if the stop is new
        if (! lastStop.equals(stop) && stopMarkers != null) {
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
                        .anchor(0.5f, 0.5f);
                busMarkers[i] = map.addMarker(options);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Ignore clicks on buses
        if (! marker.getTitle().equals("Bus")) {
            marker.showInfoWindow();
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);

            ((DisplayActivity)original).updateSelector(marker.getTitle());

        }
        // Return true to always override the default listener
        return true;
    }

}
