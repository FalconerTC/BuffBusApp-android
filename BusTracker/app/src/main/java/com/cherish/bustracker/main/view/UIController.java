package com.cherish.bustracker.main.view;

import android.app.Activity;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cherish.bustracker.R;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.main.DataModel;

public class UIController implements OnValueChangeListener {
    public static final String TAG = "UIController";

    private DataModel controller;
    private MapController map;
    private Activity original;
    private NumberPicker stopSelector;

    private String[] stops;
    private String selectedStop;
    private int activeRoutes;

    public UIController(Activity activity, DataModel model, MapController map) {
        Log.i(TAG, "Creating DisplayActivity");

        this.original = activity;
        this.controller = model;
        this.map = map;
        this.activeRoutes = -1;

        stopSelector = (NumberPicker)original.findViewById(R.id.stopPicker);

        initializeSelector();
    }

    /* Returns the closest stop as the starting value */
    private int getStartingValue() {
        // Default to 0 until "nearest stop" logic is in place
        Log.i(TAG, "Setting closest value");
        return 0;
    }

    /* Set initial values for selector */
    public void initializeSelector() {
        System.out.println("Initializing selector");
        stops = controller.getStopNames();
        int startingStop = getStartingValue();
        selectedStop = stops[startingStop];
        stopSelector.setMaxValue(stops.length - 1);
        stopSelector.setDisplayedValues(stops);
        stopSelector.setValue(startingStop);
        stopSelector.setWrapSelectorWheel(true);
        stopSelector.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // Create event listener
        stopSelector.setOnValueChangedListener(this);
        //update();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        changeSelectedStop(newVal);
    }

    /* Fetch stop ID from stop name and call overloaded method */
    public void changeSelectedStop(String stop) {
        if (stopSelector != null) {
            String[] stops = stopSelector.getDisplayedValues();
            for (int i = 0; i < stops.length; i++) {
                if (stop.equals(stops[i])) {
                    changeSelectedStop(i);
                }
            }
        }
    }

    /* Change current stop and update UI */
    public void changeSelectedStop(int newVal) {
        stopSelector.setValue(newVal);
        selectedStop = stops[newVal];
        update(false);
    }

    /* Update map and time display. Called by UIThread per interval */
    public void update(boolean calledByInterval) {
        if (selectedStop != null) {
            map.onUpdate(selectedStop, calledByInterval);
            updateTimeDisplay();
        }
    }

    /* Updates the current time display */
    public void updateTimeDisplay() {
        int newRoutes = 0;

        int[] times = controller.getNextTimes(selectedStop);
        if (times != null) {
            String value1 = (times[0] == 0 ? "Now" : (times[0] + " minute" + (times[0] == 1 ? "" : "s")));
            ((TextView)original.findViewById(R.id.time_1)).setText(value1);
            newRoutes = 1;
            // Some buses have two next times
            if (times.length > 1) {
                String value2 = (times[1] == 0 ? "Now" : (times[1] + " minute" + (times[1] == 1 ? "" : "s")));
                ((TextView)original.findViewById(R.id.time_2)).setText(value2);
                newRoutes = 2;
            }
        }
        // Cache the current number of routes
        if (this.activeRoutes != newRoutes) {
            this.activeRoutes = newRoutes;
            setTimesDisplay(newRoutes);
        }
    }

    /* Change which set of TextViews are visible along with some layout params
    *  Takes the number of active times [0-2] to show
    */
    private void setTimesDisplay(int active) {
        // Toggle view types
        if (active == 0) {
            original.findViewById(R.id.time_display).setVisibility(View.GONE);
            original.findViewById(R.id.time_1).setVisibility(View.GONE);
            original.findViewById(R.id.time_2).setVisibility(View.GONE);
            original.findViewById(R.id.time_inactive).setVisibility(View.VISIBLE);
        } else {
            original.findViewById(R.id.time_display).setVisibility(View.VISIBLE);
            original.findViewById(R.id.time_1).setVisibility(View.VISIBLE);
            original.findViewById(R.id.time_inactive).setVisibility(View.GONE);

            View timeDisplay = original.findViewById(R.id.time_1);
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams)timeDisplay.getLayoutParams();
            // Show only one bus time indicator and center is
            if (active == 1) {
                original.findViewById(R.id.time_2).setVisibility(View.GONE);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            } else {
                original.findViewById(R.id.time_2).setVisibility(View.VISIBLE);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            timeDisplay.setLayoutParams(params);
        }
    }
}
