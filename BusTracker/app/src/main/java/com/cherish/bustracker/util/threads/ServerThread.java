package com.cherish.bustracker.util.threads;

import android.app.Activity;

import com.cherish.bustracker.activity.DisplayActivity;
import com.cherish.bustracker.activity.MainActivity;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.ServerConnector;
import com.cherish.bustracker.main.view.UIController;

/**
 * Created by Falcon on 9/5/2015.
 */
public class ServerThread extends GenericThread {
    public static final String TAG = "ServerThread";

    private Activity original;
    private ServerConnector controller;
    private DataModel model;
    private UIController view;
    private boolean fullUpdate;

    public ServerThread(ServerConnector controller, DataModel model, UIController view, Activity original) {
        super();
        this.controller = controller;
        this.model = model;
        this.view = view;
        this.original = original;
        this.fullUpdate = true;
    }

    /* Used by MainActivity for a single server call for route info */
    public ServerThread(ServerConnector controller, Activity original) {
        super();
        this.controller = controller;
        this.original = original;
        this.fullUpdate = false;
    }

    public void onRun() {
        Log.i(TAG, "Executing...");
        // Send server request
        controller.update();

        // Notify activity if it exists
        if (!fullUpdate)
            original.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)original).onNotify(controller.getRoutes());
                }
            });
        else {
            // Process data
            model.update();
            // Update view
            original.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.update(true);
                }
            });
        }
    }
}
