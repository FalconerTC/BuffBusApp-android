package app.buffbus.utils.parser;

import android.util.SparseArray;

import app.buffbus.utils.parser.objects.ParsedObject;

/**
 * Created by Falcon on 8/14/2015.
 */
public interface Parser {

    public SparseArray<? extends ParsedObject> parse();
}
