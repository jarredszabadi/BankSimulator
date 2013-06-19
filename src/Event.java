/**
 * Class event
 * -Arrival
 * -Departure
 * 
 * This class is used to implement the simulation events of customers arriving and leaving the bank
 */

public class Event
{
    private double occurenceTime; //time that event occured
    private int type;    //type of event- arrival/departure
    //for arrival event only
    private double serviceTime; //how long the customer will be at the teller
    
    private static int ARRIVAL = 1;
    private static int DEPARTURE=0;
    

    
    //if type ==1 then arrival
    //if type==0 then departure
    //constructor for Departure Event
    public Event(double time, int type)
    {
        this.occurenceTime=time;
        this.type = type;
        serviceTime=0;
    }
    //constructor for arrival event
    public Event(double time, int type, double serviceTime)
    {
        this.occurenceTime=time;
        this.type = type;
        this.serviceTime=serviceTime;
    }
    //returns type of event
    public int getType()
    {
        return type;
    }
    //returns time that event was instantaniated
    public double getTime()
    {
        return occurenceTime;
    }
    //determines which event occured before the other one
    public int compareTo(Event e)
    {
        if(e.getTime()>= occurenceTime){return 1;} //if event e occurs after this then return 1
        else{return 0;}
        
    }
    //set occurence time
    public void setTime(double time)
    {
        occurenceTime=time;
    }
    //return servicetime
    public double getServiceTime()
    {
        return serviceTime;
    }
    //toString
    public String toString()
    {
        return("Time: "+occurenceTime+", Duration:"+serviceTime+", Type "+type);
    }

}
