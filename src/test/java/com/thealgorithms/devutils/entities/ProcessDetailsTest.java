package com.thealgorithms.devutils.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ProcessDetails
 * Tests the ProcessDetails entity used in scheduling algorithms
 *
 * @author Sourav Saha (yashsaha555)
 */
class ProcessDetailsTest {

    private ProcessDetails processWithPriority;
    private ProcessDetails processWithoutPriority;

    @BeforeEach
    void setUp() {
        processWithPriority = new ProcessDetails("P1", 0, 10, 5);
        processWithoutPriority = new ProcessDetails("P2", 2, 8);
    }

    @Test
    void testConstructorWithPriority() {
        ProcessDetails process = new ProcessDetails("P3", 1, 15, 3);

        assertEquals("P3", process.getProcessId());
        assertEquals(1, process.getArrivalTime());
        assertEquals(15, process.getBurstTime());
        assertEquals(3, process.getPriority());
        assertEquals(0, process.getWaitingTime());
        assertEquals(0, process.getTurnAroundTime());
    }

    @Test
    void testConstructorWithoutPriority() {
        ProcessDetails process = new ProcessDetails("P4", 3, 12);

        assertEquals("P4", process.getProcessId());
        assertEquals(3, process.getArrivalTime());
        assertEquals(12, process.getBurstTime());
        assertEquals(0, process.getPriority());
        assertEquals(0, process.getWaitingTime());
        assertEquals(0, process.getTurnAroundTime());
    }

    @Test
    void testGetProcessId() {
        assertEquals("P1", processWithPriority.getProcessId());
        assertEquals("P2", processWithoutPriority.getProcessId());
    }

    @Test
    void testGetArrivalTime() {
        assertEquals(0, processWithPriority.getArrivalTime());
        assertEquals(2, processWithoutPriority.getArrivalTime());
    }

    @Test
    void testGetBurstTime() {
        assertEquals(10, processWithPriority.getBurstTime());
        assertEquals(8, processWithoutPriority.getBurstTime());
    }

    @Test
    void testGetWaitingTime() {
        assertEquals(0, processWithPriority.getWaitingTime());
        assertEquals(0, processWithoutPriority.getWaitingTime());
    }

    @Test
    void testGetTurnAroundTime() {
        assertEquals(0, processWithPriority.getTurnAroundTime());
        assertEquals(0, processWithoutPriority.getTurnAroundTime());
    }

    @Test
    void testGetPriority() {
        assertEquals(5, processWithPriority.getPriority());
        assertEquals(0, processWithoutPriority.getPriority());
    }

    @Test
    void testSetProcessId() {
        processWithPriority.setProcessId("NewP1");
        assertEquals("NewP1", processWithPriority.getProcessId());

        processWithPriority.setProcessId(null);
        assertNull(processWithPriority.getProcessId());

        processWithPriority.setProcessId("");
        assertEquals("", processWithPriority.getProcessId());
    }

    @Test
    void testSetArrivalTime() {
        processWithPriority.setArrivalTime(5);
        assertEquals(5, processWithPriority.getArrivalTime());

        processWithPriority.setArrivalTime(-1);
        assertEquals(-1, processWithPriority.getArrivalTime());

        processWithPriority.setArrivalTime(0);
        assertEquals(0, processWithPriority.getArrivalTime());
    }

    @Test
    void testSetBurstTime() {
        processWithPriority.setBurstTime(20);
        assertEquals(20, processWithPriority.getBurstTime());

        processWithPriority.setBurstTime(0);
        assertEquals(0, processWithPriority.getBurstTime());

        processWithPriority.setBurstTime(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, processWithPriority.getBurstTime());
    }

    @Test
    void testSetWaitingTime() {
        processWithPriority.setWaitingTime(15);
        assertEquals(15, processWithPriority.getWaitingTime());

        processWithPriority.setWaitingTime(-5);
        assertEquals(-5, processWithPriority.getWaitingTime());

        processWithPriority.setWaitingTime(0);
        assertEquals(0, processWithPriority.getWaitingTime());
    }

    @Test
    void testSetTurnAroundTime() {
        processWithPriority.setTurnAroundTime(25);
        assertEquals(25, processWithPriority.getTurnAroundTime());

        processWithPriority.setTurnAroundTime(-10);
        assertEquals(-10, processWithPriority.getTurnAroundTime());

        processWithPriority.setTurnAroundTime(0);
        assertEquals(0, processWithPriority.getTurnAroundTime());
    }

    @Test
    void testCompleteProcessLifecycle() {
        ProcessDetails process = new ProcessDetails("P5", 0, 10, 2);

        process.setWaitingTime(5);
        process.setTurnAroundTime(15);

        assertEquals("P5", process.getProcessId());
        assertEquals(0, process.getArrivalTime());
        assertEquals(10, process.getBurstTime());
        assertEquals(5, process.getWaitingTime());
        assertEquals(15, process.getTurnAroundTime());
        assertEquals(2, process.getPriority());
    }

    @Test
    void testProcessWithMinimumValues() {
        ProcessDetails process = new ProcessDetails("", 0, 1, 0);

        assertEquals("", process.getProcessId());
        assertEquals(0, process.getArrivalTime());
        assertEquals(1, process.getBurstTime());
        assertEquals(0, process.getPriority());
    }

    @Test
    void testProcessWithMaximumValues() {
        ProcessDetails process = new ProcessDetails("LongProcessName", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals("LongProcessName", process.getProcessId());
        assertEquals(Integer.MAX_VALUE, process.getArrivalTime());
        assertEquals(Integer.MAX_VALUE, process.getBurstTime());
        assertEquals(Integer.MAX_VALUE, process.getPriority());
    }

    @Test
    void testProcessModificationAfterCreation() {
        ProcessDetails process = new ProcessDetails("Original", 1, 5, 3);

        process.setProcessId("Modified");
        process.setArrivalTime(10);
        process.setBurstTime(20);
        process.setWaitingTime(8);
        process.setTurnAroundTime(28);

        assertEquals("Modified", process.getProcessId());
        assertEquals(10, process.getArrivalTime());
        assertEquals(20, process.getBurstTime());
        assertEquals(8, process.getWaitingTime());
        assertEquals(28, process.getTurnAroundTime());
        assertEquals(3, process.getPriority());
    }

    @Test
    void testMultipleProcessesIndependence() {
        ProcessDetails process1 = new ProcessDetails("P1", 0, 5, 1);
        ProcessDetails process2 = new ProcessDetails("P2", 2, 8, 2);

        process1.setWaitingTime(10);
        process1.setTurnAroundTime(15);

        assertEquals("P1", process1.getProcessId());
        assertEquals(0, process1.getArrivalTime());
        assertEquals(5, process1.getBurstTime());
        assertEquals(1, process1.getPriority());
        assertEquals(10, process1.getWaitingTime());
        assertEquals(15, process1.getTurnAroundTime());

        assertEquals("P2", process2.getProcessId());
        assertEquals(2, process2.getArrivalTime());
        assertEquals(8, process2.getBurstTime());
        assertEquals(2, process2.getPriority());
        assertEquals(0, process2.getWaitingTime());
        assertEquals(0, process2.getTurnAroundTime());
    }

    @Test
    void testConstructorParameterOrder() {
        ProcessDetails process = new ProcessDetails("TestProcess", 123, 456, 789);

        assertEquals("TestProcess", process.getProcessId());
        assertEquals(123, process.getArrivalTime());
        assertEquals(456, process.getBurstTime());
        assertEquals(789, process.getPriority());
    }

    @Test
    void testTypicalSchedulingScenario() {
        ProcessDetails[] processes = {new ProcessDetails("P1", 0, 8, 3), new ProcessDetails("P2", 1, 4, 1), new ProcessDetails("P3", 2, 9, 4), new ProcessDetails("P4", 3, 5, 2)};

        int currentTime = 0;
        for (ProcessDetails process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            process.setWaitingTime(currentTime - process.getArrivalTime());
            currentTime += process.getBurstTime();
            process.setTurnAroundTime(process.getWaitingTime() + process.getBurstTime());
        }

        assertEquals(0, processes[0].getWaitingTime());
        assertEquals(8, processes[0].getTurnAroundTime());

        assertEquals(7, processes[1].getWaitingTime());
        assertEquals(11, processes[1].getTurnAroundTime());

        assertEquals(10, processes[2].getWaitingTime());
        assertEquals(19, processes[2].getTurnAroundTime());

        assertEquals(18, processes[3].getWaitingTime());
        assertEquals(23, processes[3].getTurnAroundTime());
    }
}
