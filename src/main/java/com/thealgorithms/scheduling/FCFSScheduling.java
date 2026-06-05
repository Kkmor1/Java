package com.thealgorithms.scheduling;

import com.thealgorithms.devutils.entities.ProcessDetails;
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
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        evaluateWaitingTime();
        evaluateTurnAroundTime();
    }

    private void evaluateWaitingTime() {
        int processesNumber = processes.size();

        if (processesNumber == 0) {
            return;
        }

        int currentTime = 0;
        for (ProcessDetails process : processes) {
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
