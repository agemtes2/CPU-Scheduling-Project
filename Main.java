import java.util.*;

class Process {
    String pid;
    int arrivalTime;
    int burstTime;
    int priority;

    int waitingTime;
    int turnaroundTime;

    public Process(String pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

class FCFS {
    public static void run(ArrayList<Process> processes) {
        int currentTime = 0;

        System.out.println("\n================ FCFS Scheduling ================");
        System.out.println("Execution Order:");

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

        System.out.printf("%-5s %-15s %-15s%n",
                "PID", "Waiting Time", "Turnaround Time");

        for (Process p : processes) {
            System.out.printf("%-5s %-15d %-15d%n",
                    p.pid, p.waitingTime, p.turnaroundTime);
        }
    }
}

class RoundRobin {
    public static void run(ArrayList<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>();
        int[] remainingBurst = new int[processes.size()];

        for (int i = 0; i < processes.size(); i++) {
            remainingBurst[i] = processes.get(i).burstTime;
            queue.add(processes.get(i));
        }

        int time = 0;

        System.out.println("\n================ Round Robin Scheduling ================");
        System.out.println("Time Quantum = " + quantum);
        System.out.println("Execution Order:");

        while (!queue.isEmpty()) {
            Process p = queue.poll();
            int index = Integer.parseInt(p.pid.substring(1)) - 1;

            System.out.print(p.pid + " -> ");

            if (remainingBurst[index] > quantum) {
                time += quantum;
                remainingBurst[index] -= quantum;
                queue.add(p);
            } else {
                time += remainingBurst[index];

                p.turnaroundTime = time - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                remainingBurst[index] = 0;
            }
        }

        System.out.println();

        System.out.printf("%-5s %-15s %-15s%n",
                "PID", "Waiting Time", "Turnaround Time");

        for (Process p : processes) {
            System.out.printf("%-5s %-15d %-15d%n",
                    p.pid, p.waitingTime, p.turnaroundTime);
        }
    }
}

class PriorityScheduling {
    public static void run(ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.priority));

        int currentTime = 0;

        System.out.println("\n================ Priority Scheduling ================");
        System.out.println("Smaller priority number = higher priority");
        System.out.println("Execution Order:");

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

        System.out.printf("%-5s %-10s %-15s %-15s%n",
                "PID", "Priority", "Waiting Time", "Turnaround Time");

        for (Process p : processes) {
            System.out.printf("%-5s %-10d %-15d %-15d%n",
                    p.pid, p.priority, p.waitingTime, p.turnaroundTime);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();

        // PID, Arrival Time, Burst Time, Priority
        processes.add(new Process("P1", 0, 10, 3));
        processes.add(new Process("P2", 1, 5, 1));
        processes.add(new Process("P3", 2, 8, 4));
        processes.add(new Process("P4", 3, 6, 2));
        processes.add(new Process("P5", 4, 7, 5));
        processes.add(new Process("P6", 5, 4, 2));
        processes.add(new Process("P7", 6, 9, 1));
        processes.add(new Process("P8", 7, 5, 3));
        processes.add(new Process("P9", 8, 6, 4));
        processes.add(new Process("P10", 9, 8, 2));
        processes.add(new Process("P11", 10, 3, 5));
        processes.add(new Process("P12", 11, 7, 1));
        processes.add(new Process("P13", 12, 5, 3));
        processes.add(new Process("P14", 13, 9, 2));
        processes.add(new Process("P15", 14, 4, 4));
        processes.add(new Process("P16", 15, 6, 1));
        processes.add(new Process("P17", 16, 8, 5));
        processes.add(new Process("P18", 17, 5, 3));
        processes.add(new Process("P19", 18, 7, 2));
        processes.add(new Process("P20", 19, 6, 4));

        FCFS.run(new ArrayList<>(processes));
        RoundRobin.run(new ArrayList<>(processes), 3);
        PriorityScheduling.run(new ArrayList<>(processes));
    }
}
