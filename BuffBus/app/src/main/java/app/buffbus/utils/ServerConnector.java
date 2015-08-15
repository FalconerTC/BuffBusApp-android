package app.buffbus.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import app.buffbus.utils.parser.ParserFactory;
import app.buffbus.utils.parser.objects.ParsedObject;
import app.buffbus.utils.parser.objects.Route;
import app.buffbus.utils.parser.objects.Stop;

/**
 * Created by Falcon on 8/13/2015.
 *
 * References http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
 *
 */
public class ServerConnector{

    private static ServerConnector connector;
    private DefaultHttpClient client;
    private Map<String, HttpPost> httpPosts;
    private ParserFactory parser;
    private Thread requester;

    // Parsed objects
    public Route[] routes;
    public Stop[] stops;

    public static final String SERVER_ADDR = "http://104.131.176.10:8080/";
    public static final String ROUTES_ADDR = SERVER_ADDR + ParserFactory.PARSER_ROUTES;
    public static final String STOPS_ADDR = SERVER_ADDR + ParserFactory.PARSER_STOPS;

    public static final long POLLING_INTERVAL = 5 * 1000; //5 seconds

    private ServerConnector() {
        httpPosts = new HashMap<String, HttpPost>();
        // Post to /routes
        HttpPost routesPost = new HttpPost(ROUTES_ADDR);
        routesPost.setHeader("Content-type", "application/json");
        httpPosts.put(ParserFactory.PARSER_ROUTES, routesPost);

        // Post to /stops
        HttpPost stopsPost = new HttpPost(STOPS_ADDR);
        stopsPost.setHeader("Content-type", "application/json");
        httpPosts.put(ParserFactory.PARSER_STOPS, stopsPost);

        client = new DefaultHttpClient(new BasicHttpParams());

        parser = new ParserFactory();

        Log.i("ParserFactory", "Parser factory initialized");
    }

    /* Initialize and fetch ServerConnector singleton */
    public static ServerConnector getServerConnector() {
        if (connector == null)
            connector = new ServerConnector();
        return connector;
    }

    /* Create the server polling thread */
    public void start() {
        requester = new Thread(new Runnable() {
            @Override
            public void run() {
                // Continue polling until stopped
                while(true) {
                    try {
                        update();
                    } catch (Exception e) {
                        Log.e("Thread error", "Thread failed to update");
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(POLLING_INTERVAL);
                    // Kill thread if interrupted
                    } catch(InterruptedException e) {
                        Log.i("Thread interrupted", "Exiting thread");
                        return;
                    } catch (Exception e) {
                        Log.e("Thread error", "Thread failed to sleep");
                        e.printStackTrace();
                    }
                }
            }
        });
        requester.start();
    }

    /* Stop server polling */
    public void stop() {
        if(requester != null)
            requester.interrupt();
    }

    /* Create server requests and set resulting data */
    public void update() {
        // Fetch routes only once
        if (this.routes == null) {
            this.routes = (Route[]) sendRequest(ParserFactory.PARSER_ROUTES);
        }
        //this.stops = (Stop[])sendRequest(ParserFactory.PARSER_STOPS);
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
        } catch (UnsupportedEncodingException e) {
            Log.e("Encoding error", "Encoding is unsupported");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("Protocol error", "Error sending request");
            e.printStackTrace();
        } catch(IOException e) {
            Log.e("IO error", "Error reading data");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Error", "Something broke");
            e.printStackTrace();
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
            } catch(Exception e) {
                Log.e("JSON error", "Error converting result " + e.toString());
            }
        }
        // Parse JSON response
        ParsedObject[] objects = parser.parse(type, result);
        return objects;
    }

}
