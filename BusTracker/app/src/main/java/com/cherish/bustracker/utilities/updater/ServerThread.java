package com.cherish.bustracker.utilities.updater;

import android.app.Activity;

import com.cherish.bustracker.activity.MainActivity;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.ServerController;
import com.cherish.bustracker.main.view.UIController;

/**
 * Created by Falcon on 9/5/2015.
 */
public class ServerThread extends GenericThread {
    public static final String TAG = "ServerThread";

    private Activity original;
    private ServerController controller;
    private DataModel model;
    private UIController view;
    private boolean fullUpdate;

    public ServerThread(ServerController controller, DataModel model, UIController view, Activity original) {
        super();
        this.controller = controller;
        this.model = model;
        this.view = view;
        this.original = original;
        this.fullUpdate = true;
    }

    /* Used by MainActivity for a single server call for route info */
    public ServerThread(ServerController controller, Activity original) {
        super();
        this.controller = controller;
        this.original = original;
        this.fullUpdate = false;
        // Start with rapid polling until a server response is received
        this.currentPollingInterval = RAPID_POLLING_INTERVAL;
    }

    public void onRun() {
        Log.i(TAG, "Executing...");
        boolean result;

        result = controller.update();
        if (result) {
            // Slow to standard polling interval
            this.currentPollingInterval = DEFAULT_POLLING_INTERVAL;
            // Notify activity if it exists
            if (!fullUpdate)
                original.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) original).onNotify(controller);
                    }
                });
            else {
                // Process data
                result = model.update();
                if (result) {
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
    }
}
