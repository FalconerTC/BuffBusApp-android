package app.buffbus.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import app.buffbus.R;
import app.buffbus.main.MapController;
import app.buffbus.utils.ServerConnector;
import app.buffbus.utils.parser.objects.Route;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerConnector listener = ServerConnector.getServerConnector();
        blockUntilReady(listener);

       // Route[] routes = listener.getRoutes();
        SparseArray<Route> routes = listener.getRoutes();
        // Change route order
        routes = modifyRoutes(routes);
        //listener.stop();

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
        // Hide template button
        findViewById(R.id.route_template).setVisibility(View.GONE);
        int parentElem = R.id.textView_Subtitle;
        // Create route buttons
        for (int i = 0; i < routes.size(); i++) {
            Button btn = createRouteButton(parentElem, routes.get(routes.keyAt(i)).name, (i+1));
            layout.addView(btn);
            parentElem = (i+1);
        }
        setContentView(layout);
    }

    /* Block main thread until route information is retrieved */
    private void blockUntilReady(ServerConnector listener) {
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
    public SparseArray<Route> modifyRoutes(SparseArray<Route> routes) {
        ArrayList<String> excludedRoutes = new ArrayList<>();
        excludedRoutes.add("Will Vill Football");
        excludedRoutes.add("Will Vill Basketball");

        int len = routes.size();
        for (int i = 0; i < len; i++) {
            int key = routes.keyAt(i);
            System.out.println(i + " " + key);
            System.out.println(routes.get(key).name);
            // Check exclusion list
            if(!(excludedRoutes.contains(routes.get(key).name))) {
                routes.delete(key);
            }
        }
        // Set "Hop Clockwise" to index 1
        swap(routes, 1, 2);
        // Set "Athens Court Shuttle" to index 3
        swap(routes, 2, 3);
        // Set "Late Night Black" to index 5
        swap(routes, 4, 5);
        // Set "Late Night Silver" to index 6
        swap(routes, 5, 6);
        return routes;
    }

    /* Efficient swap helper
    * http://stackoverflow.com/questions/13766209/effective-swapping-of-elements-of-an-array-in-java
    * */
    public static final <T> void swap(SparseArray<T> a, int i, int j) {
        T t = a.get(a.keyAt(i));
        a.put(i, a.get(a.keyAt(j)));
        a.put(j, t);
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
                Log.i("NOW HEAR THIS", "A button was clicked");
                String selectedRoute = ((Button)v).getText().toString();
                MapController.getMapController(MainActivity.this).loadMap(selectedRoute);
            }
        };
    }

    /*public void loadMap(View v) {
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        Log.i("NOW HEAR THIS", "A button was clicked");
    }*/

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
