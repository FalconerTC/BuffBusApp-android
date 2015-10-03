package com.cherish.bustracker.util.threads;

import android.app.Activity;

import com.cherish.bustracker.main.DataModel;
import com.cherish.bustracker.main.ServerConnector;
import com.cherish.bustracker.main.view.UIController;
import com.cherish.bustracker.util.threads.ModelThread;
import com.cherish.bustracker.util.threads.ServerThread;
import com.cherish.bustracker.util.threads.UIThread;

/**
 * Created by Falcon on 9/10/2015.
 */
public class ThreadManager {

    private UIThread UIUpdater;
    private ModelThread modelUpdater;
    private ServerThread serverUpdater;
    private Activity original;

    public ThreadManager(Activity original) {
        this.original = original;
    }

    // Initialize UI thread
    public void createUIThread(UIController view) {
        UIUpdater = new UIThread(view, original);
        UIUpdater.start();
    }

    // Initialize Model thread
    public void createModelThread(DataModel model) {
        modelUpdater = new ModelThread(model);
        modelUpdater.start();
    }

    // Initialize Server thread
    public void createServerThread(ServerConnector controller) {
        serverUpdater = new ServerThread(controller);
        serverUpdater.start();
    }

    /* Resume thread execution */
    public void onResume() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onResume();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onResume();
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onResume();
    }

    /* Pause thread execution */
    public void onPause() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onPause();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onPause();
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onPause();
    }

    /* Stop threads */
    public void onStop() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onStop();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onStop();
        if (serverUpdater != null && serverUpdater.isAlive())
            serverUpdater.onStop();
    }
}
