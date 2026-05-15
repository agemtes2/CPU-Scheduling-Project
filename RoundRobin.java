import java.util.*;

public class RoundRobin {

    private final List<String> ganttLabels = new ArrayList<>();
    private final List<int[]> ganttTimes = new ArrayList<>();

    public void run(List<Process> processes, int quantum) {

        System.out.println("\n+------------------------------------------+");
        System.out.printf("|   Round Robin  (Time Quantum Q = %-3d)   |%n", quantum);
        System.out.println("+------------------------------------------+");

        Queue<Process> queue = new LinkedList<>();
        int[] remaining = new int[processes.size()];

        ganttLabels.clear();
        ganttTimes.clear();

        for (int i = 0; i < processes.size(); i++) {
            remaining[i] = processes.get(i).burstTime;
            queue.add(processes.get(i));
        }

        int time = 0;

        while (!queue.isEmpty()) {

            Process p = queue.poll();
            int index = Integer.parseInt(p.pid.substring(1)) - 1;

            int startTime = time;

            if (remaining[index] > quantum) {
                time += quantum;
                remaining[index] -= quantum;
                ganttAdd(p.pid, startTime, time);
                queue.add(p);
            } else {
                time += remaining[index];
                ganttAdd(p.pid, startTime, time);

                p.turnaroundTime = time - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                remaining[index] = 0;
            }
        }

        printGantt();
        printResults(processes);
    }

    private void ganttAdd(String label, int start, int end) {
        ganttLabels.add(label);
        ganttTimes.add(new int[]{start, end});
    }

    private void printGantt() {
        System.out.println("\nGantt Chart:");

        for (String label : ganttLabels) {
            System.out.print("| " + label + " ");
        }
        System.out.println("|");

        for (int[] time : ganttTimes) {
            System.out.print(time[0] + "\t");
        }

        if (!ganttTimes.isEmpty()) {
            System.out.println(ganttTimes.get(ganttTimes.size() - 1)[1]);
        }
    }

    private void printResults(List<Process> processes) {

        System.out.println("\nPID\tWaiting Time\tTurnaround Time");

        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (Process p : processes) {
            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;

            System.out.println(
                p.pid + "\t" +
                p.waitingTime + "\t\t" +
                p.turnaroundTime
            );
        }

        System.out.printf("%nAverage Waiting Time: %.2f%n", totalWaiting / processes.size());
        System.out.printf("Average Turnaround Time: %.2f%n", totalTurnaround / processes.size());
    }
}