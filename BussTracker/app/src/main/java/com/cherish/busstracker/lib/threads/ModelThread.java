package com.cherish.busstracker.lib.threads;

import com.cherish.busstracker.main.DataModel;

/**
 * Created by Falcon on 8/22/2015.
 */
//TODO reimplement back button to pause threads
public class ModelThread extends GenericThread{
    private DataModel controller;

    public ModelThread(DataModel controller) {
        super();
        this.controller = controller;
    }

    public void onRun() {
        controller.update();
    }

}