package com.cherish.bustracker.util.threads;

import com.cherish.bustracker.lib.Log;

/**
 * Created by Falcon on 8/29/2015.
 */
public abstract class GenericThread extends Thread implements Runnable {
    public static final String TAG = "GenericThread";

    // Standard polling intervals
    public static final long DEFAULT_POLLING_INTERVAL = 10 * 1000; //10 seconds
    public static final long RAPID_POLLING_INTERVAL = 2 * 1000; //2 seconds

    // Time for threads to sleep
    public long currentPollingInterval;
    // Generic object used to synchronize pausing
    private Object lock;
    // Records whether thread is paused
    private volatile boolean paused;
    // Records whether thread is running
    private volatile boolean active;

    public GenericThread() {
        this.lock = new Object();
        this.paused = false;
        this.active = true;
        currentPollingInterval = DEFAULT_POLLING_INTERVAL;
    }

    public boolean isPaused() {
        return paused;
    }

    // Implemented method for actions performed every interval
    public abstract void onRun();

    @Override
    public void run() {
        // Continue polling until stopped
        while (active) {
            // Check if thread is paused
            synchronized (lock) {
                while (paused) {
                    try {
                        // Wait until signalled
                        lock.wait();
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Exiting thread");
                        return;
                    }
                }
            }
            try {
                // General thread logic
                onRun();
            } catch (Exception e) {
                Log.e(TAG, "Thread failed to update");
                e.printStackTrace();
            }
            try {
                // Sleep for the polling interval
                Thread.sleep(currentPollingInterval);
                // Kill thread if interrupted
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread interrupted. Exiting...");
                return;
            } catch (Exception e) {
                Log.e(TAG, "Thread failed to sleep");
                e.printStackTrace();
            }
        }
    }

    // Pause thread execution
    public void onPause() {
        synchronized (lock) {
            paused = true;
        }
    }

    // Resume thread execution
    public void onResume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

    // Kill the thread
    public void onStop() {
        active = false;
    }

}
