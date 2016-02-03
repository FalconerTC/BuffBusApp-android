package com.cherish.bustracker.util.threads;

import android.app.Activity;

import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.ServerConnector;
import com.cherish.bustracker.main.view.UIController;

/**
 * Created by Falcon on 9/10/2015.
 */
public class ThreadManager {

    private ServerThread serverUpdater;
    private Activity original;

    public ThreadManager(Activity original) {
        this.original = original;
    }

    // Initialize Server thread
    public void createServerThread(ServerConnector controller, DataModel model, UIController view) {
        serverUpdater = new ServerThread(controller, model, view, original);
        serverUpdater.start();
    }

    /* Resume thread execution */
    public void onResume() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onResume();
    }

    /* Pause thread execution */
    public void onPause() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onPause();
    }

    /* Stop threads */
    public void onStop() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onStop();
    }
}
