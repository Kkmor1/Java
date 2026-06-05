package com.thealgorithms.datastructures.trees;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;

class RedBlackBSTTest {

    private RedBlackBST bst;

    @BeforeEach
    void setUp() {
        bst = new RedBlackBST();
    }

    // --- Invariants checking ---
    
    private void verifyInvariants() {
        if (bst.root == bst.nil) {
            return;
        }
        
        // 1. Root must be black
        assertThat(bst.root.color).as("Root must be black").isEqualTo(RedBlackBST.BLACK);
        
        // 2. Red nodes cannot have red children
        verifyRedNodesHaveBlackChildren(bst.root);
        
        // 3. All paths from any given node to its leaf nodes must have the same number of black nodes
        verifyBlackHeight(bst.root);
    }

    private void verifyRedNodesHaveBlackChildren(RedBlackBST.Node node) {
        if (node == bst.nil) {
            return;
        }
        if (node.color == RedBlackBST.RED) {
            assertThat(node.left.color).as("Red node's left child must be black").isEqualTo(RedBlackBST.BLACK);
            assertThat(node.right.color).as("Red node's right child must be black").isEqualTo(RedBlackBST.BLACK);
        }
        verifyRedNodesHaveBlackChildren(node.left);
        verifyRedNodesHaveBlackChildren(node.right);
    }

    private int verifyBlackHeight(RedBlackBST.Node node) {
        if (node == bst.nil) {
            return 1;
        }
        int leftBlackHeight = verifyBlackHeight(node.left);
        int rightBlackHeight = verifyBlackHeight(node.right);
        
        assertThat(leftBlackHeight).as("Black height must be the same for all paths").isEqualTo(rightBlackHeight);
        
        return leftBlackHeight + (node.color == RedBlackBST.BLACK ? 1 : 0);
    }

    // --- Empty Tree Tests ---

    @Test
    void testEmptyTree() {
        assertThat(bst.search(10)).isFalse();
        assertThat(bst.delete(10)).isFalse();
        assertThat(bst.getMinimum()).isNull();
        assertThat(bst.getMaximum()).isNull();
        assertThat(bst.getSuccessor(10)).isNull();
        assertThat(bst.getPredecessor(10)).isNull();
        verifyInvariants();
    }

    // --- Single Node Tests ---

    @Test
    void testSingleNodeInsertSearchDelete() {
        bst.insert(10);
        assertThat(bst.search(10)).isTrue();
        assertThat(bst.getMinimum()).isEqualTo(10);
        assertThat(bst.getMaximum()).isEqualTo(10);
        
        verifyInvariants();

        assertThat(bst.delete(10)).isTrue();
        assertThat(bst.search(10)).isFalse();
        
        verifyInvariants();
    }

    // --- Multi-Node Insert Tests ---

    @Test
    void testMultiNodeInsert() {
        // This sequence is carefully chosen to trigger all cases in fixTree:
        // - Parent is left/right child
        // - Node is left/right child
        // - Uncle is RED (color flips)
        // - Uncle is BLACK (rotations)
        int[] keys = {
            30, 10, 20, // Triggers Left-Right case
            40, 50,     // Triggers Right-Right case
            60,         // Triggers Color Flip
            5, 1        // Triggers Left-Left case
        };
        for (int key : keys) {
            bst.insert(key);
            verifyInvariants();
        }

        for (int key : keys) {
            assertThat(bst.search(key)).isTrue();
        }
    }

    // --- Predecessor / Successor Tests ---

    @Test
    void testMinMaxPredecessorSuccessor() {
        // Insert: 10, 20, 30, 15, 25, 5, 1
        // Sorted: 1, 5, 10, 15, 20, 25, 30
        int[] keys = {10, 20, 30, 15, 25, 5, 1};
        for (int key : keys) {
            bst.insert(key);
        }

        assertThat(bst.getMinimum()).isEqualTo(1);
        assertThat(bst.getMaximum()).isEqualTo(30);

        assertThat(bst.getPredecessor(15)).isEqualTo(10);
        assertThat(bst.getSuccessor(15)).isEqualTo(20);
        
        assertThat(bst.getPredecessor(1)).isNull();
        assertThat(bst.getSuccessor(30)).isNull();
        
        assertThat(bst.getPredecessor(99)).isNull();
        assertThat(bst.getSuccessor(99)).isNull();
    }

    // --- Duplicate Keys Tests ---

    @Test
    void testDuplicateKeys() {
        bst.insert(10);
        bst.insert(10);
        bst.insert(10);
        
        verifyInvariants();
        
        assertThat(bst.search(10)).isTrue();
        
        // Removing one duplicate
        assertThat(bst.delete(10)).isTrue();
        assertThat(bst.search(10)).isTrue();
        
        // Removing second
        assertThat(bst.delete(10)).isTrue();
        assertThat(bst.search(10)).isTrue();
        
        // Removing third
        assertThat(bst.delete(10)).isTrue();
        assertThat(bst.search(10)).isFalse();
    }

    // --- Boundary Deletion Scenarios ---

    @Test
    void testBoundaryDeletion() {
        int[] keys = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93, 70, 84, 99};
        for (int key : keys) {
            bst.insert(key);
        }
        
        // Delete various nodes to trigger deleteFixup cases
        int[] toDelete = {6, 12, 93, 25, 50, 75, 87, 37, 62};
        for (int key : toDelete) {
            assertThat(bst.delete(key)).isTrue();
            verifyInvariants();
            assertThat(bst.search(key)).isFalse();
        }
    }
    
    @Test
    void testDeleteAllNodes() {
        int[] keys = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93};
        for (int key : keys) {
            bst.insert(key);
        }
        for (int key : keys) {
            assertThat(bst.delete(key)).isTrue();
            verifyInvariants();
            assertThat(bst.search(key)).isFalse();
        }
        assertThat(bst.getMinimum()).isNull();
        assertThat(bst.getMaximum()).isNull();
    }
    
    @Test
    void testPrintTree() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        
        java.io.PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        try {
            bst.printTree(bst.root);
            bst.printTreepre(bst.root);
        } finally {
            System.setOut(originalOut);
        }
        
        assertThat(outContent.toString()).contains("Key: 10");
        assertThat(outContent.toString()).contains("Key: 5");
        assertThat(outContent.toString()).contains("Key: 15");
    }
    
    @Test
    void testInsertDemo() {
        String input = "10\n20\n30\n-999\n";
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        try {
            bst.insertDemo();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        
        assertThat(bst.search(10)).isTrue();
        assertThat(bst.search(20)).isTrue();
        assertThat(bst.search(30)).isTrue();
    }
    
    @Test
    void testDeleteDemo() {
        bst.insert(10);
        bst.insert(20);
        
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        
        String input = "10\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        try {
            bst.deleteDemo();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        
        assertThat(bst.search(10)).isFalse();
        assertThat(bst.search(20)).isTrue();
        
        // Test deleting non-existent item
        input = "99\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        try {
            bst.deleteDemo();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        
        assertThat(outContent.toString()).contains("does not exist!");
    }
}
