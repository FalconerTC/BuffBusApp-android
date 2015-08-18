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
    private ServerConnector connector;

    public Route route;


    private MapController(Activity original, String selectedRoute) {
        this.original = original;
        this.connector = ServerConnector.getServerConnector();

        // Load route from name
        Route[] routes = this.connector.routes;
        int routeLen = routes.length;
        for (int i = 0; i > routeLen; i++) {
            if (routes[i].name.equals(selectedRoute)) {
                this.route = routes[i];
                break;
            }
        }
    }

    public static MapController getMapController(Activity original, String selectedRoute) {
        if (controller == null)
            controller = new MapController(original, selectedRoute);
        return controller;
    }

    public static MapController getMapController() {
        return controller;
    }

    /* */
    public void loadMap() {
        Intent intent = new Intent(original, DisplayActivity.class);
        original.startActivity(intent);
    }
}
