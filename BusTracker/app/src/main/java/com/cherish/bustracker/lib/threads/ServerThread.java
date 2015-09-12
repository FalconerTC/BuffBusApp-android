package com.cherish.bustracker.lib.threads;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.ServerConnector;

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
