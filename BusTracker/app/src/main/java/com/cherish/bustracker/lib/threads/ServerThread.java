package com.cherish.bustracker.lib.threads;

import android.app.Activity;

import com.cherish.bustracker.activity.MainActivity;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.ServerConnector;

/**
 * Created by Falcon on 9/5/2015.
 */
public class ServerThread extends GenericThread {
    public static final String TAG = "ServerThread";

    private MainActivity original;
    private ServerConnector connector;

    public ServerThread(ServerConnector connector) {
        super();
        this.connector = connector;
    }

    public ServerThread(ServerConnector connector, Activity original) {
        super();
        this.connector = connector;
        this.original = (MainActivity) original;
    }

    public void onRun() {
        Log.i(TAG, "Executing...");
        connector.update();
        // Notify activity if it exists
        if (original != null)
            original.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    original.onNotify(connector.getRoutes());
                }
            });
    }
}
