import java.io.*;
public class Simulator
{
    private double aveArrival; //averageArrival time of customers
    private double aveService; //averageService time of customers
    private double timeLimit;   //simulation timelimit
    private int servers;    //# of tellers in the bank
    private boolean showSketch; //showSketch
    
    
    
    private double curTime; //current time of the simulation
    private PrintWriter out;    //Output writer to file "out.txt"

    //The following variables could have been initialized in a method and passed through paramteres, 
    //however, for ease of access and use I used instance variables 
    private int curCustomer=0; //integer representation of the customer at the head of the queue
    private int curNumOfCustomers=0; //# of customers in queue
    private int customersServed=0; //# of customers that have left the queue
    private int customers=0;    //total # of customers through the doors
    private int nowait=0; //# of customers who didn't have to wait in the queue
    
    private static int ARRIVAL =1;  //Event = Arrival
    private static int DEPARTURE = 0; // Event=Departure
    
    private Queue<Event> eventList; //eventlist (only holds at most 1 Arrival and 1 Departure event at a time)
    private Queue<Event> bankQueue; //bankQueue (a list of arrival events to be processed)
    private Queue<Customer> customerQueue;//customerList (a list of all customers that have walked through the door)
                                            //earliest customer=head of the list
    
    private int x=0;                        //# of Arrival events  processed
    private int y=0;                        //#of Departure events processed
    
    
    /**Constructor,
    initializes :the AvergaeArrival time of customers
                 the AverageService time for customers
                 Number of tellers
    */
    public Simulator(double avgInterarrivalTime, int numberOfServers, double avgServiceTime )
    {
        aveArrival = avgInterarrivalTime;
        aveService = avgServiceTime;
        servers = numberOfServers;
        
    }
    /**
     * Generates random numbers with an exponential differentiation from a desired value
     * -used to generate random arrival events and service times
     * *Provided by Thomas Kunz, posted in Assignment4.pdf
     */
    public double exponentialDistribution(double average) 
    {
        double uniformDistribution = Math.random();
        return -average * Math.log(uniformDistribution);
    }
    
    /**
     * Parameters:
     * simTimer - simulation timelimit
     * trace - display or hide simulation events
     * 
     * This function is under the assumption that as long as the simulator is running customer arrival
     * events will occur. In other words, rather than generating an input file of customer arrivals, as long
     * as the simulation is still running a new arrival event will be generated.
     * 
     * The decision behind this was under the belief that the purpose of the simulation is to calculate
     * statistics from occuring events, if there are limited events the simulation would not be doing its job
     * 
     * Algorithm:
     * 1)get first event in the eventList
     * 2)if event=arrivalEvent
     *      -remove from eventlist
     *      -determine if queue is empty
     *      if queue.isempty()
     *          -generate departure event
     *      generate new arrival event
     *          -add to eventList in priority order
     *   else if event=DepartureEvenr
     *      -remoce event from eventList
     *      -determine if queue.isempty()
     *      if !queue.isempty()
     *          -generate a departure event for the new head of line customer
     *          -add it to eventList in priorityorder
     * 3)continue steps 1 and 2 until the timelimit has been reached
     * 4)calculate statistics and output to screen
     * 
     * the bankqueue represents the line of bank. it can have many elements in the queue
     * eventList will only have up to 2 events in it at any given time. This is so because the teller will 
     * ever only be servicing a new customer, or saying goodbye to the old one. If the eventLists's first
     * element is an arrival event then a departure event is generated. 
     * 
     */
    public void run(double simTimer, boolean trace){
        Greet();//output greeting message/instructions to user
        
        //initialize queues
        bankQueue = new Queue();
        eventList = new Queue();
        customerQueue = new Queue();
        //set simTimeLimit
        timeLimit=simTimer;
        //set showSketch to true or false
        showSketch=trace;
        
        //initialize output writer
        try{
            out = new PrintWriter("out.txt");//text file for sketch

        }
        catch(IOException e){}
        
        //create first arrival event and set clock to first arrival time        
        curTime = exponentialDistribution(aveArrival);
        Event firstArrival = new Event(curTime, ARRIVAL,exponentialDistribution(aveService));
        eventList.addPriority(firstArrival);
        
        //while simulation has not surpassed its timelimit...
        while(curTime<timeLimit)
        {
           //get next event from eventList
            Event newEvent = eventList.peek();
            writeToFile("Clock: "+newEvent.getTime());//write time
            
            //if event == Arrival Event
            if(newEvent.getType()== ARRIVAL)
            {
                x++; //increment the #of Arrival Events processed
                writeToFile("Processing Arrival Event #"+x); //write Arrival Event #
                
                customers++;    //incrememnt # of total customers that have walked through the door
                //curCustomer=customers;
                
                processArrival(newEvent, eventList, bankQueue);//processArivalEvent
                customerQueue.arrive(new Customer(newEvent.getTime()));//add customer to customer list
                //paramter newEvent.getTime()=time that the customer entered the bank
            }
            //if event == Departure Event
            else
            {
               y++; //incrememnt the #of Arrival Events Processes
               writeToFile("Processing Departure Event #"+y);//write Departure Event
               curNumOfCustomers=customers-customersServed;//determine the customers left to be serviced
               curCustomer=customers-curNumOfCustomers+1;//determine the customer at the head of the line
               //determine the time that current customer has been waiting in queue 
               //(Time at which customer became the head of the line-time of arrival)
               customerQueue.getLink(curCustomer-1).setWaitTime(customerQueue.getLink(curCustomer-2));
               //process Departure Event
               processDeparture(newEvent, eventList, bankQueue);
            }
            //update current Time of simulation
             curTime = eventList.peek().getTime();
             
             writeToFile("");//write newLine

        }
        //at the end of the simulation there may be customers who are still waiting in the queue
        //the following code updates their waiting time
        //(time of simulation end - time of arrival)
        
        //CustomerQueue is a list of all customers that have visited the bank, with the latest arrivers at 
        //the end of the list. If there are still, j, arrival events to be processed in bankQueue, they will
        //correspond to the last, j, amount of customers in the customerlist
        for(int j=0; j<bankQueue.length(); j++)
        {
            //getDeparture()==0 makes sure we do not overwrite the waitTime of a customer who is currently
            //being serviced as the simulation eneds, this has to do with the sequential order of events
            //that occur in my algorithm. Departure time for a customer is determined when the customer
            //starts being serviced
            //if the customer has not started to be serviced the departure time will be intialized to 0.00
            if(customerQueue.getLink(customers-j).getDeparture()==0)
            {
                //for each customer that has not been serviced update their waitting time
                customerQueue.getLink(customers-j).setWaitTime(timeLimit);
            }
        }
        //Write stats to the out.txt file
        printStats();
        out.close(); //close output writer
        
    }
    
    /**
     * Parameters:
     *  -e, arrival event to be processed
     *  eventList
     *  bankQueue
     *  
     *  add arrival event to the bankqueue
     *  remove arrival event from event list
     *  if the queue is empty then service begins right away and a departure event is declared
     *  
     *  Generate next arrival event and add it to the eventlist in priority order
     */
    public void processArrival(Event e, Queue<Event> eventList, Queue<Event> bankQueue)
    {        
        boolean empty = bankQueue.empty();//is the bankQueue.empty()
        bankQueue.arrive(e); //add arrival event to bankqueue
        eventList.leave(); //remove arrival event from eventList
        Event newDeparture; //decalre newDeparture event

        writeToFile("Customer" +customers+" enters the bank"); //write arrival event
        //if bankqueue is empty
        if(empty)
        {
           //line is empty so service can begin right away
           //departure event will occure at (current time+servicetime of customer)
           newDeparture = new Event(curTime+e.getServiceTime(), DEPARTURE);
           //add departure event into eventlist
           eventList.addPriority(newDeparture);
           writeToFile("     and is immediately served at "+curTime);//write to out.txt
           

           nowait++;//increment #of customers who did not have to wait in queue
        }
        //generate next arrival event
        //arrival time = (currenttime+generated arrival time)
        Event newArrival = new Event(exponentialDistribution(aveArrival)+curTime,ARRIVAL,exponentialDistribution(aveService));

        //if eventlist is empty
        if(eventList.empty())
        {
            //add to list
            eventList.arrive(newArrival);
        }
        //if eventList is not empty
        else
        {
            if(newArrival.compareTo(eventList.peek())==0)
            {
                //if new Arrival event occurs after the next departure event then add Arrival event to 
                //end of eventList
               eventList.arrive(newArrival); 
            }
            else
            {
                //if new Arrival event occurs before the next departure then add Arrival event
                //to the front of the eventlist

               eventList.addPriority(newArrival); 
               curCustomer--; //logic
            }

        }
        writeToFile("");//write newLine



    }
    /**
     * parameters:
     * e, departure eent
     * eventList
     * bankqueue
     * 
     * 
     * remove related arrivalevent from bankqueue
     * set departure time for current customer to current simulation time
     * remove departure event from event list
     * 
     * if bankqueue is not empty then generate a new departure event for the new customer at the front of
     * the line
     */
    public void processDeparture(Event e, Queue<Event> eventList, Queue<Event> bankQueue)
    {
        
        writeToFile("Customer "+curCustomer+" leaves ");//write departure event
        bankQueue.leave();  //remove arrival event that instantaneated this departure event from the queue
        customerQueue.getLink(curCustomer-1).processCustomer(curTime);//set departure time for current 
                                                //customer being departed
        
        eventList.leave();//remove departure event eventList
        Event newDeparture;//declare new dewparture event for new head of line customer
        customersServed++;  //incrememnt # of customers served
        //if queue is not empty
        if(!bankQueue.empty())
        {
           
            //create a new departure event for the current customer at the front of queue    
            newDeparture = new Event((curTime+eventList.peek().getServiceTime()), DEPARTURE);

            
            if(newDeparture.compareTo(eventList.peek())==0)
            {
                eventList.arrive(newDeparture);
                //if new departure event occurs after the next arrival event add to end of eventList

            }
            else
            {
                eventList.addPriority(newDeparture);
                //if new departure event occurs before the next arrival event add to the front of the eventList

            }
            
        }


    }
    /**
     * Prints and calculates statistics to out.txt
     */
    public void printStats()
    {
        double total=totalWaitingTime();//calculate total waiting time among all customers
        out.println("Total number of customers coming through the doors: "+customers);
        out.println("Customers Served(including those being served at end of simulation): "+(customers-curNumOfCustomers+1));
        out.println("Customers who did not hve to wait: "+nowait+" (%" +((nowait/(double)customers)*100)+")");
        out.println("Total wait time(including those who have not been serviced yet): "+total);
        out.println("Avergae waiting time: "+(total/customers));
        out.println("People still in Queue: "+(bankQueue.length()));
    }
    public double totalWaitingTime()
    {
        //for all customers in the list sum their waiting times together
        double total=0;
        for(int i=0; i<customerQueue.length(); i++)
        {
            total+=(customerQueue.getLink(i).getWaitTime());
        }
        return total;
    }
    
    //printed at beginning of simulation
    public void Greet()
    {
        System.out.println("\n\n");
        System.out.println("This Simulator works only for 1 teller at the bank");
        System.out.println("The sketch, if you choose to view it, will be located in a file \"out.txt\" "); 
        System.out.print("located in your local directory after completion");
        System.out.println();
        System.out.println();
        
        
    }
    public void writeToFile(String s)
    {
        if(showSketch)
        {
            out.println(s);
        }
    }
    
}
