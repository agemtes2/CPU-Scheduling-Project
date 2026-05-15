import java.util.*;

public class FCFS {

    public static void run(ArrayList<Process> processes) {

        int currentTime = 0;

        System.out.println("\nFCFS Scheduling:");

        // Shows execution order
        System.out.println("Execution Order:");

        for (Process p : processes) {

            // Print process execution one by one
            System.out.print(p.pid + " -> ");

            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }

            p.waitingTime = currentTime - p.arrivalTime;

            currentTime += p.burstTime;

            p.turnaroundTime = p.waitingTime + p.burstTime;
        }

        System.out.println();

        // Print results table
        System.out.println("\nPID\tWaiting Time\tTurnaround Time");

        for (Process p : processes) {
            System.out.println(
                p.pid + "\t" +
                p.waitingTime + "\t\t" +
                p.turnaroundTime
            );
        }
    }
}