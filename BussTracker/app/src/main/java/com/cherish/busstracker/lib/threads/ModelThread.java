package com.cherish.busstracker.lib.threads;

import com.cherish.busstracker.main.DataController;

/**
 * Created by Falcon on 8/22/2015.
 */
//TODO reimplement back button to pause threads
public class ModelThread extends GenericThread{
    private DataController controller;

    public ModelThread(DataController controller) {
        super();
        this.controller = controller;
    }

    public void onRun() {
        controller.updateStopData();
    }

}