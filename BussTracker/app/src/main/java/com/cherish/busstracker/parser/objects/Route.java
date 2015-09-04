package com.cherish.busstracker.parser.objects;

/**
 * Created by Falcon on 8/13/2015.
 */
public class Route implements ParsedObject{

    public int id;
    public String name;
    public int[] stops;

    public Route() {
        this.id = 0;
        this.name = "";
    }

}
