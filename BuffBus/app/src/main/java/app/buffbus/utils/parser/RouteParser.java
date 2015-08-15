package app.buffbus.utils.parser;

import app.buffbus.utils.parser.objects.ParsedObject;
import app.buffbus.utils.parser.objects.Route;

/**
 * Created by Falcon on 8/14/2015.
 */
public class RouteParser implements Parser{

    String data;

    public RouteParser(String data) {
        this.data = data;
    }

    public ParsedObject parse() {
        return new Route();
    }


}
