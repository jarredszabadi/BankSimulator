/**
 * Class Customer
 * 
 * represents an arrival event
 */

public class Customer
{
    
    private double arrival=0; //arival time
    private double departure=0; //departure time
    private double waittime=0;  //time waiting in queue

    //constructor, set arrival time
    public Customer(double x)
    {
        arrival = x;
    }
    //processCUstomer
    //initializes departure of customer from the queue
    public void processCustomer(double x)
    {
        departure = x;
    }
    //toString
    public String toString()
    {
        return "arrival: "+arrival+", departure: "+departure+ ", waitTime: "+waittime;
    }
    //returns arrival time
    public double getArrival()
    {
        return arrival;
    }
    //returns departure time
    public double getDeparture()
    {
        return departure;
    }
    
    /**
     * setWaitTime(Custome other)
     * determines the time this customer spent in the queue
     * 
     * Customer other will be the customer ahead of this customer in the queue
     * this customer has to wait in line until other has left the line
     * so the waiting time can be determined 1) if this customer does indeed have to wait 2)if it does
     * then the difference in this customer's arrival and the other customers departure is the waiting time
     * 
     * ex: this.arrival = 10
     * other.departure =9, then this did not have to wait and the difference would be <0
     * 
     * otherwise, other.departure = 11;
     * 11-10=1, this customer had to wait 1 minute
     */
    public void setWaitTime(Customer other)
    {
        
        waittime=other.getDeparture()-getArrival();//determine waiting time
        if (waittime<0) //if this customer did not have to wait for other customer then set waittime to 0
        {
            waittime=0;
        }
    }
    /**
     * this setWaitTime method is used for the customers who remain in the queue when the simulation has ended
     * the end of the simulation-their arrival time = the wait time
     */
    public void setWaitTime(double time)
    {
        waittime = time-getArrival();
    }
    //return waittime;
    public double getWaitTime()
    {
        return waittime;
    }
 
}
