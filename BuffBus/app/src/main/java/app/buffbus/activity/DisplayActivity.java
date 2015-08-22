package app.buffbus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import app.buffbus.R;
import app.buffbus.main.MapController;
import app.buffbus.utils.parser.objects.Stop;


public class DisplayActivity extends AppCompatActivity {

    private MapController controller;
    private NumberPicker stopSelector;
    private String[] stops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        controller = MapController.getMapController();

        stopSelector = (NumberPicker)findViewById(R.id.stopPicker);

        System.out.println("Creating DisplayActivity");
        initializeSelector();
        //updateSelector();
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
        setTimesDisplay(true);
        stopSelector.setMaxValue(stops.length - 1);
        stopSelector.setDisplayedValues(stops);
        stopSelector.setValue(getStartingValue());
        stopSelector.setWrapSelectorWheel(true);
        stopSelector.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // Create event listener
        final Context parent = this;
        stopSelector.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String selectedStop = stops[newVal];
                //Toast.makeText(parent, "New value: " + val1, Toast.LENGTH_SHORT).show();
                updateTimes(selectedStop);
                //((TextView)findViewById(R.id.time_1)).setText(val1);
            }
        });
        updateTimes(stops[getStartingValue()]);
    }

    /* Returns the closest stop as the starting value */
    private int getStartingValue() {
        return 0;
    }

    /* Updates the current time display */
    public void updateTimes(String selectedStop) {
        // Do nothing if the route is already known to be inactive
        if (controller.getRouteActive() == Boolean.FALSE) {
            return;
        }

        int[] times = controller.getNextTimes(selectedStop);
        if (times != null) {
            String value1 = times[0] == 0 ? "Now" : (times[0] + " minute" + (times[0] == 1 ? "" : "s"));
            ((TextView)findViewById(R.id.time_1)).setText(value1);
            // Some buses have two next times
            if (times.length > 1) {
                String value2 = times[0] == 0 ? "Now" : (times[1] + " minute" + (times[1] == 1 ? "" : "s"));
                ((TextView) findViewById(R.id.time_2)).setText(value2);
            }
        } else {
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
}
