package com.thealgorithms.scheduling;

import com.thealgorithms.devutils.entities.ProcessDetails;
import java.util.Comparator;
import java.util.List;

/**
 * Non-pre-emptive First Come First Serve scheduling. This can be understood here -
 * https://www.scaler.com/topics/first-come-first-serve/
 */
public class FCFSScheduling {

    private List<ProcessDetails> processes;

    FCFSScheduling(final List<ProcessDetails> processes) {
        this.processes = processes;
    }

    public void scheduleProcesses() {
        processes.sort(Comparator.comparingInt(ProcessDetails::getArrivalTime));
        evaluateWaitingTime();
        evaluateTurnAroundTime();
    }

    private void evaluateWaitingTime() {
        int processesNumber = processes.size();

        if (processesNumber == 0) {
            return;
        }

        int currentTime = processes.get(0).getArrivalTime();
        int burstTime = processes.get(0).getBurstTime();

        processes.get(0).setWaitingTime(0);
        currentTime += burstTime;

        for (int i = 1; i < processesNumber; i++) {
            ProcessDetails currentProcess = processes.get(i);
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }
            currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
            currentTime += currentProcess.getBurstTime();
        }
    }

    private void evaluateTurnAroundTime() {
        for (final var process : processes) {
            process.setTurnAroundTime(process.getBurstTime() + process.getWaitingTime());
        }
    }
}
