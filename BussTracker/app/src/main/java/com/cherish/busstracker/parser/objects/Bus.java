package com.cherish.busstracker.parser.objects;

/**
 * Created by Falcon on 8/13/2015.
 */
public class Bus implements ParsedObject{

    public int id;
    public double latitude;
    public double longitude;

    public Bus() {
        this.id = 0;
        this.latitude = 0;
        this.longitude = 0;
    }
}
