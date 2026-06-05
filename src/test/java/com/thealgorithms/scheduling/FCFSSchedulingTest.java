package com.thealgorithms.scheduling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thealgorithms.devutils.entities.ProcessDetails;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FCFSSchedulingTest {

    @Test
    public void testingProcesses() {
        List<ProcessDetails> processes = addProcessesForFCFS();
        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes); // for sending to FCFS

        fcfsScheduling.scheduleProcesses();

        assertEquals(3, processes.size());

        // Since processes are sorted by arrival time, P1 (0), P2 (1), P3 (2)
        assertEquals("P1", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(10, processes.get(0).getTurnAroundTime());

        assertEquals("P2", processes.get(1).getProcessId());
        assertEquals(9, processes.get(1).getWaitingTime()); // P1 ends at 10, P2 arrives at 1. Wait time = 10 - 1 = 9
        assertEquals(14, processes.get(1).getTurnAroundTime()); // Wait time (9) + burst (5) = 14

        assertEquals("P3", processes.get(2).getProcessId());
        assertEquals(13, processes.get(2).getWaitingTime()); // P2 ends at 15, P3 arrives at 2. Wait time = 15 - 2 = 13
        assertEquals(21, processes.get(2).getTurnAroundTime()); // Wait time (13) + burst (8) = 21
    }

    @Test
    public void testingProcessesOutOfOrder() {
        final ProcessDetails process1 = new ProcessDetails("P1", 2, 8);
        final ProcessDetails process2 = new ProcessDetails("P2", 0, 10);
        final ProcessDetails process3 = new ProcessDetails("P3", 1, 5);

        final List<ProcessDetails> processes = new ArrayList<>();
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);

        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes);
        fcfsScheduling.scheduleProcesses();

        assertEquals(3, processes.size());

        // Sorted: P2(0), P3(1), P1(2)
        assertEquals("P2", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(10, processes.get(0).getTurnAroundTime());

        assertEquals("P3", processes.get(1).getProcessId());
        assertEquals(9, processes.get(1).getWaitingTime()); // 10 - 1
        assertEquals(14, processes.get(1).getTurnAroundTime());

        assertEquals("P1", processes.get(2).getProcessId());
        assertEquals(13, processes.get(2).getWaitingTime()); // 15 - 2
        assertEquals(21, processes.get(2).getTurnAroundTime());
    }

    private List<ProcessDetails> addProcessesForFCFS() {
        final ProcessDetails process1 = new ProcessDetails("P1", 0, 10);
        final ProcessDetails process2 = new ProcessDetails("P2", 1, 5);
        final ProcessDetails process3 = new ProcessDetails("P3", 2, 8);

        final List<ProcessDetails> processDetails = new ArrayList<>();
        processDetails.add(process1);
        processDetails.add(process2);
        processDetails.add(process3);

        return processDetails;
    }
}
