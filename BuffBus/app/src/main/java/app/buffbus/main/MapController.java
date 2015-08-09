package app.buffbus.main;

import android.app.Activity;

/**
 * Created by Falcon on 8/9/2015.
 */
public class MapController {

    private Activity original;
    private static MapController controller;

    private MapController(Activity original) {
        this.original = original;
    }

    public static MapController getBussSelector(Activity original) {
        if (controller == null)
            controller = new MapController(original);
        return controller;
    }
}
