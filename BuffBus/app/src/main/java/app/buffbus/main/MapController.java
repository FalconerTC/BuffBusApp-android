package app.buffbus.main;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import app.buffbus.R;
import app.buffbus.lib.Polylines;
import app.buffbus.parser.objects.Stop;

/**
 * Created by Falcon on 8/29/2015.
 */

//TODO rethink when to use singletons
public class MapController implements OnMapReadyCallback, OnMarkerClickListener {

    /* Map constants */
    public static final LatLng CU_LATLNG = new LatLng(40.001894, -105.260184);
    public static final BitmapDescriptor BUS_INDICATOR_ICON
            = BitmapDescriptorFactory.fromResource(R.drawable.bus_indicator_border);
    public static final int DEFAULT_MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    public static final int DEFAULT_ZOOM_LEVEL = 14;

    /* Objects */
    private DataController model;
    private Activity original;
    public GoogleMap map;

    private String lastStop;
    private Location currentLocation;
    private Marker[] stopMarkers;

    public MapController(Activity activity, DataController model) {
        this.model = model;
        this.original = activity;

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
    public void initializeData() {
        Log.i("MapController", "Initializing data");

        // Draw the polyline for the current route
        String route = model.getRoute().name;
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

        drawBuses();
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

    private void drawBuses() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("MapController", "Marker clicked");
        marker.showInfoWindow();

        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);

        return false;
    }

}
