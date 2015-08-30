package app.buffbus.main;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.buffbus.R;

/**
 * Created by Falcon on 8/29/2015.
 */

//TODO rethink when to use singletons
public class MapController implements OnMapReadyCallback {

    /* Map constants */
    public static final LatLng CU_LATLNG = new LatLng(40.001894, -105.260184);
    public static final int DEFAULT_MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    public static final int DEFAULT_ZOOM_LEVEL = 13;

    /* Objects */
    private DataController model;
    private Activity original;
    public GoogleMap map;

    public MapController(Activity activity, DataController model) {
        this.model = model;
        this.original = activity;

        MapFragment mapFragment = (MapFragment) original.getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /* Initialize map */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        System.out.println("Initializing map");

        map.setMapType(DEFAULT_MAP_TYPE);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CU_LATLNG, DEFAULT_ZOOM_LEVEL));

        map.addMarker(new MarkerOptions()
                .position(CU_LATLNG)
                .title("CU")
                .snippet("Go buffs"));
    }

}
