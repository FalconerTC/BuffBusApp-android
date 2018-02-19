package com.cherish.bustracker.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cherish.bustracker.lib.Log;
import com.cherish.bustracker.utilities.parser.ParserFactory;
import com.cherish.bustracker.utilities.parser.objects.Bus;
import com.cherish.bustracker.utilities.parser.objects.ParsedObject;
import com.cherish.bustracker.utilities.parser.objects.Route;
import com.cherish.bustracker.utilities.parser.objects.Stop;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Falcon on 8/13/2015.
 * <p/>
 * References http://stackoverflow.com/questions/9605913
 */
public class ServerController {
    public static final String TAG = "ServerConnector";

    //TODO add DNS redundancy
    // Server constants
    public static final String SERVER_ADDR = "http://104.131.176.10:8080/";
    //public static final String SERVER_ADDR = "https://zmnjlpfugk.localtunnel.me/";
    public static final String ROUTES_ADDR = SERVER_ADDR + ParserFactory.PARSER_ROUTES;
    public static final String STOPS_ADDR = SERVER_ADDR + ParserFactory.PARSER_STOPS;
    public static final String BUSES_ADDR = SERVER_ADDR + ParserFactory.PARSER_BUSES;
    public static final int CONNECTION_TIMEOUT = 3000;

    private static ServerController controller;
    private ConnectivityManager networkManager;
    private DefaultHttpClient client;
    private Map<String, HttpPost> httpPosts;
    private ParserFactory parser;

    // Parsed objects
    private Route[] routes;
    private Stop[] stops;
    private Bus[] buses;

    public Route[] getRoutes() {
        return routes;
    }
    public Stop[] getStops() {
        return stops;
    }
    public Bus[] getBuses() {
        return buses;
    }

    private ServerController(Context activity) {
        networkManager = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        parser = new ParserFactory();
        httpPosts = new HashMap<>();

        // Post to /routes
        HttpPost routesPost = new HttpPost(ROUTES_ADDR);
        routesPost.setHeader("Content-type", "application/json");
        httpPosts.put(ParserFactory.PARSER_ROUTES, routesPost);

        // Post to /stops
        HttpPost stopsPost = new HttpPost(STOPS_ADDR);
        stopsPost.setHeader("Content-type", "application/json");
        httpPosts.put(ParserFactory.PARSER_STOPS, stopsPost);

        // Post to /buses
        HttpPost busesPost = new HttpPost(BUSES_ADDR);
        busesPost.setHeader("content-type", "application/json");
        httpPosts.put(ParserFactory.PARSER_BUSES, busesPost);

        // Initialize HTTP client
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
        client = new DefaultHttpClient(httpParams);
    }

    /* Initialize and fetch ServerConnector singleton */
    public static ServerController getServerController(Context activity) {
        if (controller == null)
            controller = new ServerController(activity);
        return controller;
    }

    /* Check if any bus is serving a given route */
    public boolean isRouteActive(String route) {
        if (buses != null) {
            int len = buses.length;
            int routeId = getRouteId(route);

            for (int i = 0; i < len; i++)
                if (buses[i].id == routeId)
                    return true;
        }
        return false;
    }

    /* Gets the route ID for a given route name */
    public int getRouteId(String route) {
        if (routes != null) {
            int len = routes.length;
            for(int i = 0; i < len; i++)
                if (routes[i].name.equals(route)) {
                    return routes[i].id;
                }
        }
        return 0;
    }

    /* Checks if the device has an internet connection */
    public boolean testConnection() {
        NetworkInfo networkActivity = networkManager.getActiveNetworkInfo();
        return (networkActivity != null && networkActivity.isConnected());
    }

    /* Create server requests and set resulting data */
    public boolean update() {
        // Check network status
        if (!testConnection()) {
            //TODO communicate this to the user
            Log.i(TAG, "Connection is invalid");
            return false;
        }

        // Fetch routes only once
        if (routes == null) {
            routes = (Route[]) sendRequest(ParserFactory.PARSER_ROUTES);
        }
        // Update stops and buses each interval
        stops = (Stop[]) sendRequest(ParserFactory.PARSER_STOPS);
        buses = (Bus[]) sendRequest(ParserFactory.PARSER_BUSES);
        return (stops == null || buses == null || routes == null) ? false : true;
    }

    /* Request an update from the server and parse it to a usable object */
    public ParsedObject[] sendRequest(String type) {
        // Fetch specific post
        HttpPost post = httpPosts.get(type);
        InputStream stream = null;
        String result = "";
        // Send request
        try {
            HttpResponse response = client.execute(post);
            stream = response.getEntity().getContent();
        } catch (ConnectTimeoutException e) {
            Log.e("Connection error", "Connection timed-out");
            Log.printStackTrace(e);
            reportNetworkError(0);
        } catch (HttpHostConnectException e) {
            Log.e("Connection error", "Unable to connect to server");
            Log.printStackTrace(e);
            reportNetworkError(1);
        } catch (ClientProtocolException e) {
            Log.e("Protocol error", "Error sending request");
            Log.printStackTrace(e);
        } catch (IOException e) {
            Log.e("IO error", "Error reading data");
            Log.printStackTrace(e);
        } catch (Exception e) {
            // TODO add catch for IllegalStateException
            Log.e("Error", "Something broke");
            Log.printStackTrace(e);
        }

        // Build JSON response
        if (stream != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");
                stream.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("JSON error", "Error converting result " + e.toString());
                return null;
            }
            // Parse JSON response
            ParsedObject[] objects = parser.parse(type, result);
            return objects;
        }
        return null;
    }

    /* Show network errors to the user */
    public void reportNetworkError(int error) {
        switch(error) {
            // Connection timed-out
            case 0:
                break;
            // Unable to connect to server
            case 1:
                break;
        }

    }
}
