package com.cherish.bustracker.parser.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Falcon on 8/13/2015.
 */
public class Stop implements ParsedObject {

    public int id;
    public String name;
    public double latitude;
    public double longitude;
    public Map<Integer, int[]> busTimes;

    public Stop() {
        this.id = 0;
        this.name = "";
        this.latitude = 0;
        this.longitude = 0;
        this.busTimes = new HashMap<Integer, int[]>();
    }

}
