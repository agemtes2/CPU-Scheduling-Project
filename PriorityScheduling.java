import java.util.*;

public class PriorityScheduling {

    public static void run(ArrayList<Process> processes) {

        processes.sort(Comparator.comparingInt(p -> p.priority));

        int currentTime = 0;

        System.out.println("\nPriority Scheduling:");
        System.out.println("Smaller priority number = higher priority");

        System.out.println("\nExecution Order:");

        for (Process p : processes) {

            System.out.print(p.pid + " -> ");

            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }

            p.waitingTime = currentTime - p.arrivalTime;

            currentTime += p.burstTime;

            p.turnaroundTime = p.waitingTime + p.burstTime;
        }

        System.out.println();

        System.out.println("\nPID\tPriority\tWaiting Time\tTurnaround Time");

        for (Process p : processes) {
            System.out.println(
                p.pid + "\t" +
                p.priority + "\t\t" +
                p.waitingTime + "\t\t" +
                p.turnaroundTime
            );
        }
    }
}