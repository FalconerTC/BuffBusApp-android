package com.cherish.bustracker.parser;

import com.cherish.bustracker.parser.objects.ParsedObject;

/**
 * Created by Falcon on 8/14/2015.
 */
public interface Parser {

    public ParsedObject[] parse();
}