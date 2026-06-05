package com.thealgorithms.scheduling;

import com.thealgorithms.devutils.entities.ProcessDetails;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Md Asif Joardar
 * The Round-robin scheduling algorithm is a kind of preemptive First come, First Serve CPU
 * Scheduling algorithm. This can be understood here -
 * https://www.scaler.com/topics/round-robin-scheduling-in-os/
 */

public class RRScheduling {
    private List<ProcessDetails> processes;
    private int quantumTime;

    RRScheduling(final List<ProcessDetails> processes, int quantumTime) {
        this.processes = processes;
        this.quantumTime = quantumTime;
    }

    public void scheduleProcesses() {
        evaluateTurnAroundTime();
        evaluateWaitingTime();
    }

    private void evaluateTurnAroundTime() {
        int processesNumber = processes.size();

        if (processesNumber == 0) {
            return;
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        int currentTime = 0;
        int completed = 0;
        int[] mark = new int[processesNumber];
        Arrays.fill(mark, 0);
        mark[0] = 1;

        int[] remainingBurstTime = new int[processesNumber];
        for (int i = 0; i < processesNumber; i++) {
            remainingBurstTime[i] = processes.get(i).getBurstTime();
        }

        while (completed != processesNumber) {
            int index = queue.poll();

            if (remainingBurstTime[index] == processes.get(index).getBurstTime()) {
                currentTime = Math.max(currentTime, processes.get(index).getArrivalTime());
            }

            if (remainingBurstTime[index] - quantumTime > 0) {
                remainingBurstTime[index] -= quantumTime;
                currentTime += quantumTime;
            } else {
                currentTime += remainingBurstTime[index];
                processes.get(index).setTurnAroundTime(currentTime - processes.get(index).getArrivalTime());
                completed++;
                remainingBurstTime[index] = 0;
            }

            for (int i = 1; i < processesNumber; i++) {
                if (remainingBurstTime[i] > 0 && processes.get(i).getArrivalTime() <= currentTime && mark[i] == 0) {
                    mark[i] = 1;
                    queue.add(i);
                }
            }

            if (remainingBurstTime[index] > 0) {
                queue.add(index);
            }

            if (queue.isEmpty()) {
                for (int i = 1; i < processesNumber; i++) {
                    if (remainingBurstTime[i] > 0) {
                        mark[i] = 1;
                        queue.add(i);
                        break;
                    }
                }
            }
        }
    }

    private void evaluateWaitingTime() {
        for (final ProcessDetails process : processes) {
            process.setWaitingTime(process.getTurnAroundTime() - process.getBurstTime());
        }
    }
}
