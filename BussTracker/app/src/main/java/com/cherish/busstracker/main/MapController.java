package com.cherish.busstracker.main;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.NumberPicker;

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

    /* Objects */
    private DataController model;
    private Activity original;
    private NumberPicker stopSelector;
    public GoogleMap map;

    private String lastStop;
    private Location currentLocation;
    private Marker[] stopMarkers;
    private Marker[] busMarkers;

    public MapController(Activity activity, DataController model) {
        this.model = model;
        this.original = activity;
        stopSelector = (NumberPicker)original.findViewById(R.id.stopPicker);

        this.lastStop = "";
        this.currentLocation = null;
        this.stopMarkers = null;


        MapFragment mapFragment = (MapFragment) original.getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setLocation(Location location) { this.currentLocation = location; }

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
    //TODO change marker z-indexes to be consistent
    public void initializeData() {
        Log.i("MapController", "Initializing data");

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

    /* Called by UI thread by interval or stop change */
    public void onUpdate(String stop) {
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
        drawBuses();
    }

    /* Draw all running buses to the map */
    //TODO look into animating the bus transition maybe ?
    private void drawBuses() {
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
    //TODO add a real return type
    public boolean onMarkerClick(Marker marker) {
        // Ignore clicks on buses
        if (! marker.getTitle().equals("Bus")) {
            marker.showInfoWindow();
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);
            if (stopSelector != null) {
                String[] stops = stopSelector.getDisplayedValues();
                for (int i = 0; i < stops.length; i++) {
                    if (marker.getTitle().equals(stops[i])) {
                        System.out.println("THINGS ARE HAPPENING");
                        ((DisplayActivity)original).updateSelector(i);
                        //stopSelector.setValue(i);
                    }
                }
            }
        }
        // Return true to always override the default listener
        return true;
    }

}
