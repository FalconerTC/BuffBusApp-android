package com.cherish.busstracker.main;

import android.app.Activity;

import com.cherish.busstracker.lib.threads.ModelThread;
import com.cherish.busstracker.lib.threads.UIThread;
import com.cherish.busstracker.main.view.UIController;

/**
 * Created by Falcon on 9/10/2015.
 */
public class ThreadManager {

    private UIThread UIUpdater;
    private ModelThread modelUpdater;
    private Activity original;

    public ThreadManager(Activity original) {
        this.original = original;
    }

    // Initialize UI thread
    public void createUIThread(UIController display) {
        UIUpdater = new UIThread(display, original);
        UIUpdater.start();
    }

    // Initialize Model thread
    public void createModelThread(DataModel model) {
        modelUpdater = new ModelThread(model);
        modelUpdater.start();
    }

    /* Resume thread execution */
    public void onResume() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onResume();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onResume();
    }

    /* Pause thread execution */
    public void onPause() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onPause();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onPause();
    }

    /* Stop threads */
    public void onStop() {
        if (UIUpdater != null && UIUpdater.isAlive())
            UIUpdater.onStop();
        if (modelUpdater != null && modelUpdater.isAlive())
            modelUpdater.onStop();
    }
}
