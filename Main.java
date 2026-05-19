import java.util.*;

class Process {
    String pid;
    int arrivalTime, burstTime, priority;
    int ioStartTime, ioDuration;

    int remainingBurst;
    int cpuUsed;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    boolean ioDone = false;

    Process(String pid, int arrivalTime, int burstTime, int priority, int ioStartTime, int ioDuration) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.ioStartTime = ioStartTime;
        this.ioDuration = ioDuration;
        this.remainingBurst = burstTime;
    }

    Process copy() {
        return new Process(pid, arrivalTime, burstTime, priority, ioStartTime, ioDuration);
    }
}

class Scheduler {

    static void runFCFS(ArrayList<Process> original) {
        runSimulation(original, "FCFS", 0);
    }

    static void runRoundRobin(ArrayList<Process> original, int quantum) {
        runSimulation(original, "Round Robin", quantum);
    }

    static void runPriority(ArrayList<Process> original) {
        runSimulation(original, "Priority", 0);
    }

    static void runSimulation(ArrayList<Process> original, String type, int quantum) {
        ArrayList<Process> processes = new ArrayList<>();
        for (Process p : original) processes.add(p.copy());

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> ioQueue = new LinkedList<>();

        Process running = null;
        Process ioProcess = null;

        int ioFinishTime = -1;
        int time = 0;
        int completed = 0;
        int index = 0;
        int quantumUsed = 0;

        ArrayList<String> ganttNames = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);

        System.out.println("\n================ " + type + " Scheduling ================");

        while (completed < processes.size()) {

            while (index < processes.size() && processes.get(index).arrivalTime <= time) {
                readyQueue.add(processes.get(index));
                index++;
            }

            if (ioProcess != null && time == ioFinishTime) {
                ioProcess.ioDone = true;
                readyQueue.add(ioProcess);
                ioProcess = null;

                if (!ioQueue.isEmpty()) {
                    ioProcess = ioQueue.poll();
                    ioFinishTime = time + ioProcess.ioDuration;
                }
            }

            if (running == null && !readyQueue.isEmpty()) {
                if (type.equals("Priority")) {
                    Process highest = null;
                    for (Process p : readyQueue) {
                        if (highest == null || p.priority < highest.priority) {
                            highest = p;
                        }
                    }
                    readyQueue.remove(highest);
                    running = highest;
                } else {
                    running = readyQueue.poll();
                }
                quantumUsed = 0;
            }

            for (Process p : readyQueue) {
                p.waitingTime++;
            }

            if (running == null) {
                addGantt(ganttNames, ganttTimes, "WASTE", time + 1);
                time++;
                continue;
            }

            addGantt(ganttNames, ganttTimes, running.pid, time + 1);

            running.cpuUsed++;
            running.remainingBurst--;
            quantumUsed++;
            time++;

            if (!running.ioDone && running.cpuUsed == running.ioStartTime && running.remainingBurst > 0) {
                if (ioProcess == null) {
                    ioProcess = running;
                    ioFinishTime = time + running.ioDuration;
                } else {
                    ioQueue.add(running);
                }
                running = null;
            }
            else if (running.remainingBurst == 0) {
                running.completionTime = time;
                running.turnaroundTime = running.completionTime - running.arrivalTime;
                completed++;
                running = null;
            }
            else if (type.equals("Round Robin") && quantumUsed == quantum) {
                readyQueue.add(running);
                running = null;
            }
        }

        printGantt(ganttNames, ganttTimes);
        printTable(processes);
    }

    static void addGantt(ArrayList<String> names, ArrayList<Integer> times, String name, int endTime) {
        if (names.size() > 0 && names.get(names.size() - 1).equals(name)) {
            times.set(times.size() - 1, endTime);
        } else {
            names.add(name);
            times.add(endTime);
        }
    }

    static void printGantt(ArrayList<String> names, ArrayList<Integer> times) {
        System.out.println("\nGantt Chart:");

        for (String name : names) {
            System.out.print("| " + name + " ");
        }
        System.out.println("|");

        for (int t : times) {
            System.out.print(t + "\t");
        }
        System.out.println();
    }

    static void printTable(ArrayList<Process> processes) {
        System.out.println("\nPID\tArrival\tBurst\tPriority\tI/O\tWaiting\tTurnaround");

        for (Process p : processes) {
            System.out.println(
                p.pid + "\t" +
                p.arrivalTime + "\t" +
                p.burstTime + "\t" +
                p.priority + "\t\t" +
                "[" + p.ioStartTime + "," + p.ioDuration + "]\t" +
                p.waitingTime + "\t" +
                p.turnaroundTime
            );
        }
    }
}

public class Main {
    public static void main(String[] args) {

        ArrayList<Process> processes = new ArrayList<>();

        // PID, Arrival Time, Burst Time, Priority, I/O Start, I/O Duration
        processes.add(new Process("P1", 0, 10, 3, 2, 3));
        processes.add(new Process("P2", 1, 5, 1, 1, 2));
        processes.add(new Process("P3", 2, 8, 4, 3, 1));
        processes.add(new Process("P4", 3, 6, 2, 2, 2));
        processes.add(new Process("P5", 4, 7, 5, 3, 2));
        processes.add(new Process("P6", 5, 4, 2, 1, 1));
        processes.add(new Process("P7", 6, 9, 1, 4, 3));
        processes.add(new Process("P8", 7, 5, 3, 2, 2));
        processes.add(new Process("P9", 8, 6, 4, 3, 1));
        processes.add(new Process("P10", 9, 8, 2, 4, 2));

        processes.add(new Process("P11", 10, 3, 5, 1, 1));
        processes.add(new Process("P12", 11, 7, 1, 3, 2));
        processes.add(new Process("P13", 12, 5, 3, 2, 1));
        processes.add(new Process("P14", 13, 9, 2, 4, 3));
        processes.add(new Process("P15", 14, 4, 4, 1, 2));
        processes.add(new Process("P16", 15, 6, 1, 3, 1));
        processes.add(new Process("P17", 16, 8, 5, 4, 2));
        processes.add(new Process("P18", 17, 5, 3, 2, 2));
        processes.add(new Process("P19", 18, 7, 2, 3, 1));
        processes.add(new Process("P20", 19, 6, 4, 2, 3));

        Scheduler.runFCFS(processes);
        Scheduler.runRoundRobin(processes, 3);
        Scheduler.runPriority(processes);
    }
}