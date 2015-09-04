package com.cherish.busstracker.main.threads;

import android.util.Log;

import com.cherish.busstracker.main.DataController;
import com.cherish.busstracker.main.ServerConnector;

/**
 * Created by Falcon on 8/22/2015.
 */
//TODO reimplement back button to pause threads
public class ControllerThread extends Thread implements Runnable {
    private Object lock;
    private volatile boolean paused;
    private volatile boolean active;

    private DataController controller;

    public ControllerThread(DataController controller) {
        this.lock = new Object();
        this.paused = false;
        this.active = true;
        this.controller = controller;
    }

    @Override
    public void run() {
        // Continue polling until stopped
        while(active) {
            // Check if thread is paused
            synchronized (lock) {
                while(paused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Log.i("Thread interrupted", "Exiting controller thread");
                        return;
                    }
                }
            }
            try {
                Log.i("ControllerThread", "Updating stop data");
                controller.updateStopData();
            } catch (Exception e) {
                Log.e("Thread error", "Controller thread failed to update");
                e.printStackTrace();
            }
            try {
                Thread.sleep(ServerConnector.POLLING_INTERVAL);
                // Kill thread if interrupted
            } catch(InterruptedException e) {
                Log.i("Thread interrupted", "Exiting display thread");
                return;
            } catch (Exception e) {
                Log.e("Thread error", "Display thread failed to sleep");
                e.printStackTrace();
            }
        }
    }
    // Kill the thread
    public void onStop() {
        active = false;
    }
    // Pause execution
    public void onPause() {
        synchronized (lock) {
            paused = true;
        }
    }
    // Resume execution
    public void onResume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

    public boolean isPaused() {
        return paused;
    }
}