package com.thealgorithms.datastructures.trees;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RedBlackBSTTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Class<?> nodeClass;
    private Constructor<?> nodeConstructor;
    private Method insertMethod;
    private Method deleteMethod;
    private Method findNodeMethod;
    private Method treeMinimumMethod;
    private Method printTreeMethod;
    private Method printTreePreMethod;
    private Field rootField;
    private Field nilField;
    private Field keyField;
    private Field colorField;
    private Field leftField;
    private Field rightField;
    private Field parentField;
    private int redColor;
    private int blackColor;

    @BeforeEach
    public void setUp() throws ReflectiveOperationException {
        nodeClass = RedBlackBST.class.getDeclaredClasses()[0];
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
        printTreeMethod = RedBlackBST.class.getDeclaredMethod("printTree", nodeClass);
        printTreeMethod.setAccessible(true);
        printTreePreMethod = RedBlackBST.class.getDeclaredMethod("printTreepre", nodeClass);
        printTreePreMethod.setAccessible(true);

        rootField = RedBlackBST.class.getDeclaredField("root");
        rootField.setAccessible(true);
        nilField = RedBlackBST.class.getDeclaredField("nil");
        nilField.setAccessible(true);

        keyField = nodeClass.getDeclaredField("key");
        keyField.setAccessible(true);
        colorField = nodeClass.getDeclaredField("color");
        colorField.setAccessible(true);
        leftField = nodeClass.getDeclaredField("left");
        leftField.setAccessible(true);
        rightField = nodeClass.getDeclaredField("right");
        rightField.setAccessible(true);
        parentField = nodeClass.getDeclaredField("p");
        parentField.setAccessible(true);

        Field redField = RedBlackBST.class.getDeclaredField("RED");
        redField.setAccessible(true);
        redColor = redField.getInt(null);
        Field blackField = RedBlackBST.class.getDeclaredField("BLACK");
        blackField.setAccessible(true);
        blackColor = blackField.getInt(null);
    }

    @Test
    public void testEmptyTreeOperations() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();

        assertThat(getRoot(tree)).isSameAs(getNil(tree));
        assertThat(findActualNode(tree, 42)).isNull();
        assertThat(deleteByKey(tree, 42)).isFalse();
        assertRedBlackInvariants(tree);
        assertThat(inOrderKeys(tree)).isEmpty();
    }

    @Test
    public void testSingleNodeInsertSearchAndDelete() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();

        insertKeys(tree, 10);

        Object root = getRoot(tree);
        assertThat(getKey(root)).isEqualTo(10);
        assertThat(getColor(root)).isEqualTo(blackColor);
        assertThat(minimumNode(tree)).isSameAs(root);
        assertThat(maximumNode(tree)).isSameAs(root);
        assertThat(predecessor(root, getNil(tree))).isNull();
        assertThat(successor(root, getNil(tree))).isNull();
        assertThat(findActualNode(tree, 10)).isSameAs(root);
        assertRedBlackInvariants(tree);

        assertThat(deleteNode(tree, root)).isTrue();
        assertThat(getRoot(tree)).isSameAs(getNil(tree));
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testInsertionsTriggerLeftAndRightRotations() throws ReflectiveOperationException {
        RedBlackBST leftRotationTree = new RedBlackBST();
        insertKeys(leftRotationTree, 10, 20, 30);

        assertTreeShape(leftRotationTree, 20, 10, 30);
        assertRedBlackInvariants(leftRotationTree);

        RedBlackBST rightRotationTree = new RedBlackBST();
        insertKeys(rightRotationTree, 30, 20, 10);

        assertTreeShape(rightRotationTree, 20, 10, 30);
        assertRedBlackInvariants(rightRotationTree);
    }

    @Test
    public void testInsertionsTriggerInnerRotationsAndColorFlip() throws ReflectiveOperationException {
        RedBlackBST leftRightTree = new RedBlackBST();
        insertKeys(leftRightTree, 30, 10, 20);
        assertTreeShape(leftRightTree, 20, 10, 30);
        assertRedBlackInvariants(leftRightTree);

        RedBlackBST rightLeftTree = new RedBlackBST();
        insertKeys(rightLeftTree, 10, 30, 20);
        assertTreeShape(rightLeftTree, 20, 10, 30);
        assertRedBlackInvariants(rightLeftTree);

        RedBlackBST colorFlipTree = new RedBlackBST();
        insertKeys(colorFlipTree, 10, 5, 15, 1);

        Object root = getRoot(colorFlipTree);
        Object leftChild = getLeft(root);
        Object rightChild = getRight(root);
        Object leftGrandChild = getLeft(leftChild);

        assertThat(getKey(root)).isEqualTo(10);
        assertThat(getColor(root)).isEqualTo(blackColor);
        assertThat(getKey(leftChild)).isEqualTo(5);
        assertThat(getColor(leftChild)).isEqualTo(blackColor);
        assertThat(getKey(rightChild)).isEqualTo(15);
        assertThat(getColor(rightChild)).isEqualTo(blackColor);
        assertThat(getKey(leftGrandChild)).isEqualTo(1);
        assertThat(getColor(leftGrandChild)).isEqualTo(redColor);
        assertRedBlackInvariants(colorFlipTree);
    }

    @Test
    public void testMinimumMaximumPredecessorAndSuccessorQueries() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();
        insertKeys(tree, 20, 10, 30, 5, 15, 25, 35, 13, 17);

        Object minimum = minimumNode(tree);
        Object maximum = maximumNode(tree);
        Object nodeFifteen = findActualNode(tree, 15);
        Object nodeFive = findActualNode(tree, 5);
        Object nodeThirtyFive = findActualNode(tree, 35);
        Object nil = getNil(tree);

        assertThat(getKey(minimum)).isEqualTo(5);
        assertThat(getKey(maximum)).isEqualTo(35);
        assertThat(getKey(predecessor(nodeFifteen, nil))).isEqualTo(13);
        assertThat(getKey(successor(nodeFifteen, nil))).isEqualTo(17);
        assertThat(predecessor(nodeFive, nil)).isNull();
        assertThat(successor(nodeThirtyFive, nil)).isNull();
        assertThat(inOrderKeys(tree)).containsExactly(5, 10, 13, 15, 17, 20, 25, 30, 35);
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testDuplicateKeysAreStoredAndDeletedOneAtATime() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();
        insertKeys(tree, 10, 10, 10, 5, 15);

        assertThat(inOrderKeys(tree)).containsExactly(5, 10, 10, 10, 15);
        assertThat(findNodesByKey(tree, 10)).hasSize(3);
        assertRedBlackInvariants(tree);

        assertThat(deleteNode(tree, findNodesByKey(tree, 10).get(0))).isTrue();
        assertThat(inOrderKeys(tree)).containsExactly(5, 10, 10, 15);
        assertThat(findNodesByKey(tree, 10)).hasSize(2);
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testBoundaryDeletionScenariosMaintainInvariants() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();
        insertKeys(tree, 20, 10, 30, 5, 15, 25, 35, 1, 6, 14, 16, 24, 26, 34, 36);

        assertThat(deleteNode(tree, findActualNode(tree, 1))).isTrue();
        assertThat(inOrderKeys(tree)).containsExactly(5, 6, 10, 14, 15, 16, 20, 24, 25, 26, 30, 34, 35, 36);
        assertRedBlackInvariants(tree);

        assertThat(deleteNode(tree, findActualNode(tree, 5))).isTrue();
        assertThat(inOrderKeys(tree)).containsExactly(6, 10, 14, 15, 16, 20, 24, 25, 26, 30, 34, 35, 36);
        assertRedBlackInvariants(tree);

        assertThat(deleteNode(tree, getRoot(tree))).isTrue();
        assertThat(inOrderKeys(tree)).containsExactly(6, 10, 14, 15, 16, 24, 25, 26, 30, 34, 35, 36);
        assertRedBlackInvariants(tree);

        assertThat(deleteNode(tree, findActualNode(tree, 36))).isTrue();
        assertThat(inOrderKeys(tree)).containsExactly(6, 10, 14, 15, 16, 24, 25, 26, 30, 34, 35);
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testPrintMethodsProduceExpectedTraversalOutput() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();
        insertKeys(tree, 20, 10, 30);

        String inOrderOutput = captureOutput(() -> invokePrintTree(tree));
        String preOrderOutput = captureOutput(() -> invokePrintTreePre(tree));

        assertThat(inOrderOutput)
            .isEqualTo(" R Key: 10 Parent: 20" + LINE_SEPARATOR + " B Key: 20 Parent: -1" + LINE_SEPARATOR + " R Key: 30 Parent: 20" + LINE_SEPARATOR);
        assertThat(preOrderOutput)
            .isEqualTo(" B Key: 20 Parent: -1" + LINE_SEPARATOR + " R Key: 10 Parent: 20" + LINE_SEPARATOR + " R Key: 30 Parent: 20" + LINE_SEPARATOR);
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testInsertDemoBuildsBalancedTreeFromInput() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();

        String output = captureIo("20 10 30 -999" + LINE_SEPARATOR, tree::insertDemo);

        assertThat(output)
            .contains("Add items")
            .contains("Pre order")
            .contains(" B Key: 20 Parent: -1")
            .contains(" R Key: 10 Parent: 20")
            .contains(" R Key: 30 Parent: 20");
        assertThat(inOrderKeys(tree)).containsExactly(10, 20, 30);
        assertRedBlackInvariants(tree);
    }

    @Test
    public void testDeleteDemoDeletesRequestedNodeFromTree() throws ReflectiveOperationException {
        RedBlackBST tree = new RedBlackBST();
        insertKeys(tree, 20, 10, 30, 25);

        String output = captureIo("30" + LINE_SEPARATOR, tree::deleteDemo);

        assertThat(output)
            .contains("Delete items")
            .contains("Deleting item 30: deleted!")
            .contains(" B Key: 20 Parent: -1")
            .contains(" B Key: 25 Parent: 20");
        assertThat(inOrderKeys(tree)).containsExactly(10, 20, 25);
        assertRedBlackInvariants(tree);
    }

    private void insertKeys(RedBlackBST tree, int... keys) throws ReflectiveOperationException {
        for (int key : keys) {
            insertMethod.invoke(tree, newNode(tree, key));
        }
    }

    private Object newNode(RedBlackBST tree, int key) throws ReflectiveOperationException {
        return nodeConstructor.newInstance(tree, key);
    }

    private Object getRoot(RedBlackBST tree) throws ReflectiveOperationException {
        return rootField.get(tree);
    }

    private Object getNil(RedBlackBST tree) throws ReflectiveOperationException {
        return nilField.get(tree);
    }

    private Object getLeft(Object node) throws ReflectiveOperationException {
        return leftField.get(node);
    }

    private Object getRight(Object node) throws ReflectiveOperationException {
        return rightField.get(node);
    }

    private Object getParent(Object node) throws ReflectiveOperationException {
        return parentField.get(node);
    }

    private int getKey(Object node) throws ReflectiveOperationException {
        return keyField.getInt(node);
    }

    private int getColor(Object node) throws ReflectiveOperationException {
        return colorField.getInt(node);
    }

    private Object findActualNode(RedBlackBST tree, int key) throws ReflectiveOperationException {
        return findNodeMethod.invoke(tree, newNode(tree, key), getRoot(tree));
    }

    private boolean deleteByKey(RedBlackBST tree, int key) throws ReflectiveOperationException {
        return (boolean) deleteMethod.invoke(tree, newNode(tree, key));
    }

    private boolean deleteNode(RedBlackBST tree, Object node) throws ReflectiveOperationException {
        return (boolean) deleteMethod.invoke(tree, node);
    }

    private Object minimumNode(RedBlackBST tree) throws ReflectiveOperationException {
        Object root = getRoot(tree);
        if (root == getNil(tree)) {
            return null;
        }
        return treeMinimumMethod.invoke(tree, root);
    }

    private Object maximumNode(RedBlackBST tree) throws ReflectiveOperationException {
        Object nil = getNil(tree);
        Object current = getRoot(tree);
        if (current == nil) {
            return null;
        }
        while (getRight(current) != nil) {
            current = getRight(current);
        }
        return current;
    }

    private Object predecessor(Object node, Object nil) throws ReflectiveOperationException {
        if (getLeft(node) != nil) {
            return maximumNode(getLeft(node), nil);
        }
        Object current = node;
        Object parent = getParent(current);
        while (parent != nil && current == getLeft(parent)) {
            current = parent;
            parent = getParent(parent);
        }
        return parent == nil ? null : parent;
    }

    private Object successor(Object node, Object nil) throws ReflectiveOperationException {
        if (getRight(node) != nil) {
            return minimumNode(getRight(node), nil);
        }
        Object current = node;
        Object parent = getParent(current);
        while (parent != nil && current == getRight(parent)) {
            current = parent;
            parent = getParent(parent);
        }
        return parent == nil ? null : parent;
    }

    private Object minimumNode(Object node, Object nil) throws ReflectiveOperationException {
        Object current = node;
        while (getLeft(current) != nil) {
            current = getLeft(current);
        }
        return current;
    }

    private Object maximumNode(Object node, Object nil) throws ReflectiveOperationException {
        Object current = node;
        while (getRight(current) != nil) {
            current = getRight(current);
        }
        return current;
    }

    private List<Integer> inOrderKeys(RedBlackBST tree) throws ReflectiveOperationException {
        List<Integer> keys = new ArrayList<>();
        collectInOrder(getRoot(tree), getNil(tree), keys, java.util.Collections.newSetFromMap(new IdentityHashMap<>()));
        return keys;
    }

    private List<Object> findNodesByKey(RedBlackBST tree, int key) throws ReflectiveOperationException {
        List<Object> nodes = new ArrayList<>();
        collectNodesByKey(getRoot(tree), getNil(tree), key, nodes, java.util.Collections.newSetFromMap(new IdentityHashMap<>()));
        return nodes;
    }

    private void collectInOrder(Object node, Object nil, List<Integer> keys, Set<Object> visited) throws ReflectiveOperationException {
        if (node == nil) {
            return;
        }
        assertThat(visited.add(node)).isTrue();
        collectInOrder(getLeft(node), nil, keys, visited);
        keys.add(getKey(node));
        collectInOrder(getRight(node), nil, keys, visited);
    }

    private void collectNodesByKey(Object node, Object nil, int key, List<Object> nodes, Set<Object> visited) throws ReflectiveOperationException {
        if (node == nil) {
            return;
        }
        assertThat(visited.add(node)).isTrue();
        collectNodesByKey(getLeft(node), nil, key, nodes, visited);
        if (getKey(node) == key) {
            nodes.add(node);
        }
        collectNodesByKey(getRight(node), nil, key, nodes, visited);
    }

    private void assertTreeShape(RedBlackBST tree, int rootKey, int leftKey, int rightKey) throws ReflectiveOperationException {
        Object root = getRoot(tree);
        assertThat(getKey(root)).isEqualTo(rootKey);
        assertThat(getColor(root)).isEqualTo(blackColor);
        assertThat(getKey(getLeft(root))).isEqualTo(leftKey);
        assertThat(getKey(getRight(root))).isEqualTo(rightKey);
    }

    private void assertRedBlackInvariants(RedBlackBST tree) throws ReflectiveOperationException {
        Object root = getRoot(tree);
        Object nil = getNil(tree);
        if (root == nil) {
            return;
        }
        assertThat(getColor(root)).isEqualTo(blackColor);
        assertThat(validateNode(root, nil, null, null, java.util.Collections.newSetFromMap(new IdentityHashMap<>()))).isPositive();
        assertThat(inOrderKeys(tree)).isSorted();
    }

    private int validateNode(Object node, Object nil, Long minInclusive, Long maxExclusive, Set<Object> visited)
        throws ReflectiveOperationException {
        if (node == nil) {
            return 1;
        }

        assertThat(visited.add(node)).isTrue();

        int key = getKey(node);
        if (minInclusive != null) {
            assertThat((long) key).isGreaterThanOrEqualTo(minInclusive);
        }
        if (maxExclusive != null) {
            assertThat((long) key).isLessThan(maxExclusive);
        }

        Object left = getLeft(node);
        Object right = getRight(node);
        if (left != nil) {
            assertThat(getParent(left)).isSameAs(node);
            assertThat(getKey(left)).isLessThan(key);
        }
        if (right != nil) {
            assertThat(getParent(right)).isSameAs(node);
            assertThat(getKey(right)).isGreaterThanOrEqualTo(key);
        }
        if (getColor(node) == redColor) {
            assertThat(getColor(left)).isEqualTo(blackColor);
            assertThat(getColor(right)).isEqualTo(blackColor);
        }

        int leftBlackHeight = validateNode(left, nil, minInclusive, (long) key, visited);
        int rightBlackHeight = validateNode(right, nil, (long) key, maxExclusive, visited);
        assertThat(leftBlackHeight).isEqualTo(rightBlackHeight);
        return leftBlackHeight + (getColor(node) == blackColor ? 1 : 0);
    }

    private void invokePrintTree(RedBlackBST tree) throws ReflectiveOperationException {
        printTreeMethod.invoke(tree, getRoot(tree));
    }

    private void invokePrintTreePre(RedBlackBST tree) throws ReflectiveOperationException {
        printTreePreMethod.invoke(tree, getRoot(tree));
    }

    private String captureOutput(ThrowingRunnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            action.run();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        } finally {
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    private String captureIo(String input, ThrowingRunnable action) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            action.run();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws ReflectiveOperationException;
    }
}
