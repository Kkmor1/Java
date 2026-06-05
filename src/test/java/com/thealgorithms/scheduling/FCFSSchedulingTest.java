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
        assertEquals(12, processes.get(2).getWaitingTime());
        assertEquals(20, processes.get(2).getTurnAroundTime());
    }

    @Test
    public void testingProcessesWithDifferentArrivalTimes() {
        List<ProcessDetails> processes = addProcessesWithDifferentArrivalTimes();
        final FCFSScheduling fcfsScheduling = new FCFSScheduling(processes);

        fcfsScheduling.scheduleProcesses();

        assertEquals(4, processes.size());

        assertEquals("P2", processes.get(0).getProcessId());
        assertEquals(0, processes.get(0).getWaitingTime());
        assertEquals(3, processes.get(0).getTurnAroundTime());

        assertEquals("P1", processes.get(1).getProcessId());
        assertEquals(2, processes.get(1).getWaitingTime());
        assertEquals(7, processes.get(1).getTurnAroundTime());

        assertEquals("P4", processes.get(2).getProcessId());
        assertEquals(4, processes.get(2).getWaitingTime());
        assertEquals(8, processes.get(2).getTurnAroundTime());

        assertEquals("P3", processes.get(3).getProcessId());
        assertEquals(7, processes.get(3).getWaitingTime());
        assertEquals(11, processes.get(3).getTurnAroundTime());
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
        final ProcessDetails process1 = new ProcessDetails("P1", 2, 5);
        final ProcessDetails process2 = new ProcessDetails("P2", 0, 3);
        final ProcessDetails process3 = new ProcessDetails("P3", 5, 4);
        final ProcessDetails process4 = new ProcessDetails("P4", 3, 4);

        final List<ProcessDetails> processDetails = new ArrayList<>();
        processDetails.add(process1);
        processDetails.add(process2);
        processDetails.add(process3);
        processDetails.add(process4);

        return processDetails;
    }
}
