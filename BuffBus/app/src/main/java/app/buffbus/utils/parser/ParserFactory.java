package app.buffbus.utils.parser;

import app.buffbus.utils.parser.objects.ParsedObject;
import app.buffbus.utils.parser.objects.StopParser;

/**
 * Created by Falcon on 8/14/2015.
 */
public class ParserFactory {

    public static final String PARSER_ROUTES = "routes";
    public static final String PARSER_STOPS = "stops";
    public static final String PARSER_BUSES = "buses";

    public ParsedObject[] parse(String type, String data) {
        if (type.equals(PARSER_ROUTES)) {
            return new RouteParser(data).parse();
        } else if (type.equals(PARSER_STOPS)) {
            return new StopParser(data).parse();
        } else if (type.equals(PARSER_BUSES)) {
            return new BusParser(data).parse();
        }
        return null;
    }
}
