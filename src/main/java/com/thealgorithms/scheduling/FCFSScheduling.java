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

        for (final var process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            process.setWaitingTime(currentTime - process.getArrivalTime());
            currentTime += process.getBurstTime();
        }
    }

    private void evaluateTurnAroundTime() {
        for (final var process : processes) {
            process.setTurnAroundTime(process.getBurstTime() + process.getWaitingTime());
        }
    }
}
