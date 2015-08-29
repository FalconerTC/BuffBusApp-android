package app.buffbus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import app.buffbus.R;
import app.buffbus.main.DataController;
import app.buffbus.main.MapController;
import app.buffbus.main.ServerConnector;

// TODO change DisplayActivity to a static Singleton, move out UIThread (?)
public class DisplayActivity extends AppCompatActivity{

    private DataController controller;
    private NumberPicker stopSelector;
    private UIThread updater;
    private MapController map;

    private String[] stops;
    private String selectedStop;

    /* Returns the closest stop as the starting value */
    private int getStartingValue() {
        // Default to 0 until map logic is in place
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("CREATING DISPLAY_ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        controller = DataController.getDataController();
        stopSelector = (NumberPicker)findViewById(R.id.stopPicker);
        map = MapController.getMapController();


        updater = new UIThread();
        updater.start();


        Log.i("DisplayActivity", "Creating DisplayActivity");
        initializeSelector();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(map);

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Display activity was stopped");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Display activity was started");
        //map.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        stopSelector.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedStop = stops[newVal];
                updateTimes();
            }
        });
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
            ((TextView)findViewById(R.id.time_1)).setText(value1);
            // Some buses have two next times
            // TODO fix support for showing only one time
            if (times.length > 1) {
                String value2 = times[1] == 0 ? "Now" : (times[1] + " minute" + (times[1] == 1 ? "" : "s"));
                ((TextView) findViewById(R.id.time_2)).setText(value2);
            }
        } else {
            // TODO make sure "no buses" event can't false trigger and lock out times
            controller.setRouteActive(Boolean.FALSE);
            setTimesDisplay(false);
        }

    }

    /* Change which set of TextViews are visible */
    private void setTimesDisplay(boolean active) {
        findViewById(R.id.time_display).setVisibility(active ? View.VISIBLE : View.GONE);
        findViewById(R.id.time_1).setVisibility(active ? View.VISIBLE : View.GONE);
        findViewById(R.id.time_2).setVisibility(active ? View.VISIBLE : View.GONE);
        findViewById(R.id.time_inactive).setVisibility(active ? View.GONE : View.VISIBLE);
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
                    runOnUiThread(new Runnable() {
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
