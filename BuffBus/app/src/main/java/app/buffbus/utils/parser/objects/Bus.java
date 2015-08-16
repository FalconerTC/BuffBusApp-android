package app.buffbus.utils.parser.objects;

/**
 * Created by Falcon on 8/13/2015.
 */
public class Bus implements ParsedObject{

    public Boolean inService;
    public String equipment;
    public double latitude;
    public double longitude;
    // Only used if bus is in service
    public int id;
    public int nextStopId;

    public Bus() {
        this.inService = false;
        this.equipment = "";
        this.latitude = 0;
        this.longitude = 0;
        this.id = 0;
        this.nextStopId = 0;
    }
}
