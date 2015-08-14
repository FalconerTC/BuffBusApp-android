package app.buffbus.utils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

/**
 * Created by Falcon on 8/13/2015.
 *
 * References http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
 */
public class ServerConnector {

    private static ServerConnector connector;
    private DefaultHttpClient client;
    private HttpPost routes;

    public static final String SERVER_ADDR = "http://104.131.176.10:8080/";
    public static final String ROUTES_ADDR = SERVER_ADDR + "routes";

    private ServerConnector() {
        routes = new HttpPost(ROUTES_ADDR);
        routes.setHeader("Content-type", "application/json");
    }

    public static ServerConnector getServerConnector() {
        if (connector == null)
            connector = new ServerConnector();
        return connector;
    }

    public void connect() {
        client = new DefaultHttpClient(new BasicHttpParams());
    }

    public void update() {
        try {

        } catch (Exception e) {

        }
    }

}
