package app.buffbus.utils.parser;

import app.buffbus.utils.parser.objects.ParsedObject;

/**
 * Created by Falcon on 8/14/2015.
 */
public class ParserFactory {

    public static final String PARSER_ROUTES = "routes";

    public ParsedObject parse(String type, String data) {
        if (type.equals(PARSER_ROUTES)) {
            return new RouteParser(data).parse();
        }
        return null;
    }
}
