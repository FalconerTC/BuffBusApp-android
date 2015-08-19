package app.buffbus.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import app.buffbus.R;
import app.buffbus.main.MapController;
import app.buffbus.utils.parser.objects.Bus;
import app.buffbus.utils.parser.objects.Stop;

public class DisplayActivity extends ActionBarActivity {

    NumberPicker stopSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

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
        String[] stops = MapController.getMapController().getStopNames();
        stopSelector.setMaxValue(stops.length - 1);
        stopSelector.setDisplayedValues(stops);
        stopSelector.setWrapSelectorWheel(true);
        stopSelector.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    /* Set values for selector */
    public void updateSelector() {

        /*Spinner spinner = (Spinner)findViewById(R.id.buss_spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });*/

    }
}
