import java.io.*;
// The "Queue" class.
// Implements a queue using a linked list. A queue consists of a list (a 
// sequence) of elements of type E. The elements can be added to and 
// removed from the list in a first-in-first-out (FIFO) basis.
public class Queue<E>{
    //circular linked queue
    public LinkEntry<E> back; //the entry point to the queue is the back of the queue
    private static int LIMIT = 100;//limit is 31
    public int size; //current size of the queue
    
    // Create a new Queue object. The queue is empty to start.
    public Queue()
    {
        back=null;
        size=0;
    }

    // Add element to the rear of the queue. The queue must not be full.
    public void arrive(E element) 
    {
        LinkEntry<E> newLink;
        newLink = new LinkEntry(element,back); //initialize newLink to point to back
        if(empty())
        {
            newLink.setLink(newLink);//link newLink to itself if queue is empty
        }
        else if(size>=LIMIT)
        {
             throw new QueueException("QueueException on arrive: Queue reached capacity");
        }
        else//add to back of queue
        {
           newLink.setLink(back.getLink());//set newLink.link to the first element
           back.setLink(newLink); //set back.link to newLink
        }
        back=newLink; //back=newLink
        size++;
       
    }
    //add an element to the front of the queue
    //used for the eventList only
    //pushes the front link back one node
    public void addPriority(E element)
    {
        LinkEntry<E> newLink;
        newLink = new LinkEntry(element, back);//intialize newLink 
        if(empty())
        {
            newLink.setLink(newLink);//if empty link newLink to itself
        }
        else if(size>=LIMIT)
        {
             throw new QueueException("QueueException on arrive: Queue reached capacity");
        }
        else//add to front of list
        {
            newLink.setLink(back.getLink());//newLink.link=first
            back.setLink(newLink);//back.setLink=newLink
        }
        back = newLink.getLink(); //back = old first link
        size++;
    }
    

    // Remove the element at the front of the queue. The queue must not be
    // empty.
    E leave() 
    {
        //as long as queue is not empty
       if(!empty())
       {
           LinkEntry<E> firstLink = back.getLink();//get the first link in the queue
           if(firstLink==back)//only one element in the queue?
           {
               back = null;//set queue to empty state
               
           }
           else
           {
               back.setLink(firstLink.getLink());//remove first link from the queue by
                                    //delinking it
               
           }
           size--;//decreent size
           return firstLink.getElement();
       }//else...if queue empty
       else{
           throw new QueueException("QueueException on dequeue: Queue empty");
       }
       
    }

    // Return the element at the front of the queue, without
    // changing the queueu. The queue must not be empty.
    E peek()
    {
       if(!empty())//return first link
       {
           LinkEntry<E> firstLink = back.getLink();
           return firstLink.getElement();
       }
       else{
           throw new QueueException("QueueException on peek: Queue empty");
       }
    }

    // Return number of elements in the queue.
    int length() {return size;}

    // Return true if the queue contai  ns no elements.
    boolean empty() {
        return back==null;
    }

    // Return true if the queue cannot hold any more elements.
    boolean full() {return size>LIMIT;}
    /**
     * Was used for testing purposes only on the customer queue,
     * outputs "Customer #, Arrival Time, Departure Time, Waiting Time"
     * by calling the toString method found under Customer Class
     * returns the whole queue in one indented string
    public String print()
    {
        String s = "";
        int x =1;
        if (back ==null)
        {
            return null;
        }
        LinkEntry<E> first= back.getLink();
        LinkEntry<E> cur = first;
        do
        {
            s=s+"Customer "+x+": "+(cur.getElement().toString());
            s+="\n";
            x++;
            cur = cur.getLink();
        }while(cur!=first);
        return s;
       
    }
    */
   //returns the element belonging to the link at the specific location in the queue
    public E getLink(int x)
    {
        LinkEntry<E> first = back.getLink();//first = first link in the list
        for(int i=0; i<x; i++)
        {
            first=first.getLink();//first=first.link
        }
        return first.getElement();
    }

} /* Queue class */
