package com.cherish.busstracker.lib.threads;

import com.cherish.busstracker.lib.Log;
import com.cherish.busstracker.main.ServerConnector;

/**
 * Created by Falcon on 9/5/2015.
 */
public class ServerThread extends GenericThread{
    private Object syncObject;
    private ServerConnector connector;

    public void setSyncObject(Object sync) { this.syncObject = sync; }

    public ServerThread(ServerConnector connector) {
        super();
        this.connector = connector;
    }

    public void onRun() {
        Log.i("ServerThread", "Executing...");
        connector.update();
    }

    /* Notify waiting object in MainActivity */
    public void onNotify() {
        if (syncObject != null) {
            synchronized(syncObject) {
                syncObject.notify();
            }
        }
    }
}
