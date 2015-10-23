package com.cherish.bustracker.util.threads;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;

/**
 * Created by Falcon on 8/22/2015.
 */
public class ModelThread extends GenericThread {
    public static final String TAG = "ModelThread";
    private DataModel model;

    public ModelThread(DataModel model) {
        super();
        this.model = model;
    }

    public void onRun() {
        Log.i(TAG, "Executing...");
        model.update();
    }

}