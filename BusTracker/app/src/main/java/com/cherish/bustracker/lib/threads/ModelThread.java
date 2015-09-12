package com.cherish.bustracker.lib.threads;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;

/**
 * Created by Falcon on 8/22/2015.
 */
public class ModelThread extends GenericThread{
    private DataModel controller;
    public static final String TAG = "ModelThread";

    public ModelThread(DataModel controller) {
        super();
        this.controller = controller;
    }

    public void onRun() {
        Log.i(TAG, "Executing...");
        controller.update();
    }

}