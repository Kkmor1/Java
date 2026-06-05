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
        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes);

        fcfsScheduling.scheduleProcesses();

        assertEquals(3, processes.size());

        assertEquals("P1", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(10, processes.get(0).getTurnAroundTime());

        assertEquals("P2", processes.get(1).getProcessId());
        assertEquals(9, processes.get(1).getWaitingTime());
        assertEquals(14, processes.get(1).getTurnAroundTime());

        assertEquals("P3", processes.get(2).getProcessId());
        assertEquals(13, processes.get(2).getWaitingTime());
        assertEquals(21, processes.get(2).getTurnAroundTime());
    }

    @Test
    public void testingProcessesWithDifferentArrivalTimes() {
        List<ProcessDetails> processes = addProcessesWithDifferentArrivalTimes();
        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes);

        fcfsScheduling.scheduleProcesses();

        assertEquals(3, processes.size());

        assertEquals("P2", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getArrivalTime());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(3, processes.get(0).getTurnAroundTime());

        assertEquals("P3", processes.get(1).getProcessId());
        assertEquals(2, processes.get(1).getArrivalTime());
        assertEquals(1, processes.get(1).getWaitingTime());
        assertEquals(7, processes.get(1).getTurnAroundTime());

        assertEquals("P1", processes.get(2).getProcessId());
        assertEquals(5, processes.get(2).getArrivalTime());
        assertEquals(4, processes.get(2).getWaitingTime());
        assertEquals(14, processes.get(2).getTurnAroundTime());
    }

    @Test
    public void testingProcessesWithCpuIdleGaps() {
        List<ProcessDetails> processes = addProcessesWithCpuIdleGaps();
        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes);

        fcfsScheduling.scheduleProcesses();

        assertEquals(3, processes.size());

        assertEquals("P2", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getArrivalTime());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(2, processes.get(0).getTurnAroundTime());

        assertEquals("P1", processes.get(1).getProcessId());
        assertEquals(5, processes.get(1).getArrivalTime());
        assertEquals(0, processes.get(1).getWaitingTime());
        assertEquals(3, processes.get(1).getTurnAroundTime());

        assertEquals("P3", processes.get(2).getProcessId());
        assertEquals(10, processes.get(2).getArrivalTime());
        assertEquals(0, processes.get(2).getWaitingTime());
        assertEquals(4, processes.get(2).getTurnAroundTime());
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

    private List<ProcessDetails> addProcessesWithDifferentArrivalTimes() {
        final ProcessDetails process1 = new ProcessDetails("P1", 5, 10);
        final ProcessDetails process2 = new ProcessDetails("P2", 0, 3);
        final ProcessDetails process3 = new ProcessDetails("P3", 2, 6);

        final List<ProcessDetails> processDetails = new ArrayList<>();
        processDetails.add(process1);
        processDetails.add(process2);
        processDetails.add(process3);

        return processDetails;
    }

    private List<ProcessDetails> addProcessesWithCpuIdleGaps() {
        final ProcessDetails process1 = new ProcessDetails("P1", 5, 3);
        final ProcessDetails process2 = new ProcessDetails("P2", 0, 2);
        final ProcessDetails process3 = new ProcessDetails("P3", 10, 4);

        final List<ProcessDetails> processDetails = new ArrayList<>();
        processDetails.add(process1);
        processDetails.add(process2);
        processDetails.add(process3);

        return processDetails;
    }
}