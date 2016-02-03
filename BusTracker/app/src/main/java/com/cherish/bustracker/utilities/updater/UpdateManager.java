package com.cherish.bustracker.utilities.updater;

import android.app.Activity;

import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.ServerController;
import com.cherish.bustracker.main.view.UIController;

/**
 * Created by Falcon on 9/10/2015.
 */
public class UpdateManager {

    private ServerThread serverUpdater;
    private Activity original;

    public UpdateManager(Activity original) {
        this.original = original;
    }

    /* Initialize Server thread */
    public void createServerThread(ServerController controller, DataModel model, UIController view) {
        serverUpdater = new ServerThread(controller, model, view, original);
        serverUpdater.start();
    }

    /* Resume thread execution */
    public void resume() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onResume();
    }

    /* Pause thread execution */
    public void pause() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onPause();
    }

    /* Stop updates */
    public void stop() {
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onStop();
    }
}
