package com.cherish.bustracker.lib.threads;

import android.app.Activity;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.view.UIController;

/**
 * Created by Falcon on 9/5/2015.
 */
public class UIThread extends GenericThread {
    public static final String TAG = "UIThread";
    private Activity original;
    private UIController controller;

    public UIThread(UIController controller, Activity original) {
        super();
        this.controller = controller;
        this.original = original;
    }

    /* Run UI updates on main thread */
    public void onRun() {
        Log.i(TAG, "Executing...");
        original.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.update(true);
            }
        });
    }
}
