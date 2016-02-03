package com.cherish.bustracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cherish.bustracker.R;
import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.utilities.updater.ServerThread;
import com.cherish.bustracker.main.ServerController;
import com.cherish.bustracker.utilities.parser.objects.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = "MainActivity";

    public static final int MAX_ROUTES = 8;
    /* Tag for Extra when starting DisplayActivity*/
    public final static String SELECTED_ROUTE = "com.cherish.bustracker.selected_route";

    public final static String FEEDBACK_EMAIL = "cherishdevapps@gmail.com";
    public final static String FEEDBACK_SUBJECT = "Android CU Bus Tracker Feedback!";
    public Intent display;
    private ServerThread updater;
    private Button[] buttons;

    /* Efficient swap helper
    * http://stackoverflow.com/questions/13766209/effective-swapping-of-elements-of-an-array-in-java
    * */
    public static final <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Creating MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup feedback button
        TextView feedback = (TextView) findViewById(R.id.feedback);
        feedback.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("plain/text");
                i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL});
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, FEEDBACK_SUBJECT);
                i.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "Send mail"));
            }
        });

    }

    /* Called by the ServerThread once it received route info
     * This method then creates the buttons based on route info
     *  then destroys the server thread
     * */
    public void onNotify(ServerController connector) {
        Log.i(TAG, "Received route info");
        Route[] routes = connector.getRoutes();
        // Create buttons
        if (routes != null) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);
            // Change route order
            routes = modifyRoutes(connector);
            // Create route buttons
            int len = (routes.length <= MAX_ROUTES ? routes.length : MAX_ROUTES);
            int parentElem = R.id.textView_Subtitle;
            this.buttons = new Button[len];
            for (int i = 0; i < len; i++) {
                buttons[i] = createRouteButton(parentElem, routes[i].name, (i + 1));
                layout.addView(buttons[i]);
                parentElem = (i + 1);
            }
            setContentView(layout);

            // Remove unnecessary objects
            if (updater != null)
                updater.onStop();
            updater = null;
        }
    }

    /* This applies several hard-coded changes to what routes we show and in what order
     * This function serves to mimic the route list as it is defined in the iOS version
     * TODO rewrite this to be more reliable
     */
    public Route[] modifyRoutes(ServerController connector) {
        Route[] routes = connector.getRoutes();
        int len = routes.length;
        ArrayList<String> excludedRoutes = new ArrayList<>();
        excludedRoutes.add("Football Extension");

        Route[] newRoutes = new Route[len - excludedRoutes.size()];
        // New routes index
        int j = 0;
        for (int i = 0; i < len; i++) {
            // Check exclusion list
            if (!(excludedRoutes.contains(routes[i].name))) {
                newRoutes[j] = routes[i];
                j++;
            }
        }

        // Set "Hop Clockwise" to index 1
        swap(newRoutes, 1, 2);
        // Set "Athens Court Shuttle" to index 3
        swap(newRoutes, 2, 3);
        // Set "Late Night Black" to index 5
        swap(newRoutes, 4, 5);
        // Set "Late Night Silver" to index 6
        swap(newRoutes, 5, 6);
        // Set "Discovery Express Loop" to index 7
        swap(newRoutes, 9, 7);

        // Replace BuffBus slot if no buses are running
        boolean routeActive = connector.isRouteActive("Buff Bus");
        if (!routeActive) {
            if (connector.isRouteActive("Will Vill Football"))
                swap(newRoutes, 0, 7);
            else
                swap(newRoutes, 0, 8);
        }

        return newRoutes;
    }

    /* Create a button for route-selection, based on the template */
    public Button createRouteButton(int parentElem, String text, int id) {
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.textView_Title);
        relativeParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.textView_Title);
        relativeParams.addRule(RelativeLayout.BELOW, parentElem);
        relativeParams.setMargins(0, 12, 0, 0);

        Button btn = new Button(MainActivity.this, null, R.attr.RouteButtonStyle);

        btn.setText(text);
        btn.setId(id);
        btn.setLayoutParams(relativeParams);
        btn.setOnClickListener(this);
        btn.setVisibility(View.VISIBLE);

        return btn;
    }

    /* Detects if GooglePlayServices is available
     * Reference: http://stackoverflow.com/questions/15401748/how-to-detect-if-google-play-is-installed-not-market
     */
    public static boolean checkPlayServices(Activity activity) {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, activity, result).show();
            } else {
                Toast.makeText(activity.getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                activity.finish();
            }
            return false;
        }
        return true;
    }

    @Override
    /* onClick listener for generated buttons */
    public void onClick(View v) {
        /* PlayServices must be installed to continue */
        if (checkPlayServices(this)) {
            String selectedRoute = ((Button) v).getText().toString();

            if (display == null) {
                display = new Intent(this, DisplayActivity.class);
                display.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            display.putExtra(SELECTED_ROUTE, selectedRoute);
            startActivity(display);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /* Activity actions */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Starting");

        // Run a server request to get route information
        ServerController listener = ServerController.getServerController(this);
        updater = new ServerThread(listener, this);
        updater.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Resuming");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Pausing");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Stopping");
    }
}
