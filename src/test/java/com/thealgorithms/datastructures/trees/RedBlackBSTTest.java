package com.thealgorithms.datastructures.trees;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RedBlackBSTTest {

    private static final int RED = 0;
    private static final int BLACK = 1;

    private RedBlackBST tree;
    private Object root;
    private Object nil;
    private Constructor<?> nodeConstructor;
    private Method insertMethod;
    private Method deleteMethod;
    private Method findNodeMethod;
    private Method treeMinimumMethod;

    @BeforeEach
    void setUp() throws Exception {
        tree = new RedBlackBST();
        initReflection();
    }

    private void initReflection() throws Exception {
        Field rootField = RedBlackBST.class.getDeclaredField("root");
        rootField.setAccessible(true);
        root = rootField.get(tree);

        Field nilField = RedBlackBST.class.getDeclaredField("nil");
        nilField.setAccessible(true);
        nil = nilField.get(tree);

        Class<?> nodeClass = Class.forName("com.thealgorithms.datastructures.trees.RedBlackBST$Node");
        nodeConstructor = nodeClass.getDeclaredConstructor(RedBlackBST.class, int.class);
        nodeConstructor.setAccessible(true);

        insertMethod = RedBlackBST.class.getDeclaredMethod("insert", nodeClass);
        insertMethod.setAccessible(true);

        deleteMethod = RedBlackBST.class.getDeclaredMethod("delete", nodeClass);
        deleteMethod.setAccessible(true);

        findNodeMethod = RedBlackBST.class.getDeclaredMethod("findNode", nodeClass, nodeClass);
        findNodeMethod.setAccessible(true);

        treeMinimumMethod = RedBlackBST.class.getDeclaredMethod("treeMinimum", nodeClass);
        treeMinimumMethod.setAccessible(true);
    }

    private void refreshRoot() {
        try {
            Field rootField = RedBlackBST.class.getDeclaredField("root");
            rootField.setAccessible(true);
            root = rootField.get(tree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object makeNode(int key) throws Exception {
        return nodeConstructor.newInstance(tree, key);
    }

    private void insert(int key) throws Exception {
        Object node = makeNode(key);
        insertMethod.invoke(tree, node);
        refreshRoot();
    }

    private boolean delete(int key) throws Exception {
        Object node = makeNode(key);
        boolean result = (boolean) deleteMethod.invoke(tree, node);
        refreshRoot();
        return result;
    }

    private Object find(int key) throws Exception {
        return findNodeMethod.invoke(tree, makeNode(key), root);
    }

    private Object treeMinimum(Object subTreeRoot) throws Exception {
        return treeMinimumMethod.invoke(tree, subTreeRoot);
    }

    private int keyOf(Object node) {
        try {
            Field keyField = node.getClass().getDeclaredField("key");
            keyField.setAccessible(true);
            return (int) keyField.get(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int colorOf(Object node) {
        try {
            Field colorField = node.getClass().getDeclaredField("color");
            colorField.setAccessible(true);
            return (int) colorField.get(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object leftOf(Object node) {
        try {
            Field leftField = node.getClass().getDeclaredField("left");
            leftField.setAccessible(true);
            return leftField.get(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object rightOf(Object node) {
        try {
            Field rightField = node.getClass().getDeclaredField("right");
            rightField.setAccessible(true);
            return rightField.get(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object parentOf(Object node) {
        try {
            Field pField = node.getClass().getDeclaredField("p");
            pField.setAccessible(true);
            return pField.get(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void assertRedBlackInvariants() {
        assertThat(colorOf(root))
            .as("Root must be black")
            .isEqualTo(BLACK);

        assertNoConsecutiveRedNodes(root);
        assertBlackHeightConsistent(root);
    }

    private void assertNoConsecutiveRedNodes(Object node) {
        if (node == nil) {
            return;
        }
        if (colorOf(node) == RED) {
            assertThat(colorOf(leftOf(node)))
                .as("Red node " + keyOf(node) + " must have black left child")
                .isEqualTo(BLACK);
            assertThat(colorOf(rightOf(node)))
                .as("Red node " + keyOf(node) + " must have black right child")
                .isEqualTo(BLACK);
        }
        assertNoConsecutiveRedNodes(leftOf(node));
        assertNoConsecutiveRedNodes(rightOf(node));
    }

    private int assertBlackHeightConsistent(Object node) {
        if (node == nil) {
            return 1;
        }
        int leftBH = assertBlackHeightConsistent(leftOf(node));
        int rightBH = assertBlackHeightConsistent(rightOf(node));
        assertThat(leftBH)
            .as("Black height must be consistent at node " + keyOf(node))
            .isEqualTo(rightBH);
        return leftBH + (colorOf(node) == BLACK ? 1 : 0);
    }

    private List<Integer> inorderKeys() {
        List<Integer> keys = new ArrayList<>();
        inorderCollect(root, keys);
        return keys;
    }

    private void inorderCollect(Object node, List<Integer> keys) {
        if (node == nil) {
            return;
        }
        inorderCollect(leftOf(node), keys);
        keys.add(keyOf(node));
        inorderCollect(rightOf(node), keys);
    }

    @Nested
    @DisplayName("Empty tree operations")
    class EmptyTreeTests {

        @Test
        @DisplayName("New tree has nil root with black color")
        void testEmptyTreeInitialState() {
            assertThat(root).isSameAs(nil);
            assertThat(colorOf(root)).isEqualTo(BLACK);
        }

        @Test
        @DisplayName("findNode returns null for any key in empty tree")
        void testFindInEmptyTree() throws Exception {
            assertThat(find(10)).isNull();
            assertThat(find(0)).isNull();
            assertThat(find(-5)).isNull();
        }

        @Test
        @DisplayName("delete returns false for any key in empty tree")
        void testDeleteFromEmptyTree() throws Exception {
            assertThat(delete(10)).isFalse();
            assertThat(delete(0)).isFalse();
            assertThat(root).isSameAs(nil);
        }

        @Test
        @DisplayName("treeMinimum on nil returns nil")
        void testTreeMinimumOnEmpty() throws Exception {
            assertThat(treeMinimum(nil)).isSameAs(nil);
        }
    }

    @Nested
    @DisplayName("Single node operations")
    class SingleNodeTests {

        @Test
        @DisplayName("Insert single node: root is black with nil children")
        void testInsertSingleNode() throws Exception {
            insert(42);

            assertThat(root).isNotSameAs(nil);
            assertThat(keyOf(root)).isEqualTo(42);
            assertThat(colorOf(root)).isEqualTo(BLACK);
            assertThat(leftOf(root)).isSameAs(nil);
            assertThat(rightOf(root)).isSameAs(nil);
            assertThat(parentOf(root)).isSameAs(nil);

            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("findNode locates the single inserted node")
        void testFindSingleNode() throws Exception {
            insert(42);

            Object found = find(42);
            assertThat(found).isNotNull();
            assertThat(keyOf(found)).isEqualTo(42);

            assertThat(find(100)).isNull();
            assertThat(find(0)).isNull();
        }

        @Test
        @DisplayName("treeMinimum returns the root when tree has one node")
        void testTreeMinimumSingleNode() throws Exception {
            insert(42);
            assertThat(keyOf(treeMinimum(root))).isEqualTo(42);
        }
    }

    @Nested
    @DisplayName("Multi-node insertions triggering rotations and color flips")
    class RotationAndColorFlipTests {

        @Test
        @DisplayName("Left rotation triggered by inserting larger keys in order")
        void testLeftRotation() throws Exception {
            insert(10);
            insert(20);
            insert(30);

            assertRedBlackInvariants();
            assertThat(inorderKeys()).containsExactly(10, 20, 30);
            assertThat(keyOf(root)).isEqualTo(20);
            assertThat(colorOf(root)).isEqualTo(BLACK);
            assertThat(keyOf(leftOf(root))).isEqualTo(10);
            assertThat(colorOf(leftOf(root))).isEqualTo(RED);
            assertThat(keyOf(rightOf(root))).isEqualTo(30);
            assertThat(colorOf(rightOf(root))).isEqualTo(RED);
        }

        @Test
        @DisplayName("Right rotation triggered by inserting smaller keys in order")
        void testRightRotation() throws Exception {
            insert(30);
            insert(20);
            insert(10);

            assertRedBlackInvariants();
            assertThat(inorderKeys()).containsExactly(10, 20, 30);
            assertThat(keyOf(root)).isEqualTo(20);
            assertThat(colorOf(root)).isEqualTo(BLACK);
            assertThat(keyOf(leftOf(root))).isEqualTo(10);
            assertThat(colorOf(leftOf(root))).isEqualTo(RED);
            assertThat(keyOf(rightOf(root))).isEqualTo(30);
            assertThat(colorOf(rightOf(root))).isEqualTo(RED);
        }

        @Test
        @DisplayName("Color flip triggered when uncle is red")
        void testColorFlip() throws Exception {
            insert(10);
            insert(5);
            insert(15);
            insert(3);

            assertRedBlackInvariants();
            assertThat(inorderKeys()).containsExactly(3, 5, 10, 15);
            assertThat(colorOf(root)).isEqualTo(BLACK);
        }

        @Test
        @DisplayName("Left-right case triggers double rotation")
        void testLeftRightCase() throws Exception {
            insert(30);
            insert(10);
            insert(20);

            assertRedBlackInvariants();
            assertThat(inorderKeys()).containsExactly(10, 20, 30);
            assertThat(keyOf(root)).isEqualTo(20);
            assertThat(colorOf(root)).isEqualTo(BLACK);
            assertThat(keyOf(leftOf(root))).isEqualTo(10);
            assertThat(keyOf(rightOf(root))).isEqualTo(30);
        }

        @Test
        @DisplayName("Right-left case triggers double rotation")
        void testRightLeftCase() throws Exception {
            insert(10);
            insert(30);
            insert(20);

            assertRedBlackInvariants();
            assertThat(inorderKeys()).containsExactly(10, 20, 30);
            assertThat(keyOf(root)).isEqualTo(20);
            assertThat(colorOf(root)).isEqualTo(BLACK);
            assertThat(keyOf(leftOf(root))).isEqualTo(10);
            assertThat(keyOf(rightOf(root))).isEqualTo(30);
        }

        @Test
        @DisplayName("Complex insertion sequence maintains all invariants")
        void testComplexInsertionSequence() throws Exception {
            int[] keys = {41, 38, 31, 12, 19, 8, 45, 72, 15, 60};
            for (int k : keys) {
                insert(k);
                assertRedBlackInvariants();
            }

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).isSorted();
            assertThat(inorder).containsExactly(8, 12, 15, 19, 31, 38, 41, 45, 60, 72);
        }

        @Test
        @DisplayName("Large sorted ascending insertion maintains all invariants")
        void testLargeAscendingInsertion() throws Exception {
            for (int i = 1; i <= 50; i++) {
                insert(i);
                assertRedBlackInvariants();
            }

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).hasSize(50);
            assertThat(inorder.get(0)).isEqualTo(1);
            assertThat(inorder.get(49)).isEqualTo(50);
        }

        @Test
        @DisplayName("Large sorted descending insertion maintains all invariants")
        void testLargeDescendingInsertion() throws Exception {
            for (int i = 50; i >= 1; i--) {
                insert(i);
                assertRedBlackInvariants();
            }

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).hasSize(50);
            assertThat(inorder).isSorted();
        }
    }

    @Nested
    @DisplayName("Search and minimum queries")
    class SearchAndMinTests {

        @BeforeEach
        void buildTree() throws Exception {
            int[] keys = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
            for (int k : keys) {
                insert(k);
            }
        }

        @Test
        @DisplayName("findNode locates existing keys")
        void testFindExistingKeys() throws Exception {
            assertThat(find(50)).isNotNull();
            assertThat(find(30)).isNotNull();
            assertThat(find(10)).isNotNull();
            assertThat(find(80)).isNotNull();
            assertThat(find(25)).isNotNull();
            assertThat(find(45)).isNotNull();
        }

        @Test
        @DisplayName("findNode returns null for missing keys")
        void testFindMissingKeys() throws Exception {
            assertThat(find(100)).isNull();
            assertThat(find(0)).isNull();
            assertThat(find(55)).isNull();
            assertThat(find(15)).isNull();
            assertThat(find(-99)).isNull();
        }

        @Test
        @DisplayName("treeMinimum finds minimum from various subtrees")
        void testTreeMinimum() throws Exception {
            assertThat(keyOf(treeMinimum(root))).isEqualTo(10);
            Object rightSubtree = find(70);
            assertThat(keyOf(treeMinimum(rightSubtree))).isEqualTo(60);

            Object leaf = find(10);
            assertThat(keyOf(treeMinimum(leaf))).isEqualTo(10);
        }

        @Test
        @DisplayName("Successor: minimum of right subtree")
        void testSuccessorViaTreeMinimum() throws Exception {
            insert(75);
            Object node70 = find(70);
            assertThat(keyOf(treeMinimum(rightOf(node70)))).isEqualTo(75);
        }

        @Test
        @DisplayName("Predecessor: maximum of left subtree")
        void testPredecessorViaTreeMinimum() throws Exception {
            insert(75);
            Object node30 = find(30);
            assertThat(keyOf(treeMinimum(rightOf(node30)))).isEqualTo(35);
        }
    }

    @Nested
    @DisplayName("Deletion scenarios")
    class DeletionTests {

        @Test
        @DisplayName("Delete a red leaf node")
        void testDeleteRedLeaf() throws Exception {
            insert(10);
            insert(5);
            insert(15);

            boolean deleted = delete(15);
            assertThat(deleted).isTrue();
            assertThat(find(15)).isNull();
            assertThat(inorderKeys()).containsExactly(5, 10);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete the root when it is the only node")
        void testDeleteRootSingleNode() throws Exception {
            insert(42);
            boolean deleted = delete(42);
            assertThat(deleted).isTrue();
            assertThat(find(42)).isNull();
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete a leaf node from a larger tree")
        void testDeleteLeafFromLargerTree() throws Exception {
            int[] keys = {50, 30, 70, 20, 40, 60, 80};
            for (int k : keys) {
                insert(k);
            }

            boolean deleted = delete(20);
            assertThat(deleted).isTrue();
            assertThat(find(20)).isNull();
            assertThat(inorderKeys()).containsExactly(30, 40, 50, 60, 70, 80);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete non-existent key returns false")
        void testDeleteNonExistentKey() throws Exception {
            int[] keys = {50, 30, 70};
            for (int k : keys) {
                insert(k);
            }

            int sizeBefore = inorderKeys().size();
            assertThat(delete(999)).isFalse();
            assertThat(delete(-1)).isFalse();
            assertThat(delete(40)).isFalse();
            int sizeAfter = inorderKeys().size();
            assertThat(sizeAfter).isEqualTo(sizeBefore);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Sequential deletions maintain invariants")
        void testSequentialDeletions() throws Exception {
            int[] keys = {41, 38, 31, 12, 19, 8, 45, 72, 15, 60, 55, 90, 3, 25};
            for (int k : keys) {
                insert(k);
            }

            assertRedBlackInvariants();

            int[] toDelete = {8, 72, 15, 41, 31};
            for (int k : toDelete) {
                assertThat(delete(k)).as("Delete key " + k).isTrue();
                assertThat(find(k)).as("Key " + k + " should no longer be found").isNull();
                assertRedBlackInvariants();
            }

            List<Integer> remaining = inorderKeys();
            assertThat(remaining).isSorted();
            for (int k : toDelete) {
                assertThat(remaining).doesNotContain(k);
            }
        }

        @Test
        @DisplayName("Delete all nodes one by one until empty")
        void testDeleteAllNodes() throws Exception {
            int[] keys = {50, 30, 70, 20, 40, 60, 80};
            for (int k : keys) {
                insert(k);
            }

            for (int k : keys) {
                assertThat(delete(k)).isTrue();
            }

            assertThat(inorderKeys()).isEmpty();
            assertRedBlackInvariants();
        }
    }

    @Nested
    @DisplayName("Duplicate key handling")
    class DuplicateKeyTests {

        @Test
        @DisplayName("Inserting duplicate keys creates multiple nodes (goes to right)")
        void testDuplicateKeysInsertedToRight() throws Exception {
            insert(10);
            insert(10);
            insert(10);

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(10, 10, 10);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Deleting one duplicate leaves others intact")
        void testDeleteOneDuplicate() throws Exception {
            insert(10);
            insert(10);
            insert(10);

            boolean deleted = delete(10);
            assertThat(deleted).isTrue();

            List<Integer> inorderAfter = inorderKeys();
            assertThat(inorderAfter).hasSize(2);
            assertThat(inorderAfter).allMatch(k -> k == 10);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Mix of duplicate and unique keys works correctly")
        void testMixDuplicateAndUniqueKeys() throws Exception {
            insert(20);
            insert(10);
            insert(30);
            insert(10);
            insert(20);
            insert(30);

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(10, 10, 20, 20, 30, 30);
            assertRedBlackInvariants();

            assertThat(delete(20)).isTrue();
            assertThat(delete(30)).isTrue();

            List<Integer> afterDelete = inorderKeys();
            assertThat(afterDelete).containsExactly(10, 10, 20, 30);
            assertRedBlackInvariants();
        }
    }

    @Nested
    @DisplayName("Edge cases and boundary scenarios")
    class EdgeCaseTests {

        @Test
        @DisplayName("Insert negative keys and zero")
        void testInsertNegativeAndZeroKeys() throws Exception {
            insert(0);
            insert(-10);
            insert(-5);
            insert(5);
            insert(-20);

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(-20, -10, -5, 0, 5);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Insert Integer MIN_VALUE and MAX_VALUE")
        void testInsertMinMaxValues() throws Exception {
            insert(Integer.MIN_VALUE);
            insert(0);
            insert(Integer.MAX_VALUE);

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete the minimum key")
        void testDeleteMinKey() throws Exception {
            int[] keys = {50, 30, 70, 20, 40, 60, 80, 10};
            for (int k : keys) {
                insert(k);
            }

            Integer minBefore = inorderKeys().get(0);
            assertThat(minBefore).isEqualTo(10);

            assertThat(delete(10)).isTrue();
            assertThat(find(10)).isNull();

            List<Integer> inorderAfter = inorderKeys();
            assertThat(inorderAfter.get(0)).isEqualTo(20);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete the maximum key")
        void testDeleteMaxKey() throws Exception {
            int[] keys = {50, 30, 70, 20, 40, 60, 80};
            for (int k : keys) {
                insert(k);
            }

            List<Integer> inorder = inorderKeys();
            assertThat(inorder.get(inorder.size() - 1)).isEqualTo(80);

            assertThat(delete(80)).isTrue();
            assertThat(find(80)).isNull();

            List<Integer> inorderAfter = inorderKeys();
            assertThat(inorderAfter.get(inorderAfter.size() - 1)).isEqualTo(70);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Insert and delete alternating pattern")
        void testInsertDeleteAlternating() throws Exception {
            insert(10);
            assertRedBlackInvariants();

            insert(20);
            assertRedBlackInvariants();

            assertThat(delete(10)).isTrue();
            assertThat(find(10)).isNull();
            assertRedBlackInvariants();

            insert(30);
            assertRedBlackInvariants();

            insert(10);
            assertRedBlackInvariants();

            assertThat(delete(20)).isTrue();
            assertRedBlackInvariants();

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(10, 30);
        }

        @Test
        @DisplayName("Reinserting deleted keys works correctly")
        void testReinsertDeletedKey() throws Exception {
            insert(10);
            insert(20);
            insert(30);

            assertThat(delete(20)).isTrue();
            assertThat(find(20)).isNull();

            insert(20);
            assertThat(find(20)).isNotNull();

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).containsExactly(10, 20, 30);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete root with left subtree")
        void testDeleteRootWithLeftSubtree() throws Exception {
            insert(10);
            insert(5);
            insert(3);
            insert(7);

            assertThat(delete(10)).isTrue();
            assertThat(find(10)).isNull();
            assertThat(inorderKeys()).containsExactly(3, 5, 7);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Delete root with right subtree")
        void testDeleteRootWithRightSubtree() throws Exception {
            insert(10);
            insert(15);
            insert(13);
            insert(17);

            assertThat(delete(10)).isTrue();
            assertThat(find(10)).isNull();
            assertThat(inorderKeys()).containsExactly(13, 15, 17);
            assertRedBlackInvariants();
        }

        @Test
        @DisplayName("Deleting random inserted keys one by one")
        void testRandomInsertAndDeleteAll() throws Exception {
            Set<Integer> keySet = new HashSet<>();
            java.util.Random rng = new java.util.Random(42);
            for (int i = 0; i < 50; i++) {
                int key = rng.nextInt(1000);
                keySet.add(key);
            }

            for (int key : keySet) {
                insert(key);
                assertRedBlackInvariants();
            }

            List<Integer> remainingKeys = new ArrayList<>(keySet);
            for (int key : remainingKeys) {
                assertThat(delete(key)).as("Delete key " + key).isTrue();
            }

            assertThat(inorderKeys()).isEmpty();
            assertRedBlackInvariants();
        }
    }

    @Nested
    @DisplayName("Red-black invariants across many random operations")
    class InvariantStressTests {

        @Test
        @DisplayName("1000 random insertions always maintain invariants")
        void testManyRandomInsertions() throws Exception {
            java.util.Random rng = new java.util.Random(12345);
            Set<Integer> inserted = new HashSet<>();

            for (int i = 0; i < 1000; i++) {
                int key = rng.nextInt();
                inserted.add(key);
                insert(key);
                assertRedBlackInvariants();
            }

            List<Integer> inorder = inorderKeys();
            assertThat(inorder).hasSize(inserted.size());
            assertThat(inorder).isSorted();
        }

        @Test
        @DisplayName("Mixed insertions and deletions maintain invariants")
        void testMixedInsertDeleteStress() throws Exception {
            java.util.Random rng = new java.util.Random(6789);
            List<Integer> present = new ArrayList<>();

            for (int i = 0; i < 500; i++) {
                if (rng.nextBoolean() || present.isEmpty()) {
                    int key = rng.nextInt(10000);
                    insert(key);
                    present.add(key);
                } else {
                    int idx = rng.nextInt(present.size());
                    int key = present.remove(idx);
                    delete(key);
                }
                assertRedBlackInvariants();

                List<Integer> inorder = inorderKeys();
                assertThat(inorder).hasSize(present.size());
                assertThat(inorder).isSorted();
            }
        }

        @Test
        @DisplayName("100 ascending insertions then 100 descending deletions maintain invariants")
        void testAscendingInsertDescendingDelete() throws Exception {
            for (int i = 1; i <= 100; i++) {
                insert(i);
                assertRedBlackInvariants();
            }

            for (int i = 100; i >= 1; i--) {
                assertThat(delete(i)).isTrue();
                assertRedBlackInvariants();
            }

            assertThat(inorderKeys()).isEmpty();
        }
    }
}