package com.cherish.busstracker.lib.threads;

import android.app.Activity;
import com.cherish.busstracker.main.UIController;

/**
 * Created by Falcon on 9/5/2015.
 */
public class UIThread extends GenericThread {
    private Activity original;
    private UIController controller;

    public UIThread(UIController controller, Activity original) {
        super();
        this.controller = controller;
        this.original = original;
    }

    /* Run UI updates on main thread */
    public void onRun() {
        original.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.updateTimes();
            }
        });
    }
}
