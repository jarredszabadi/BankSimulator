// The "BankSimulator" class.
// This simulates customers arriving in a single queue and being served
// by a number of clerks. The purpose of the simulation is to determine
// the average time a customer waits in the queue before being served.

import java.io.*;
import java.util.*;

public class BankSimulator {
	// The main method. This throws an IOException and
	// NumberFormatException because we do not handle any
	// exceptions from reading numbers from the keyboard in the
	// method.
	public static void main(String args[]) throws IOException,
			NumberFormatException {
		Scanner reader = new Scanner(System.in);

		// Here are the parameters for a simulation:
		// time between customer arrivals, number of servers (clerks),
		// time for service, time limit for the simulation, and whether
		// the execution is traced.
		double avgInterarrivalTime;
		int numberOfServers;
		double avgServiceTime;
		double timeLimit;
		boolean tracing;

		String line;

		System.out.print("Simulation of service for people at a service ");
		System.out.println("counter at a bank");
		System.out.println();
		System.out.println("Please give these parameters for the simulation: ");

		System.out.print("Average time between arrivals: ");
		avgInterarrivalTime = reader.nextDouble();

		
		numberOfServers = 1;

		System.out.print("Average time to serve a customer: ");
		avgServiceTime = reader.nextDouble();

		System.out.print("Time limit for simulation: ");
		timeLimit = reader.nextDouble();

		System.out.print("Show trace (y or n): ");
		line = reader.next();
		if (line.equals("y")) {
			tracing = true;
		} else {
			tracing = false;
		}

		// Create a new simulator object with the simulation parameters.
		Simulator simulator = new Simulator(avgInterarrivalTime,
				numberOfServers, avgServiceTime);

		// Run the simulation for the specified length of time.
		simulator.run(timeLimit, tracing);
	} // main method
} /* BankSimulator class */
