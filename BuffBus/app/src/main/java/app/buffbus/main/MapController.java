package app.buffbus.main;

import android.app.Activity;
import android.content.Intent;

import app.buffbus.activity.DisplayActivity;
import app.buffbus.utils.ServerConnector;
import app.buffbus.utils.parser.objects.Route;

/**
 * Created by Falcon on 8/9/2015.
 */
public class MapController {

    private static MapController controller;
    private Activity original;
    private Intent map;
    private ServerConnector connector;

    public Route route;


    private MapController(Activity original) {
        this.original = original;
        this.connector = ServerConnector.getServerConnector();
    }


    public static MapController getMapController(Activity original) {
        if (controller == null)
            controller = new MapController(original);
        return controller;
    }

    public static MapController getMapController() {
        return controller;
    }

    /* */
    public void setRoute(String selectedRoute) {
        // Load route from name
/*        Route[] routes = this.connector.getRoutes();
        int routeLen = routes.length;
        for (int i = 0; i > routeLen; i++) {
            if (routes[i].name.equals(selectedRoute)) {
                this.route = routes[i];
                break;
            }
        }*/
    }

    /* */
    public void loadMap(String selectedRoute) {
        setRoute(selectedRoute);
        if (map == null)
            map = new Intent(original, DisplayActivity.class);
        original.startActivity(map);
    }
}
