package app.buffbus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import app.buffbus.R;
import app.buffbus.main.DataController;
import app.buffbus.main.ServerConnector;
import app.buffbus.parser.objects.Route;

public class MainActivity extends AppCompatActivity {

    public DataController controller;
    public ServerConnector listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listener = ServerConnector.getServerConnector();
        controller = DataController.getDataController(MainActivity.this);
        blockUntilReady();

        Route[] routes = listener.getRoutes();
        // Change route order
        routes = modifyRoutes(routes);
        //listener.stop();

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
        // Hide template button
        findViewById(R.id.route_template).setVisibility(View.GONE);
        int parentElem = R.id.textView_Subtitle;
        // Create route buttons
        for (int i = 0; i < routes.length; i++) {
            Button btn = createRouteButton(parentElem, routes[i].name, (i+1));
            layout.addView(btn);
            parentElem = (i+1);
        }
        setContentView(layout);
    }

    /* Block main thread until route information is retrieved */
    // TODO fix blocking to not show a black screen, but to just not spawn buttons yet
    private void blockUntilReady() {
        Log.i("Blocking main thread", "Waiting for route information...");
        Object syncObject = new Object();
        listener.setSyncObject(syncObject);
        // Start polling the server for updates
        listener.start();
        synchronized(syncObject) {
            try {
                // Wait for notify from ServerConnection
                syncObject.wait();
                Log.i("Unblocking main thread", "Route info received");
            } catch (InterruptedException e) {
                Log.i("Thread interrupted", "Unblocking main thread");
                return;
            } catch (Exception e) {
                Log.e("Sync error", "Something unexpected happened");
                e.printStackTrace();
            }
        }
    }

    /* This applies several hard-coded changes to what routes we show and in what order
     * This function serves to mimic the route list as it is defined in the iOS version */
    public Route[] modifyRoutes(Route[] routes) {
        int len = routes.length;
        ArrayList<String> excludedRoutes = new ArrayList<>();
        excludedRoutes.add("Will Vill Football");
        excludedRoutes.add("Will Vill Basketball");

        Route[] newRoutes = new Route[len - excludedRoutes.size()];
        // New routes index
        int j = 0;
        for (int i = 0; i < len; i++) {
            // Check exclusion list
            if(!(excludedRoutes.contains(routes[i].name))) {
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
        return newRoutes;
    }

    /* Efficient swap helper
    * http://stackoverflow.com/questions/13766209/effective-swapping-of-elements-of-an-array-in-java
    * */
    public static final <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
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
        btn.setOnClickListener(routeClick(btn));

        return btn;
    }
    /* Called on click for each button in route selector */
    View.OnClickListener routeClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String selectedRoute = ((Button)v).getText().toString();
                // TODO reimplement this in MainActivity,
                // change DataController to not be a singleton
                controller.loadMap(selectedRoute);
            }
        };
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
}
