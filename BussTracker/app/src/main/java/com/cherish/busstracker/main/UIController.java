package com.cherish.busstracker.main;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.cherish.busstracker.R;

// TODO change DisplayActivity to a static Singleton, move out UIThread (?)
public class UIController implements OnValueChangeListener {

    private DataController controller;
    private UIThread updater;
    private MapController map;
    private Activity original;
    public NumberPicker stopSelector;

    private String[] stops;
    public String selectedStop;

    public UIController(Activity activity, DataController model, MapController map) {
        Log.i("DisplayActivity", "Creating DisplayActivity");

        this.original = activity;
        this.controller = model;
        this.map = map;

        stopSelector = (NumberPicker)original.findViewById(R.id.stopPicker);

        updater = new UIThread();
        updater.start();

        initializeSelector();
    }

    /* Returns the closest stop as the starting value */
    private int getStartingValue() {
        // Default to 0 until "nearest stop" logic is in place
        return 0;
    }

    /* Set initial values for selector */
    public void initializeSelector() {
        stops = controller.getStopNames();
        selectedStop = stops[getStartingValue()];
        setTimesDisplay(true);
        stopSelector.setMaxValue(stops.length - 1);
        stopSelector.setDisplayedValues(stops);
        stopSelector.setValue(getStartingValue());
        stopSelector.setWrapSelectorWheel(true);
        stopSelector.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // Create event listener
        stopSelector.setOnValueChangedListener(this);
        updateTimes();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        changeSelectedStop(newVal);
    }

    /* Change current stop and update UI */
    public void changeSelectedStop(int newVal) {
        selectedStop = stops[newVal];
        updateTimes();
    }

    /* Updates the current time display */
    public void updateTimes() {
        // Do nothing if the route is already known to be inactive
        if (controller.getRouteActive() == Boolean.FALSE) {
            return;
        }

        int[] times = controller.getNextTimes(selectedStop);
        if (times != null) {
            String value1 = times[0] == 0 ? "Now" : (times[0] + " minute" + (times[0] == 1 ? "" : "s"));
            ((TextView)original.findViewById(R.id.time_1)).setText(value1);
            // Some buses have two next times
            // TODO fix support for showing only one time
            if (times.length > 1) {
                String value2 = times[1] == 0 ? "Now" : (times[1] + " minute" + (times[1] == 1 ? "" : "s"));
                ((TextView)original.findViewById(R.id.time_2)).setText(value2);
            }
        } else {
            // TODO make sure "no buses" event can't false trigger and lock out times
            controller.setRouteActive(Boolean.FALSE);
            setTimesDisplay(false);
        }

        //TODO only re-draw buses if triggered by interval
        map.onUpdate(selectedStop);

    }

    /* Change which set of TextViews are visible */
    private void setTimesDisplay(boolean active) {
        original.findViewById(R.id.time_display).setVisibility(active ? View.VISIBLE : View.GONE);
        original.findViewById(R.id.time_1).setVisibility(active ? View.VISIBLE : View.GONE);
        original.findViewById(R.id.time_2).setVisibility(active ? View.VISIBLE : View.GONE);
        original.findViewById(R.id.time_inactive).setVisibility(active ? View.GONE : View.VISIBLE);
    }

    /* Updater thread for the view
     * This is kept here as a sub-class instead of with the other
     * threads as DisplayActivity does not have a static context */
    //TODO create abstract thread class
    class UIThread extends Thread implements Runnable {
        private Object lock = new Object();
        private volatile boolean paused = false;
        private volatile boolean active = true;

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
                            Log.i("Thread interrupted", "Exiting display thread");
                            return;
                        }
                    }
                }
                try {
                    Log.i("UIThread", "Updating times indicator");
                    // UI changes must be on the main thread
                    original.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTimes();
                        }
                    });
                } catch (Exception e) {
                    Log.e("Thread error", "Display thread failed to update");
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
}
