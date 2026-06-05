package com.thealgorithms.datastructures.trees;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedBlackBSTTest {

    private RedBlackBST tree;

    @BeforeEach
    void setUp() {
        tree = new RedBlackBST();
    }

    @Test
    void testEmptyTree() {
        assertTrue(tree.isEmpty());
        assertFalse(tree.search(1));
        assertThat(tree.treeMinimum()).isNull();
        assertThat(tree.treeMaximum()).isNull();
        assertThat(tree.inOrderTraversal()).isEmpty();
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testSingleNodeInsertion() {
        tree.insert(10);

        assertFalse(tree.isEmpty());
        assertTrue(tree.search(10));
        assertThat(tree.treeMinimum()).isEqualTo(10);
        assertThat(tree.treeMaximum()).isEqualTo(10);
        assertThat(tree.inOrderTraversal()).containsExactly(10);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testSingleNodeDeletion() {
        tree.insert(10);
        boolean deleted = tree.delete(10);

        assertTrue(deleted);
        assertTrue(tree.isEmpty());
        assertFalse(tree.search(10));
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testMultipleNodesInsertionLeftLeftCase() {
        tree.insert(30);
        tree.insert(20);
        tree.insert(10);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.search(20)).isTrue();
        assertThat(tree.search(30)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 20, 30);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testMultipleNodesInsertionRightRightCase() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.search(20)).isTrue();
        assertThat(tree.search(30)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 20, 30);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testMultipleNodesInsertionLeftRightCase() {
        tree.insert(30);
        tree.insert(10);
        tree.insert(20);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.search(20)).isTrue();
        assertThat(tree.search(30)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 20, 30);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testMultipleNodesInsertionRightLeftCase() {
        tree.insert(10);
        tree.insert(30);
        tree.insert(20);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.search(20)).isTrue();
        assertThat(tree.search(30)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 20, 30);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testInsertWithColorFlip() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.search(20)).isTrue();
        assertThat(tree.search(30)).isTrue();
        assertThat(tree.search(40)).isTrue();
        assertThat(tree.search(50)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 20, 30, 40, 50);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDuplicateKeys() {
        tree.insert(10);
        tree.insert(10);

        assertThat(tree.search(10)).isTrue();
        assertThat(tree.inOrderTraversal()).containsExactly(10, 10);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testTreeMinimum() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);

        assertThat(tree.treeMinimum()).isEqualTo(20);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testTreeMaximum() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        assertThat(tree.treeMaximum()).isEqualTo(80);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testFindSuccessor() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        assertThat(tree.findSuccessor(20)).isEqualTo(30);
        assertThat(tree.findSuccessor(30)).isEqualTo(40);
        assertThat(tree.findSuccessor(40)).isEqualTo(50);
        assertThat(tree.findSuccessor(50)).isEqualTo(60);
        assertThat(tree.findSuccessor(60)).isEqualTo(70);
        assertThat(tree.findSuccessor(70)).isEqualTo(80);
        assertThat(tree.findSuccessor(80)).isNull();
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testFindPredecessor() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        assertThat(tree.findPredecessor(80)).isEqualTo(70);
        assertThat(tree.findPredecessor(70)).isEqualTo(60);
        assertThat(tree.findPredecessor(60)).isEqualTo(50);
        assertThat(tree.findPredecessor(50)).isEqualTo(40);
        assertThat(tree.findPredecessor(40)).isEqualTo(30);
        assertThat(tree.findPredecessor(30)).isEqualTo(20);
        assertThat(tree.findPredecessor(20)).isNull();
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDeleteLeafNode() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);

        boolean deleted = tree.delete(30);

        assertTrue(deleted);
        assertFalse(tree.search(30));
        assertTrue(tree.search(50));
        assertTrue(tree.search(70));
        assertThat(tree.inOrderTraversal()).containsExactly(50, 70);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDeleteNodeWithOneChild() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);

        boolean deleted = tree.delete(30);

        assertTrue(deleted);
        assertFalse(tree.search(30));
        assertTrue(tree.search(50));
        assertTrue(tree.search(70));
        assertTrue(tree.search(20));
        assertThat(tree.inOrderTraversal()).containsExactly(20, 50, 70);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDeleteNodeWithTwoChildren() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        boolean deleted = tree.delete(50);

        assertTrue(deleted);
        assertFalse(tree.search(50));
        assertTrue(tree.search(30));
        assertTrue(tree.search(70));
        assertThat(tree.inOrderTraversal()).containsExactly(20, 30, 40, 60, 70, 80);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDeleteRootNode() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        boolean deleted = tree.delete(20);

        assertTrue(deleted);
        assertFalse(tree.search(20));
        assertTrue(tree.search(10));
        assertTrue(tree.search(30));
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testDeleteNonExistentNode() {
        tree.insert(10);
        tree.insert(20);

        boolean deleted = tree.delete(30);

        assertFalse(deleted);
        assertTrue(tree.search(10));
        assertTrue(tree.search(20));
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testComplexInsertionAndDeletion() {
        List<Integer> keys = Arrays.asList(10, 5, 15, 3, 7, 12, 18, 2, 4, 6, 8, 11, 13, 16, 19);
        
        keys.forEach(tree::insert);
        
        assertThat(tree.inOrderTraversal()).containsExactly(2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 15, 16, 18, 19);
        assertTrue(tree.validateRedBlackInvariants());

        List<Integer> toDelete = Arrays.asList(5, 15, 10, 2, 19);
        toDelete.forEach(tree::delete);

        assertThat(tree.inOrderTraversal()).containsExactly(3, 4, 6, 7, 8, 11, 12, 13, 16, 18);
        assertTrue(tree.validateRedBlackInvariants());
    }

    @Test
    void testRandomInsertionsAndDeletions() {
        List<Integer> keys = Arrays.asList(40, 20, 60, 10, 30, 50, 70, 5, 15, 25, 35, 45, 55, 65, 75);
        Collections.shuffle(keys);
        
        keys.forEach(tree::insert);
        
        assertThat(tree.inOrderTraversal()).containsExactly(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75);
        assertTrue(tree.validateRedBlackInvariants());

        Collections.shuffle(keys);
        keys.forEach(tree::delete);

        assertTrue(tree.isEmpty());
        assertTrue(tree.validateRedBlackInvariants());
    }
}
